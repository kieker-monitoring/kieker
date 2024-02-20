package kieker.monitoring.probe.javassist;

import java.io.File;
import java.io.IOException;

public class BuildTimeAdaptionUtil {
	public static void extractJar(File instrumentJar, File tempDir) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(new String[] {"jar", "xf", instrumentJar.getAbsolutePath() });
		builder.directory(tempDir);
		builder.inheritIO();
		builder.start();
	}
	
	public static void createJar(File instrumentJar, File tempDir) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(new String[] {"jar", "cf",
				instrumentJar.getAbsolutePath(),
				"." });
		builder.directory(tempDir);
		
		builder.inheritIO();
		
		Process start = builder.start();
		
		try {
			start.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
