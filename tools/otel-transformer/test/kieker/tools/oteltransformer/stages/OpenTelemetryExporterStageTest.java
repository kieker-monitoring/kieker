/***************************************************************************
 * Copyright (C) 2026 Kieker Project (https://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.oteltransformer.stages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.Version;
import kieker.common.util.signature.Signature;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.AssemblyComponent;
import kieker.model.system.model.ComponentType;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionContainer;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.Operation;
import kieker.model.system.model.exceptions.InvalidTraceException;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.data.StatusData;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.CodeAttributes;
import io.opentelemetry.semconv.ServiceAttributes;
import io.opentelemetry.semconv.TelemetryAttributes;
import teetime.framework.test.StageTester;

/**
 * Tests the conversion of Kieker execution traces to OpenTelemetry spans by the {@link OpenTelemetryExporterStage}.
 *
 * @author Malte Hansen
 */
public class OpenTelemetryExporterStageTest {

	private static final String HOST_A = "host-a";
	private static final String HOST_B = "host-b";
	private static final String CLASS_A = "org.shop.A";
	private static final String SESSION = "session-42";
	private static final String NO_SESSION = OperationExecutionRecord.NO_SESSION_ID;
	private static final String SCHEMA_URL = "https://opentelemetry.io/schemas/1.45.0";

	private final Configuration configuration = new Configuration();
	private final RecordingSpanExporter exporter = new RecordingSpanExporter();

	public OpenTelemetryExporterStageTest() {
		// default constructor
	}

	private List<SpanData> export(final ExecutionTrace... traces) {
		final OpenTelemetryExporterStage stage = new OpenTelemetryExporterStage(ExporterSettings.create(this.configuration, name -> null), this.exporter);
		StageTester.test(stage).and().send(Arrays.asList(traces)).to(stage.getInputPort()).start();
		return this.exporter.spans;
	}

	private static ExecutionTrace trace(final long traceId, final Execution... executions) throws InvalidTraceException {
		final ExecutionTrace trace = new ExecutionTrace(traceId);
		for (final Execution execution : executions) {
			trace.add(execution);
		}
		return trace;
	}

	private static Execution execution(final String hostname, final String className, final String method, final long traceId, final int eoi,
			final int ess) {
		final Operation operation = OpenTelemetryExporterStageTest.operation(className, method);
		return new Execution(operation, OpenTelemetryExporterStageTest.component(hostname, operation), traceId, NO_SESSION, eoi, ess, 1000L + eoi,
				2000L - eoi, false);
	}

	private static Operation operation(final String className, final String method) {
		return new Operation(1, new ComponentType(1, className),
				new Signature(method, new String[] { "public" }, "boolean", new String[] { "int", "long" }));
	}

	private static AllocationComponent component(final String hostname, final Operation operation) {
		return new AllocationComponent(1, new AssemblyComponent(1, "assembly", operation.getComponentType()),
				new ExecutionContainer(1, null, hostname));
	}

	@Test
	public void executionBecomesInternalSpanWithCodeAttributes() throws InvalidTraceException {
		this.configuration.setProperty(OpenTelemetryExporterStage.TIME_UNIT, "MILLISECONDS");

		final Operation getItem = OpenTelemetryExporterStageTest.operation("org.shop.Catalog", "getItem");
		final List<SpanData> spans = this.export(OpenTelemetryExporterStageTest.trace(7L,
				new Execution(getItem, OpenTelemetryExporterStageTest.component(HOST_A, getItem), 7L, SESSION, 0, 0, 1500L, 1750L, false)));

		Assert.assertEquals(1, spans.size());
		final SpanData span = spans.get(0);
		Assert.assertEquals("Catalog.getItem", span.getName());
		Assert.assertEquals(SpanKind.INTERNAL, span.getKind());
		Assert.assertEquals(StatusData.unset(), span.getStatus());
		Assert.assertTrue(span.hasEnded());
		Assert.assertEquals(TimeUnit.MILLISECONDS.toNanos(1500L), span.getStartEpochNanos());
		Assert.assertEquals(TimeUnit.MILLISECONDS.toNanos(1750L), span.getEndEpochNanos());

		Assert.assertEquals("org.shop.Catalog.getItem", span.getAttributes().get(CodeAttributes.CODE_FUNCTION_NAME));
		Assert.assertEquals("public boolean org.shop.Catalog.getItem(int, long)",
				span.getAttributes().get(ExecutionTraceSpanConverter.KIEKER_OPERATION_SIGNATURE));
		Assert.assertEquals(SESSION, span.getAttributes().get(ExecutionTraceSpanConverter.SESSION_ID));
		Assert.assertEquals(Long.valueOf(7L), span.getAttributes().get(ExecutionTraceSpanConverter.KIEKER_TRACE_ID));
		Assert.assertEquals(Long.valueOf(0L), span.getAttributes().get(ExecutionTraceSpanConverter.KIEKER_EOI));
		Assert.assertEquals(Long.valueOf(0L), span.getAttributes().get(ExecutionTraceSpanConverter.KIEKER_ESS));
		Assert.assertNull(span.getAttributes().get(ExecutionTraceSpanConverter.KIEKER_ASSUMED));
		Assert.assertEquals(span.getAttributes().size(), span.getTotalAttributeCount());

		// deprecated or resource-level attributes must not appear on the span
		Assert.assertNull(span.getAttributes().get(AttributeKey.stringKey("code.namespace")));
		Assert.assertNull(span.getAttributes().get(AttributeKey.stringKey("code.function")));
		Assert.assertNull(span.getAttributes().get(ServiceAttributes.SERVICE_NAME));

		Assert.assertEquals("kieker", span.getInstrumentationScopeInfo().getName());
		Assert.assertEquals(Version.getVERSION(), span.getInstrumentationScopeInfo().getVersion());
		Assert.assertEquals(SCHEMA_URL, span.getInstrumentationScopeInfo().getSchemaUrl());
	}

	@Test
	public void missingSessionIsOmittedAndAssumedExecutionIsFlagged() throws InvalidTraceException {
		final Operation getItem = OpenTelemetryExporterStageTest.operation("org.shop.Catalog", "getItem");
		final Operation load = OpenTelemetryExporterStageTest.operation("org.shop.Catalog", "load");
		final List<SpanData> spans = this.export(OpenTelemetryExporterStageTest.trace(1L,
				new Execution(getItem, OpenTelemetryExporterStageTest.component(HOST_A, getItem), 1L, NO_SESSION, 0, 0, 10L, 40L, false),
				new Execution(load, OpenTelemetryExporterStageTest.component(HOST_A, load), 1L, Execution.NO_SESSION_ID, 1, 1, 20L, 30L, true)));

		Assert.assertNull(spans.get(0).getAttributes().get(ExecutionTraceSpanConverter.SESSION_ID));
		Assert.assertNull(spans.get(1).getAttributes().get(ExecutionTraceSpanConverter.SESSION_ID));
		Assert.assertEquals(Boolean.TRUE, spans.get(1).getAttributes().get(ExecutionTraceSpanConverter.KIEKER_ASSUMED));
	}

	@Test
	public void parentsFollowTheCallStack() throws InvalidTraceException {
		// a() { b() { c() } d() { e() } } followed by a second trace with a single execution
		final List<SpanData> spans = this.export(
				OpenTelemetryExporterStageTest.trace(1L,
						OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "a", 1L, 0, 0),
						OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "b", 1L, 1, 1),
						OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "c", 1L, 2, 2),
						OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "d", 1L, 3, 1),
						OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "e", 1L, 4, 2)),
				OpenTelemetryExporterStageTest.trace(2L,
						OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "f", 2L, 0, 0)));

		Assert.assertEquals(6, spans.size());
		final SpanData a = spans.get(0);
		final SpanData b = spans.get(1);
		final SpanData c = spans.get(2);
		final SpanData d = spans.get(3);
		final SpanData e = spans.get(4);
		final SpanData f = spans.get(5);

		Assert.assertEquals(SpanContext.getInvalid(), a.getParentSpanContext());
		Assert.assertEquals(a.getSpanContext(), b.getParentSpanContext());
		Assert.assertEquals(b.getSpanContext(), c.getParentSpanContext());
		Assert.assertEquals(a.getSpanContext(), d.getParentSpanContext());
		Assert.assertEquals(d.getSpanContext(), e.getParentSpanContext());
		// the call stack of the first trace must not leak into the second one
		Assert.assertEquals(SpanContext.getInvalid(), f.getParentSpanContext());

		for (final SpanData span : spans.subList(0, 5)) {
			Assert.assertEquals(a.getTraceId(), span.getTraceId());
			Assert.assertTrue(span.getSpanContext().isValid());
			Assert.assertTrue(span.getSpanContext().isSampled());
		}
		Assert.assertNotEquals(a.getTraceId(), f.getTraceId());
		Assert.assertEquals(5L, spans.subList(0, 5).stream().map(SpanData::getSpanId).distinct().count());
	}

	@Test
	public void identifiersAreReproducibleAndSeparatedByNamespace() throws InvalidTraceException {
		final ExecutionTrace trace = OpenTelemetryExporterStageTest.trace(5L,
				OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "a", 5L, 0, 0),
				OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "b", 5L, 1, 1));

		final List<SpanData> first = new ArrayList<>(this.export(trace));
		this.exporter.spans.clear();
		final List<SpanData> second = new ArrayList<>(this.export(trace));
		this.exporter.spans.clear();
		this.configuration.setProperty(OpenTelemetryExporterStage.TRACE_ID_NAMESPACE, "another-run");
		final List<SpanData> otherNamespace = new ArrayList<>(this.export(trace));

		Assert.assertEquals(first.get(0).getSpanContext(), second.get(0).getSpanContext());
		Assert.assertEquals(first.get(1).getSpanContext(), second.get(1).getSpanContext());
		Assert.assertNotEquals(first.get(0).getTraceId(), otherNamespace.get(0).getTraceId());
		Assert.assertNotEquals(first.get(0).getSpanId(), otherNamespace.get(0).getSpanId());
	}

	@Test
	public void everyHostGetsItsOwnResource() throws InvalidTraceException {
		this.configuration.setProperty(OpenTelemetryExporterStage.RESOURCE_ATTRIBUTES, "deployment.environment.name=test,host.name=overridden");

		final List<SpanData> spans = this.export(OpenTelemetryExporterStageTest.trace(1L,
				OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "a", 1L, 0, 0),
				OpenTelemetryExporterStageTest.execution(HOST_B, "org.shop.B", "b", 1L, 1, 1),
				OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "c", 1L, 2, 1)));

		final SpanData hostA = spans.get(0);
		final SpanData hostB = spans.get(1);
		Assert.assertEquals(HOST_A, hostA.getResource().getAttribute(ServiceAttributes.SERVICE_NAME));
		Assert.assertEquals(HOST_A, hostA.getResource().getAttribute(ExecutionTraceSpanConverter.HOST_NAME));
		Assert.assertEquals(HOST_B, hostB.getResource().getAttribute(ServiceAttributes.SERVICE_NAME));
		Assert.assertEquals(HOST_B, hostB.getResource().getAttribute(ExecutionTraceSpanConverter.HOST_NAME));
		Assert.assertSame(hostA.getResource(), spans.get(2).getResource());
		// a call across hosts stays within one trace
		Assert.assertEquals(hostA.getSpanContext(), hostB.getParentSpanContext());

		for (final SpanData span : spans) {
			Assert.assertEquals(SCHEMA_URL, span.getResource().getSchemaUrl());
			Assert.assertEquals("test", span.getResource().getAttribute(AttributeKey.stringKey("deployment.environment.name")));
			Assert.assertEquals("opentelemetry", span.getResource().getAttribute(TelemetryAttributes.TELEMETRY_SDK_NAME));
			Assert.assertNull(span.getResource().getAttribute(ServiceAttributes.SERVICE_INSTANCE_ID));
		}
	}

	@Test
	public void configuredServiceNameAppliesToAllHosts() throws InvalidTraceException {
		this.configuration.setProperty(OpenTelemetryExporterStage.SERVICE_NAME, "shop");

		final List<SpanData> spans = this.export(OpenTelemetryExporterStageTest.trace(1L,
				OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "a", 1L, 0, 0),
				OpenTelemetryExporterStageTest.execution(HOST_B, "org.shop.B", "b", 1L, 1, 1)));

		Assert.assertEquals("shop", spans.get(0).getResource().getAttribute(ServiceAttributes.SERVICE_NAME));
		Assert.assertEquals("shop", spans.get(1).getResource().getAttribute(ServiceAttributes.SERVICE_NAME));
		Assert.assertEquals(HOST_A, spans.get(0).getResource().getAttribute(ExecutionTraceSpanConverter.HOST_NAME));
		Assert.assertEquals(HOST_B, spans.get(1).getResource().getAttribute(ExecutionTraceSpanConverter.HOST_NAME));
	}

	@Test
	public void spansAreExportedInBatchesAndFlushedOnTermination() throws InvalidTraceException {
		final int numberOfTraces = 1200;
		final ExecutionTrace[] traces = new ExecutionTrace[numberOfTraces];
		for (int i = 0; i < numberOfTraces; i++) {
			traces[i] = OpenTelemetryExporterStageTest.trace(i, OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "a", i, 0, 0));
		}

		final List<SpanData> spans = this.export(traces);

		Assert.assertEquals(numberOfTraces, spans.size());
		Assert.assertEquals(Arrays.asList(512, 512, 176), this.exporter.batchSizes);
		Assert.assertEquals(1, this.exporter.shutdowns);
	}

	@Test
	public void failedExportDoesNotStopTheStage() throws InvalidTraceException {
		this.exporter.result = CompletableResultCode.ofFailure();

		this.export(OpenTelemetryExporterStageTest.trace(1L, OpenTelemetryExporterStageTest.execution(HOST_A, CLASS_A, "a", 1L, 0, 0)));

		Assert.assertEquals(Arrays.asList(1), this.exporter.batchSizes);
		Assert.assertEquals(1, this.exporter.shutdowns);
	}

	/**
	 * Keeps all exported spans. Unlike the in-memory exporter of the OpenTelemetry SDK, it retains them after shutdown.
	 */
	private static final class RecordingSpanExporter implements SpanExporter {

		private final List<SpanData> spans = new ArrayList<>();
		private final List<Integer> batchSizes = new ArrayList<>();
		private CompletableResultCode result = CompletableResultCode.ofSuccess();
		private int shutdowns;

		RecordingSpanExporter() {
			// default constructor
		}

		@Override
		public CompletableResultCode export(final Collection<SpanData> batch) {
			this.spans.addAll(batch);
			this.batchSizes.add(batch.size());
			return this.result;
		}

		@Override
		public CompletableResultCode flush() {
			return CompletableResultCode.ofSuccess();
		}

		@Override
		public CompletableResultCode shutdown() {
			this.shutdowns++;
			return CompletableResultCode.ofSuccess();
		}
	}
}
