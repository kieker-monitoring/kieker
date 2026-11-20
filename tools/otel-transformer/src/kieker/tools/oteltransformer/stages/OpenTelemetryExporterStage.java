/***************************************************************************
 * Copyright (C) 2024 Kieker Project (https://kieker-monitoring.net)
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.model.system.model.ExecutionTrace;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporterBuilder;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporterBuilder;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.common.export.MemoryMode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import teetime.framework.AbstractConsumerStage;

/**
 * A stage for exporting Kieker traces to an OpenTelemetry backend via OTLP.
 *
 * @author DaGeRe
 * @author Malte Hansen
 */
public class OpenTelemetryExporterStage extends AbstractConsumerStage<ExecutionTrace> {

	public static final String PREFIX = OpenTelemetryExporterStage.class.getName() + ".";

	/**
	 * The OTLP transport protocol: <code>grpc</code> (default) or <code>http/protobuf</code>.
	 */
	public static final String EXPORT_TYPE = PREFIX + "ExportType";

	/**
	 * The base URL of the OTLP endpoint, by default http://localhost:4317 for gRPC and http://localhost:4318 for HTTP.
	 */
	public static final String EXPORT_URL = PREFIX + "ExportURL";

	/**
	 * The service name used for the spans of all hosts. By default, the Kieker hostname is used as service name.
	 */
	public static final String SERVICE_NAME = PREFIX + "ServiceName";

	/**
	 * Additional attributes for the resource of every span, in the form <code>key1=value1,key2=value2</code>.
	 */
	public static final String RESOURCE_ATTRIBUTES = PREFIX + "ResourceAttributes";

	/**
	 * Headers sent with every export request, in the form <code>key1=value1,key2=value2</code>.
	 */
	public static final String HEADERS = PREFIX + "Headers";

	/**
	 * A namespace that is hashed into the trace IDs, to keep traces of unrelated logs with equal Kieker trace IDs apart.
	 */
	public static final String TRACE_ID_NAMESPACE = PREFIX + "TraceIdNamespace";

	/**
	 * The unit of the Kieker timestamps, as name of a {@link TimeUnit}. By default, nanoseconds.
	 */
	public static final String TIME_UNIT = PREFIX + "TimeUnit";

	/** The fully qualified name of the queue to be used for the records. */
	public static final String RECORD_QUEUE_FQN = "RecordQueueFQN";

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenTelemetryExporterStage.class);

	/** The default batch size and export timeout of the OpenTelemetry SDK. */
	private static final int MAX_BATCH_SIZE = 512;
	private static final long EXPORT_TIMEOUT_SECONDS = 10;

	private final SpanExporter spanExporter;
	private final ExecutionTraceSpanConverter converter;
	private final List<SpanData> pendingSpans = new ArrayList<>();

	/**
	 * Creates a stage that is configured by the given Kieker configuration, with the standard OpenTelemetry environment
	 * variables as fallback.
	 *
	 * @param configuration
	 *            the Kieker configuration
	 */
	public OpenTelemetryExporterStage(final Configuration configuration) {
		this(ExporterSettings.create(configuration));
	}

	/**
	 * Creates a stage that exports via OTLP as specified by the given settings.
	 *
	 * @param settings
	 *            the settings of the exporter
	 */
	public OpenTelemetryExporterStage(final ExporterSettings settings) {
		this(settings, OpenTelemetryExporterStage.createSpanExporter(settings));
		final String protocol = settings.getProtocol().getName();
		final String endpoint = settings.getEndpoint();
		LOGGER.info("Exporting spans via {} to {}", protocol, endpoint);
	}

	/**
	 * Creates a stage that hands its spans to the given exporter. The stage shuts the exporter down when it terminates.
	 *
	 * @param settings
	 *            the settings of the exporter; protocol, endpoint, and headers are not used
	 * @param spanExporter
	 *            the exporter receiving the spans
	 */
	public OpenTelemetryExporterStage(final ExporterSettings settings, final SpanExporter spanExporter) {
		super();
		this.converter = new ExecutionTraceSpanConverter(settings);
		this.spanExporter = spanExporter;
	}

	private static SpanExporter createSpanExporter(final ExporterSettings settings) {
		// The stage may hand over the next batch while a timed out export is still in progress, which the default memory
		// mode of the OTLP exporters does not allow.
		switch (settings.getProtocol()) {
		case GRPC:
			final OtlpGrpcSpanExporterBuilder grpcBuilder = OtlpGrpcSpanExporter.builder().setEndpoint(settings.getEndpoint())
					.setMemoryMode(MemoryMode.IMMUTABLE_DATA);
			settings.getHeaders().forEach(grpcBuilder::addHeader);
			return grpcBuilder.build();
		case HTTP_PROTOBUF:
			final OtlpHttpSpanExporterBuilder httpBuilder = OtlpHttpSpanExporter.builder().setEndpoint(settings.getEndpoint())
					.setMemoryMode(MemoryMode.IMMUTABLE_DATA);
			settings.getHeaders().forEach(httpBuilder::addHeader);
			return httpBuilder.build();
		default:
			throw new IllegalArgumentException("Unsupported protocol " + settings.getProtocol());
		}
	}

	@Override
	protected void execute(final ExecutionTrace trace) throws Exception {
		this.pendingSpans.addAll(this.converter.convert(trace));
		while (this.pendingSpans.size() >= MAX_BATCH_SIZE) {
			this.exportBatch(MAX_BATCH_SIZE);
		}
	}

	@Override
	protected void onTerminating() {
		if (!this.pendingSpans.isEmpty()) {
			this.exportBatch(this.pendingSpans.size());
		}
		if (!this.spanExporter.shutdown().join(EXPORT_TIMEOUT_SECONDS, TimeUnit.SECONDS).isSuccess()) {
			LOGGER.warn("The span exporter did not shut down cleanly");
		}
		super.onTerminating();
	}

	private void exportBatch(final int size) {
		final List<SpanData> head = this.pendingSpans.subList(0, size);
		final CompletableResultCode result = this.spanExporter.export(new ArrayList<>(head)).join(EXPORT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
		head.clear();
		if (!result.isSuccess()) {
			final Throwable cause = result.getFailureThrowable();
			LOGGER.error("Export of {} spans failed or timed out, these spans are lost", size, cause);
		}
	}
}
