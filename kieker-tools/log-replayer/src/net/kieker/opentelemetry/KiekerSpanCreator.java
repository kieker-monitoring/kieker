package net.kieker.opentelemetry;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.internal.ImmutableSpanContext;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceId;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.context.Context;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.common.InstrumentationLibraryInfo;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.internal.InstrumentationScopeUtil;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class KiekerSpanCreator {

	private static final String LIBRARY_NAME = "Kieker-OpenTelemetry-Transformer";
	
	private static final int MILLI_TO_NANO = 1000000;
	private static int spanid = 1;
	private static int traceid = 1;

	private final OtlpGrpcSpanExporter spanExporter;
	private Resource resource;
	private final String traceId = TraceId.fromLongs(111121212, traceid++);
	private final Span rootSpan = Span.fromContext(Context.root());
	private final InstrumentationLibraryInfo libraryInfo = getLibraryInfo();
    private final String serviceName;

	public KiekerSpanCreator(String serviceName, String endpoint) {
		this.serviceName = serviceName;
		spanExporter = OtlpGrpcSpanExporter.builder()
				.setEndpoint(endpoint).build();

		createTracerProvider(spanExporter, serviceName);
	}

	private SdkTracerProvider createTracerProvider(OtlpGrpcSpanExporter spanExporter, String serviceName) {
		AttributesBuilder attrBuilders = Attributes.builder().put(ResourceAttributes.SERVICE_NAME, serviceName);
		resource = Resource.create(attrBuilders.build());

		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(
						BatchSpanProcessor.builder(spanExporter).build())
				.setResource(resource).build();
		return sdkTracerProvider;
	}

	public KiekerSpanData createSpan(String name, long start, long end) {
		return createSubSpan(name, start, end, rootSpan.getSpanContext());
	}
	
	public KiekerSpanData createSubSpan(String name, long start, long end, SpanContext parentSpanContext) {
		List<SpanData> spans = new LinkedList<>();

		KiekerSpanData generatedSpanData = createInternalSpan(name, parentSpanContext, start, end);
		
		spans.add(generatedSpanData);

		CompletableResultCode export = spanExporter.export(spans);
		
		// Do the following to assure completion of the export
//		export.join(1, TimeUnit.SECONDS);
//		System.out.println("Finished: " + export.isDone() + " Success: " + export.isSuccess());
		
		return generatedSpanData;
	}
	
	private KiekerSpanData createInternalSpan(String name, SpanContext parent, long start, long end) {
		String spanId = SpanId.fromLong(spanid++);
		SpanContext mySpanContext = SpanContext.create(traceId, spanId, TraceFlags.getDefault(), TraceState.getDefault());
		KiekerSpanData generatedSpan = new KiekerSpanData(name, mySpanContext, parent, start, start + 5* MILLI_TO_NANO, resource, libraryInfo);
		return generatedSpan;
	}

	private static final InstrumentationLibraryInfo getLibraryInfo() {
		InstrumentationScopeInfo result = InstrumentationScopeInfo.create(LIBRARY_NAME);
		InstrumentationLibraryInfo libraryInfo = InstrumentationScopeUtil.toInstrumentationLibraryInfo(result);
		return libraryInfo;
	}
	
	public String getServiceName() {
		return serviceName;
	}
}
