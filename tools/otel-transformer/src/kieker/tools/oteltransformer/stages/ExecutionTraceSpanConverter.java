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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.Version;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.common.util.signature.Signature;
import kieker.model.system.model.AbstractTrace;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceId;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.semconv.CodeAttributes;
import io.opentelemetry.semconv.SchemaUrls;
import io.opentelemetry.semconv.ServiceAttributes;

/**
 * Converts Kieker execution traces to OpenTelemetry spans following the semantic conventions 1.45.0.
 *
 * @author Malte Hansen
 */
final class ExecutionTraceSpanConverter {

	/** Name of the instrumentation scope of all converted spans. */
	static final String SCOPE_NAME = "kieker";
	/** The schema URL of the semantic conventions the emitted attribute names follow. */
	static final String SCHEMA_URL = SchemaUrls.V1_45_0;

	/** Semantic convention <code>host.name</code>, not yet part of the stable semantic conventions artifact. */
	static final AttributeKey<String> HOST_NAME = AttributeKey.stringKey("host.name");
	/** Semantic convention <code>session.id</code>, not yet part of the stable semantic conventions artifact. */
	static final AttributeKey<String> SESSION_ID = AttributeKey.stringKey("session.id");

	/** The complete Kieker operation signature including modifiers, return type, and parameter types. */
	static final AttributeKey<String> KIEKER_OPERATION_SIGNATURE = AttributeKey.stringKey("kieker.operation.signature");
	/** The Kieker trace ID the OpenTelemetry trace ID was derived from. */
	static final AttributeKey<Long> KIEKER_TRACE_ID = AttributeKey.longKey("kieker.trace.id");
	/** The Kieker execution order index. */
	static final AttributeKey<Long> KIEKER_EOI = AttributeKey.longKey("kieker.eoi");
	/** The Kieker execution stack size. */
	static final AttributeKey<Long> KIEKER_ESS = AttributeKey.longKey("kieker.ess");
	/** Set to true for executions that were not measured but assumed by Kieker. */
	static final AttributeKey<Boolean> KIEKER_ASSUMED = AttributeKey.booleanKey("kieker.assumed");

	private static final int TRACE_ID_BYTES = 16;
	private static final int SPAN_ID_BYTES = 8;

	private final ExporterSettings settings;
	private final InstrumentationScopeInfo scope;
	private final Map<String, Resource> resources = new ConcurrentHashMap<>();
	private final byte[] traceIdNamespace;

	ExecutionTraceSpanConverter(final ExporterSettings settings) {
		this.settings = settings;
		this.scope = InstrumentationScopeInfo.builder(SCOPE_NAME).setVersion(Version.getVERSION()).setSchemaUrl(SCHEMA_URL).build();
		this.traceIdNamespace = settings.getTraceIdNamespace().getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Converts all executions of the given trace.
	 *
	 * @param trace
	 *            the trace to convert
	 *
	 * @return one span per execution, ordered by execution order index
	 */
	List<SpanData> convert(final ExecutionTrace trace) {
		final byte[] traceIdBytes = this.createTraceIdBytes(trace.getTraceId());
		final String traceId = TraceId.fromBytes(traceIdBytes);

		final List<SpanData> spans = new ArrayList<>(trace.getLength());
		// the executions that are still open at the current position, innermost last
		final Deque<Execution> openExecutions = new ArrayDeque<>();
		final Deque<SpanContext> openContexts = new ArrayDeque<>();

		for (final Execution execution : trace.getTraceAsSortedExecutionSet()) {
			// an execution is called by the latest preceding execution with a smaller stack size
			while (!openExecutions.isEmpty() && (openExecutions.peekLast().getEss() >= execution.getEss())) {
				openExecutions.removeLast();
				openContexts.removeLast();
			}
			final SpanContext parentContext;
			if (openContexts.isEmpty()) {
				parentContext = SpanContext.getInvalid();
			} else {
				parentContext = openContexts.peekLast();
			}
			final String spanId = ExecutionTraceSpanConverter.createSpanId(traceIdBytes, execution.getEoi());
			final SpanContext context = SpanContext.create(traceId, spanId, TraceFlags.getSampled(), TraceState.getDefault());

			spans.add(this.createSpan(execution, context, parentContext));

			openExecutions.addLast(execution);
			openContexts.addLast(context);
		}
		return spans;
	}

	private SpanData createSpan(final Execution execution, final SpanContext context, final SpanContext parentContext) {
		final String className = execution.getOperation().getComponentType().getFullQualifiedName();
		final Signature signature = execution.getOperation().getSignature();
		final String simpleClassName = className.substring(className.lastIndexOf('.') + 1);

		final AttributesBuilder attributes = Attributes.builder()
				.put(CodeAttributes.CODE_FUNCTION_NAME, className + '.' + signature.getName())
				.put(KIEKER_OPERATION_SIGNATURE, ClassOperationSignaturePair.createOperationSignatureString(className, signature))
				.put(KIEKER_TRACE_ID, execution.getTraceId())
				.put(KIEKER_EOI, execution.getEoi())
				.put(KIEKER_ESS, execution.getEss());
		if (ExecutionTraceSpanConverter.hasSession(execution.getSessionId())) {
			attributes.put(SESSION_ID, execution.getSessionId());
		}
		if (execution.isAssumed()) {
			attributes.put(KIEKER_ASSUMED, true);
		}

		final String hostname = execution.getAllocationComponent().getExecutionContainer().getName();
		return new KiekerSpanData(simpleClassName + '.' + signature.getName(), context, parentContext,
				this.settings.getTimeUnit().toNanos(execution.getTin()), this.settings.getTimeUnit().toNanos(execution.getTout()),
				attributes.build(), this.resources.computeIfAbsent(hostname, this::createResource), this.scope);
	}

	/**
	 * Creates the resource for all spans of one Kieker host. The host name is the only information Kieker records about
	 * the origin of an execution, so it also serves as service name unless one is configured.
	 */
	private Resource createResource(final String hostname) {
		final AttributesBuilder attributes = Attributes.builder().putAll(this.settings.getResourceAttributes());
		final String serviceName = Objects.requireNonNullElse(this.settings.getServiceName(), hostname);
		if (!hostname.isEmpty()) {
			attributes.put(HOST_NAME, hostname);
		}
		if (!serviceName.isEmpty()) {
			attributes.put(ServiceAttributes.SERVICE_NAME, serviceName);
		}
		// the default resource describes the OpenTelemetry SDK that emits the spans
		return Resource.getDefault().merge(Resource.create(attributes.build(), SCHEMA_URL));
	}

	private static boolean hasSession(final String sessionId) {
		return (sessionId != null) && !sessionId.isEmpty() && !Execution.NO_SESSION_ID.equals(sessionId)
				&& !OperationExecutionRecord.NO_SESSION_ID.equals(sessionId) && !AbstractTrace.DEFAULT_SESSION_ID.equals(sessionId);
	}

	/**
	 * Derives the trace ID from the configured namespace and the Kieker trace ID, so that exporting the same trace twice
	 * results in the same OpenTelemetry trace.
	 */
	private byte[] createTraceIdBytes(final long kiekerTraceId) {
		final MessageDigest digest = ExecutionTraceSpanConverter.createDigest();
		digest.update(this.traceIdNamespace);
		digest.update((byte) 0);
		digest.update(ByteBuffer.allocate(Long.BYTES).putLong(kiekerTraceId).array());
		return ExecutionTraceSpanConverter.toValidId(digest.digest(), TRACE_ID_BYTES);
	}

	private static String createSpanId(final byte[] traceIdBytes, final int eoi) {
		final MessageDigest digest = ExecutionTraceSpanConverter.createDigest();
		digest.update(traceIdBytes);
		digest.update(ByteBuffer.allocate(Integer.BYTES).putInt(eoi).array());
		return SpanId.fromBytes(ExecutionTraceSpanConverter.toValidId(digest.digest(), SPAN_ID_BYTES));
	}

	private static MessageDigest createDigest() {
		try {
			return MessageDigest.getInstance("SHA-256");
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 is required to derive trace and span IDs", e);
		}
	}

	/**
	 * Truncates the hash to the length of an ID. IDs consisting only of zeros are invalid in OpenTelemetry.
	 */
	private static byte[] toValidId(final byte[] hash, final int length) {
		final byte[] id = Arrays.copyOf(hash, length);
		for (final byte b : id) {
			if (b != 0) {
				return id;
			}
		}
		id[length - 1] = 1;
		return id;
	}
}
