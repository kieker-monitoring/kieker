package kieker.tools.log.replayer;

import java.util.concurrent.TimeUnit;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.misc.KiekerMetadataRecord;
import teetime.framework.AbstractConsumerStage;

public class OpenTelemetryStage extends AbstractConsumerStage<IMonitoringRecord> {

	private static final String KIEKER_OTEL_TRANSFORMER = "kieker-otel";
	private static final String KIEKER_OTEL_TRANSFORMER_VERSION = "2.0.0-SNAPSHOT";

	private final Tracer tracer;
	
	public OpenTelemetryStage() {
		Resource resource = Resource.getDefault()
				.merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "logical-service-name")));

		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().build()).build())
				.setResource(resource).build();

		SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
				.registerMetricReader(
						PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().build()).build())
				.setResource(resource).build();

		OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
				.setMeterProvider(sdkMeterProvider)
				.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
				.buildAndRegisterGlobal();

		tracer = openTelemetry.getTracer(KIEKER_OTEL_TRANSFORMER, KIEKER_OTEL_TRANSFORMER_VERSION);
	}
	
	@Override
	protected void execute(IMonitoringRecord record) throws Exception {
		if (record instanceof KiekerMetadataRecord) {
			System.out.println("Ignoring metadata record");
		} else if (record instanceof OperationExecutionRecord) {

			OperationExecutionRecord operationExecutionRecord = (OperationExecutionRecord) record;

			

			SpanBuilder spanBuilder = tracer.spanBuilder(operationExecutionRecord.getOperationSignature());
			spanBuilder.setStartTimestamp(operationExecutionRecord.getTin(), TimeUnit.MILLISECONDS);
			Span span = spanBuilder.startSpan();
			span.end(operationExecutionRecord.getTout(), TimeUnit.MILLISECONDS);

			// Make the span the current span
			try (Scope ss = span.makeCurrent()) {
				// In this scope, the span is the current/active span
			} finally {
				span.end();
			}
		} else {
			throw new RuntimeException("Currently unsupported record type: " + record.getClass());
		}
	}

}
