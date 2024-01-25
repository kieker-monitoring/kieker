package kieker.tools.log.replayer.stages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZipkinServerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSendStage.class);

	private static final String jarPath = "src/test/resources/zipkin-server-3.0.4-exec.jar";
	
	private Process process;
	
    @BeforeEach
    public void startZipkinServer() throws IOException {
    	LOGGER.info("Starting zipking");
        
        String command = String.format("java -jar %s", jarPath);

        try {
        	LOGGER.info("Command: " + command);
            process = Runtime.getRuntime().exec(command);

            waitForZipkinStartup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void waitForZipkinStartup() throws IOException {
		// capture and print the process output
		 InputStream inputStream = process.getInputStream();
		 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		 String line;
		 while ((line = bufferedReader.readLine()) != null) {
			 LOGGER.info(line);
			 if (line.contains("Serving HTTP at")) {
				 LOGGER.info("Startup finished");
				 return;
			 }
		}
	}

    @Test
    public void Test() {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    @AfterEach
    public void stopZipkinServer() {
    	process.destroyForcibly();
    }
}
