package kieker.tools.oteltransformer.receiver;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

public class OtlpReceiverSettings {
	@Parameter(names = { "-lp",
		"--listenPort" }, required = true, description = "Port where the otel-transformer receives OpenTelemetry traces")
	private int listenPort;

	@Parameter(names = { "-c",
		"--configuration" }, required = false, description = "Configuration file.", converter = PathConverter.class)
	private Path configurationPath;

	public int getListenPort() {
		return listenPort;
	}

	public Path getKiekerMonitoringProperties() {
		return this.configurationPath;
	}
}
