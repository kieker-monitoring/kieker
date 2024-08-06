package kieker.tools.oteltransformer;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

import java.nio.file.Path;

public class Settings {
	@Parameter(names = { "-lp",
		"--listenPort" }, required = true, description = "Port where the otel-transformer listens for traces")
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
