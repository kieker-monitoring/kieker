package kieker.tools.oteltransformer.receiver;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;

import kieker.common.record.controlflow.OperationExecutionRecord;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse;
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc;
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc.AsyncService;
import io.opentelemetry.proto.common.v1.KeyValue;
import io.opentelemetry.proto.trace.v1.ResourceSpans;
import io.opentelemetry.proto.trace.v1.ScopeSpans;
import io.opentelemetry.proto.trace.v1.Span;
import teetime.framework.AbstractProducerStage;

public class OtlpGrpcReceiverStage extends AbstractProducerStage<OperationExecutionRecord> implements io.grpc.BindableService, AsyncService {

	@java.lang.Override
	public final io.grpc.ServerServiceDefinition bindService() {
		return TraceServiceGrpc.bindService(this);
	}
	
	private final int port;
	
	public OtlpGrpcReceiverStage(int port) {
		this.port = port;
	}


	@Override
	public void export(ExportTraceServiceRequest request, StreamObserver<ExportTraceServiceResponse> responseObserver) {
		List<ResourceSpans> resourceSpansList = request.getResourceSpansList();
		for (ResourceSpans rs : resourceSpansList) {
			for (ScopeSpans ss : rs.getScopeSpansList()) {
				for (Span span : ss.getSpansList()) {
					convert(span);
//					System.out.println(record);
				}
			}
		}
		responseObserver.onNext(ExportTraceServiceResponse.getDefaultInstance());
		responseObserver.onCompleted();
	}

	private final Map<String, Integer> threadLocalEoi = new HashMap<>();
	private final Map<String, Integer> threadLocalEss = new HashMap<>();
	
	private final UnprocessedSpanHandler spanHandler = new UnprocessedSpanHandler();

	public void convert(Span span) {
		ByteString traceIdBytes = span.getTraceId();
		final String traceIdHex = BaseEncoding.base16().lowerCase().encode(traceIdBytes.toByteArray());
		final String spanId = BaseEncoding.base16().lowerCase().encode(span.getSpanId().toByteArray());
		final String parentSpanId = BaseEncoding.base16().lowerCase().encode(span.getParentSpanId().toByteArray());

		final int eoi;
		final int ess;

		if (parentSpanId == null || parentSpanId.isEmpty() || parentSpanId.equals("0000000000000000")) {
			// root span
			eoi = 0;
			ess = 0;
		} else {
			if (threadLocalEoi.get(traceIdHex) == null || threadLocalEss.get(parentSpanId) == null) {
				spanHandler.addUnprocessedSpan(span);
				return;
			}
			
			int parentEoi = threadLocalEoi.getOrDefault(traceIdHex, -1);
			int parentEss = threadLocalEss.getOrDefault(parentSpanId, -1);

			ess = parentEss + 1;
			eoi = parentEoi + 1;
			
			threadLocalEoi.put(traceIdHex, eoi);
		}

		threadLocalEoi.put(traceIdHex, eoi);
		threadLocalEss.put(spanId, ess);
		
		final String sessionId = traceIdHex;
		final String operationSignature = span.getName();
		final String hostname = getHostname(span);

		final long tin = toUnixNanos(span.getStartTimeUnixNano());
		final long tout = toUnixNanos(span.getEndTimeUnixNano());
		
		long traceIdAsLong = traceIdAsLong(traceIdHex);
		OperationExecutionRecord operationExecutionRecord = new OperationExecutionRecord(operationSignature, sessionId, traceIdAsLong, tin, tout, hostname, eoi, ess);
		getOutputPort().send(operationExecutionRecord);
		
		convertMissingSpans(spanId);
	}

	private String getHostname(Span span) {
		String hostname = "localhost";
		String peer = null;
		for (KeyValue key : span.getAttributesList()) {
			System.out.println(key + " -- " + key.getValue());
			if (key.getKey().equals("rpc.service")) {
				hostname = key.getValue().getStringValue();
			}
			if (key.getKey().equals("net.peer.name")) {
				peer = key.getValue().getStringValue();
			}
			if (key.getKey().equals("net.sock.peer.addr")) {
				hostname = key.getValue().getStringValue();
			}
			if (key.getKey().equals("peer.address")) {
				hostname = key.getValue().getStringValue();
			}
		}
		if (peer != null) {
			hostname = hostname + "-" + peer;
		}
		return hostname;
	}


	private void convertMissingSpans(final String spanId) {
		List<Span> unprocessedSpans = spanHandler.getUnprocessedSpans(spanId);
		if (unprocessedSpans != null) {
			System.out.println("Handling unprocessed: " + spanId + " " + unprocessedSpans.size());
			for (Span child : unprocessedSpans) {
				convert(child);
			}
		} else {
			System.out.println("No unprocessed spans for " + spanId);
		}
	}

	private long traceIdAsLong(String traceIdHex) {
		return new BigInteger(traceIdHex.substring(16), 16).longValue();
	}

	private long toUnixNanos(long nanosSinceEpoch) {
		return nanosSinceEpoch;
	}

	@Override
	protected void execute() throws Exception {
		startServer();
	}
	
	
	public void startServer() {
		Server server = ServerBuilder
				.forPort(port)
				.addService(this)
				.build();

		try {
			server.start();
			System.out.println("OTLP gRPC Receiver läuft auf Port " + port);
			server.awaitTermination();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
