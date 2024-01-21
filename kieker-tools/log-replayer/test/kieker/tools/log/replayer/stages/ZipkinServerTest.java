package kieker.tools.log.replayer.stages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZipkinServerTest {

    @BeforeEach
    public void startZipkinServer() throws IOException {
        String jarPath = "kieker-tools/test-resources/kieker.monitoring.probe.aspectj.flow.operationExecution/zipkin-server-3.0.4-exec.jar";
        String command = String.format("java -jar %s", jarPath);

        try {
            Process process = Runtime.getRuntime().exec(command);

            // capture and print the process output
             InputStream inputStream = process.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
             String line;
             while ((line = bufferedReader.readLine()) != null) {
                 System.out.println(line);
            }

            // Wait for a moment to allow Zipkin to start 
            Thread.sleep(5000);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Test() {
        //test method 
    }
}
