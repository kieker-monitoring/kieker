package kieker.tools.log.replayer.stages;


import java.time.Instant;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import teetime.framework.AbstractConsumerStage;

public class OpenTelemetryStage extends AbstractConsumerStage<IMonitoringRecord> {
    
    private final Tracer tracer;
    

    public OpenTelemetryStage() {
    	 
        SdkTracerProvider tracerProvider = createTracerProvider();
        
        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();

        this.tracer = openTelemetry.getTracer("kieker-instrumentation");
    }
    
    private OtlpHttpSpanExporter createSpanExporter() {
        return OtlpHttpSpanExporter.builder()
                .setEndpoint("http://localhost:55681/v1/traces") //55681
                
                .build();
    }
    

    private SdkTracerProvider createTracerProvider() {
        Resource resource = Resource.getDefault().merge(
                Resource.create(Attributes.builder().put(AttributeKey.stringKey("service.name"), "kieker-data").build()));

        return SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(ZipkinSpanExporter.builder().setEndpoint("http://localhost:9411/api/v2/spans").build()).build())
                .build();
    }

    @Override
    protected void execute(IMonitoringRecord record) throws Exception {
        if (record instanceof OperationExecutionRecord) {
            OperationExecutionRecord oer = (OperationExecutionRecord) record;
            System.out.println("OER: " + oer);

            Instant startTime = Instant.ofEpochMilli(oer.getTin());
            Span span = tracer.spanBuilder(oer.getOperationSignature())
            		.setStartTimestamp(startTime)
                    .startSpan();

            try (Scope scope = span.makeCurrent()) {
                span.setAttribute("customAttribute", "5");
               
            } finally {
            	Instant endTime = Instant.ofEpochMilli(oer.getTout());
                span.end(endTime);
            }
        }
    }
}

