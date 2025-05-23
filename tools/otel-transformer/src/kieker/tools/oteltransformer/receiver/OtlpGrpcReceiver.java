package kieker.tools.oteltransformer.receiver;

import java.util.List;

import io.grpc.stub.StreamObserver;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse;
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc;
import io.opentelemetry.proto.trace.v1.ResourceSpans;
import io.opentelemetry.proto.trace.v1.ScopeSpans;
import io.opentelemetry.proto.trace.v1.Span;

public class OtlpGrpcReceiver extends TraceServiceGrpc.TraceServiceImplBase {

	@Override
	public void export(ExportTraceServiceRequest request, StreamObserver<ExportTraceServiceResponse> responseObserver) {
		List<ResourceSpans> resourceSpansList = request.getResourceSpansList();
		for (ResourceSpans rs : resourceSpansList) {
			for (ScopeSpans ss : rs.getScopeSpansList()) {
				for (Span span : ss.getSpansList()) {
					System.out.println("Received span: " + span.getName());
					// Convert to your internal representation if needed
				}
			}
		}
		responseObserver.onNext(ExportTraceServiceResponse.getDefaultInstance());
		responseObserver.onCompleted();
	}
}
