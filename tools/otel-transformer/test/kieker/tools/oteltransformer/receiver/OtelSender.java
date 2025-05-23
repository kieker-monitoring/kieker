
package kieker.tools.oteltransformer.receiver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

public class OtelSender {

	private static final int OTEL_PORT = 9000;

	private static final Logger LOG = Logger.getLogger(OtelSender.class.getName());

	@Before
	public void startServer() {
		Thread serverBackgroundThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					OtlpReceiverStarter.startServer(OTEL_PORT);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		serverBackgroundThread.start();
		
		try {
			// Should wait some time until server is up
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSpanSending() {
		OtlpGrpcSpanExporter exporter = OtlpGrpcSpanExporter.builder()
				.setEndpoint("http://localhost:" + OTEL_PORT)
				.build();

		SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(BatchSpanProcessor.builder(exporter)
						.setScheduleDelay(100, TimeUnit.MILLISECONDS)
						.build())
				.build();

		OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
				.setTracerProvider(tracerProvider)
				.build();

		GlobalOpenTelemetry.set(openTelemetry);

		// Generate test spans
		Tracer tracer = GlobalOpenTelemetry.getTracer("abcd");
		
		for (int i = 0; i < 5; i++) {
			LOG.info("Sending " + i);

			SpanBuilder spanBuilder = tracer.spanBuilder("parent" + i);
			Span span = spanBuilder.startSpan();

			SpanBuilder spanBuilder2 = tracer.spanBuilder("child" + i);
			spanBuilder2.setParent(Context.current().with(span));
			Span span2 = spanBuilder2.startSpan();
			span2.end();

			span.end();
			
			LOG.info("TraceId für child" + i + ": " + span2.getSpanContext().getTraceId());
		}

		System.out.println("Waiting");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Waiting finished");
	}

}
