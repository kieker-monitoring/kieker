package kieker.tools.oteltransformer.receiver;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;

import kieker.common.record.controlflow.OperationExecutionRecord;

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
					OperationExecutionRecord record = convert(span);
					System.out.println(record);
				}
			}
		}
		responseObserver.onNext(ExportTraceServiceResponse.getDefaultInstance());
		responseObserver.onCompleted();
	}
	
	private final Map<String, Integer> threadLocalEoi = new HashMap<>();
    private final Map<String, Integer> threadLocalEss = new HashMap<>();
	
	public OperationExecutionRecord convert(Span span) {
		ByteString traceIdBytes = span.getTraceId();
		String traceIdHex = BaseEncoding.base16().lowerCase().encode(traceIdBytes.toByteArray());
        final String spanId = BaseEncoding.base16().lowerCase().encode(span.getSpanId().toByteArray());
        final String parentSpanId = BaseEncoding.base16().lowerCase().encode(span.getParentSpanId().toByteArray());

        final String sessionId = traceIdHex;
        final String operationSignature = span.getName();
        final String hostname = "localhost";

        final long tin = toUnixNanos(span.getStartTimeUnixNano());
        final long tout = toUnixNanos(span.getEndTimeUnixNano());

        int eoi;
        int ess;

        if (parentSpanId == null || parentSpanId.isEmpty() || parentSpanId.equals("0000000000000000")) {
            // root span
            eoi = 0;
            ess = 0;
        } else {
            int parentEoi = threadLocalEoi.getOrDefault(parentSpanId, -1);
            int parentEss = threadLocalEss.getOrDefault(parentSpanId, -1);

            ess = parentEss + 1;
            eoi = parentEoi + 1;
        }

        threadLocalEoi.put(spanId, eoi);
        threadLocalEss.put(spanId, ess);

        System.out.println(traceIdHex);
        long traceIdAsLong = traceIdAsLong(traceIdHex);
		return new OperationExecutionRecord(operationSignature, sessionId, traceIdAsLong, tin, tout, hostname, eoi, ess);
    }

    private long traceIdAsLong(String traceIdHex) {
        return new BigInteger(traceIdHex.substring(16), 16).longValue();
    }

    private long toUnixNanos(long nanosSinceEpoch) {
        return nanosSinceEpoch;
    }
}
