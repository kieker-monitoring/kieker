package kieker.tools.log.replayer.stages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.log.replayer.ReplayerMain;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ZipkinServerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSendStage.class);

	private static final String jarPath = "src/test/resources/zipkin-server-3.0.4-exec.jar";
	
	private static final String kiekerDataPath = "src/test/resources/kieker results/kieker-results";
	
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
    public void Test() throws IOException {
    	try {
    		
    		ReplayerMain replayerMain = new ReplayerMain();
    		
    		//Get list of file in keieker-data directory
            File[] kiekerDataFiles = Objects.requireNonNull(new java.io.File(kiekerDataPath).listFiles());

            for (File kiekerDataFile : kiekerDataFiles) {
            	
            	 // Replay each Kieker data file
                replayerMain.run("Replayer", "replayer", new String[]{"--delay", "1", "-i", kiekerDataFile.getAbsolutePath()});

                Thread.sleep(1000);
			}
            
    		//Check Zipkin API for spans
            boolean spansCreated = checkZipkinForSpans();
            assertTrue(spansCreated, "Spans should be created in Zipkin");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private boolean checkZipkinForSpans() throws IOException {
    	
        // Zipkin API to check if spans were created
        URL url = new URL("http://localhost:9411/api/v2/spans");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK;
    }

    
    @AfterEach
    public void stopZipkinServer() {
    	process.destroyForcibly();
    }
}
