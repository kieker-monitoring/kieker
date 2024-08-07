package kieker.tools.oteltransformer.stages;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import teetime.framework.AbstractConsumerStage;

public class OpenTelemetryExporterStage extends AbstractConsumerStage<ExecutionTrace> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenTelemetryExporterStage.class);

	public enum ExportType {
		GRPC, ZIPKIN;
	}

	public static final String PREFIX = OpenTelemetryExporterStage.class.getName() + ".";

	/**
	 * The type of the export, currently supported: Zipkin and GRPC
	 */
	public static final String EXPORT_TYPE = PREFIX + "ExportType";

	/**
	 * The url, for example http://localhost:417/
	 */
	public static final String EXPORT_URL = PREFIX + "ExportURL";
	/** The fully qualified name of the queue to be used for the records. */
	public static final String RECORD_QUEUE_FQN = "RecordQueueFQN";

	private int lastEss;
	private final Stack<Span> lastSpan = new Stack<Span>();
	private final SdkTracerProvider tracerProvider;

	private final ExportType exportType;
	private final String exportUrl;

	public OpenTelemetryExporterStage(final Configuration configuration) {

		final String typeParameter = configuration.getStringProperty(EXPORT_TYPE);
		if ("GRPC".equals(typeParameter) || typeParameter == null || typeParameter.isEmpty()) {
			exportType = ExportType.GRPC;
		} else if ("zipkin".equals(typeParameter)) {
			exportType = ExportType.ZIPKIN;
		} else {
			throw new RuntimeException("Please specifiy accepted " + EXPORT_TYPE + " parameter, was " + typeParameter);
		}

		final String urlParameter = configuration.getStringProperty(EXPORT_URL);
		if (urlParameter == null || urlParameter.isEmpty()) {
			if (exportType.equals(ExportType.GRPC)) {
				exportUrl = "http://localhost:4317";
			} else if (exportType.equals(ExportType.ZIPKIN)) {
				exportUrl = "http://localhost:9411/api/v2/";
			} else {
				exportUrl = null;
			}
		} else {
			exportUrl = urlParameter;
		}

		tracerProvider = createTracerProvider("kieker-data");
	}

	private SdkTracerProvider createTracerProvider(final String serviceName) {
		final Resource resource = Resource.getDefault().merge(
				Resource.create(Attributes.builder().put(AttributeKey.stringKey("service.name"), serviceName).build()));

		final SpanExporter spanExporter = getSpanExporter();

		final SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder().setResource(resource)
				.addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build()).build();

		OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider).buildAndRegisterGlobal();

		return sdkTracerProvider;
	}

	private SpanExporter getSpanExporter() {
		final SpanExporter spanExporter;
		switch (exportType) {
		case ZIPKIN:
			spanExporter = ZipkinSpanExporter.builder().setEndpoint(exportUrl).build();
			break;
		case GRPC:
			spanExporter = OtlpGrpcSpanExporter.builder().setEndpoint(exportUrl).build();
			break;
		default:
			throw new RuntimeException("Unsupported span exporter");
		}
		return spanExporter;
	}

	private int i = 0;
	private int serviceIndex = 0;
	private final Map<String, String> serviceIndexMap = new HashMap<>();

	@Override
	protected void execute(final ExecutionTrace trace) throws Exception {
		final Tracer tracer = tracerProvider.get("kieker-import");

		for (final Execution execution : trace.getTraceAsSortedExecutionSet()) {
			final String fullClassname = execution.getOperation().getComponentType().getFullQualifiedName().intern();

			final String operationSignature = ClassOperationSignaturePair.createOperationSignatureString(fullClassname,
					execution.getOperation().getSignature());

			final SpanBuilder spanBuilder1 = tracer.spanBuilder(operationSignature);
			final SpanBuilder spanBuilder = spanBuilder1.setStartTimestamp(execution.getTin(), TimeUnit.NANOSECONDS);
			if (lastSpan != null && execution.getEss() > 0) {

				LOGGER.debug("Parent: " + execution.getEss() + " " + execution.getEoi());

				spanBuilder.setParent(Context.current().with(lastSpan.peek()));
			} else {
				LOGGER.info("Root span");
			}

			final Span span = createSpan(execution, fullClassname, spanBuilder);

			LOGGER.debug("Spans added: " + ++i);

			if (execution.getEss() >= lastEss) {
				lastEss++;
				lastSpan.add(span);
			} else if (execution.getEss() == lastEss) {
				lastSpan.pop();
				lastSpan.add(span);
			} else {
				lastEss--;
				lastSpan.pop();
				lastSpan.add(span);
			}
		}
	}

	private Span createSpan(final Execution execution, final String fullClassname, final SpanBuilder spanBuilder) {
		final Span span = spanBuilder.startSpan();

		try (Scope scope = span.makeCurrent()) {
			final String serviceName = execution.getAllocationComponent().getExecutionContainer().getName();
			span.setAttribute("service.name", serviceName);
			String serviceInstanceId = serviceIndexMap.get(serviceName);
			if (serviceInstanceId == null) {
				serviceInstanceId = Integer.toString(serviceIndex++);
				serviceIndexMap.put(serviceName, serviceInstanceId);
			}
			span.setAttribute("service.instance.id", serviceInstanceId);
			span.setAttribute("code.namespace", fullClassname);
			span.setAttribute("code.function", execution.getOperation().getSignature().getName());
			span.setAttribute("telemetry.sdk.language", "java");
			span.setAttribute("explorviz.token.id", "mytokenvalue");
			span.setAttribute("explorviz.token.secret", "mytokensecret");
		} finally {
			span.end(execution.getTout(), TimeUnit.NANOSECONDS);
		}
		return span;
	}
}
