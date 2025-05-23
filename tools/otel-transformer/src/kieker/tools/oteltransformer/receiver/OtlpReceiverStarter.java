package kieker.tools.oteltransformer.receiver;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class OtlpReceiverStarter {
	public static void main(String[] args) throws IOException, InterruptedException {
		startServer(9000);
	}
	
	public static void startServer(int port) throws IOException, InterruptedException {
		Server server = ServerBuilder
				.forPort(port)
				.addService(new OtlpGrpcReceiver())
				.build();

		server.start();
		System.out.println("OTLP gRPC Receiver läuft auf Port 9000");
		server.awaitTermination();
	}
}
