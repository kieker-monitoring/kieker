package kieker.tools.log.replayer.stages;


import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporterBuilder;
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
    	 
    	OtlpHttpSpanExporter spanExporter = createSpanExporter();
    	
    	
    	BatchSpanProcessor spanProcessor = BatchSpanProcessor.builder(spanExporter).build();
    	
        SdkTracerProvider tracerProvider = createTracerProvider();

        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();

        this.tracer = openTelemetry.getTracer("io.opentelemetry.example.Example");
    }
    
    private OtlpHttpSpanExporter createSpanExporter() {
        return OtlpHttpSpanExporter.builder()
                .setEndpoint("http://localhost:55681S/v1/traces") //55681
                
                .build();
    }
    

    private SdkTracerProvider createTracerProvider() {
        Resource resource = Resource.getDefault().merge(
                Resource.create(Attributes.builder().put(AttributeKey.stringKey("service.name"), "my-service-name").build()));

        return SdkTracerProvider.builder()
                .setResource(resource)
                .build();
    }

    @Override
    protected void execute(IMonitoringRecord record) throws Exception {
        System.out.println("Record: " + record);

        if (record instanceof OperationExecutionRecord) {
            OperationExecutionRecord oer = (OperationExecutionRecord) record;
            System.out.println("OER: " + oer);

            Span span = tracer.spanBuilder(oer.getOperationSignature())
                    .startSpan();

            try (Scope scope = span.makeCurrent()) {
                span.setAttribute("customAttribute", "attributeValue");
               
            } finally {
                span.end();
            }
        }
    }
}

