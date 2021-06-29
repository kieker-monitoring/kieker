package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public enum Util {
	;

	public static final File EXAMPLE_PROJECT_FOLDER = new File("test-resources/example-projects-aspectj");

	/**
	 * Creates a temporary project folder, containing the example project
	 * @param aopXML
	 * @return
	 * @throws IOException 
	 */
	public static File createTemporaryProject(final File aopXML) throws IOException {
//		File temporaryFile = Files.createTempDirectory("kieker-test-").toFile();
		File temporaryFile = new File("build/kieker-test-aspectj/example-project");
		if (!temporaryFile.exists()) {
			temporaryFile.mkdirs();
		} else {
			FileUtils.cleanDirectory(temporaryFile);
		}
		FileUtils.copyDirectory(new File(EXAMPLE_PROJECT_FOLDER, "example-pure"), temporaryFile);
		File aopXMLFile = new File(temporaryFile, "src/test/resources/META-INF/aop.xml");
		FileUtils.copyFile(aopXML, aopXMLFile);
		return temporaryFile;
	}
	
	public static File runTestcase(final File projectFolder, final String testcase) throws IOException {
		File logFolder = createLogFolder(projectFolder);
		
		callTest(testcase, projectFolder);
		
		return logFolder;
	}

	private static File createLogFolder(final File projectFolder) throws IOException {
		File logFolder = new File(projectFolder, "monitoring-logs");
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		} else {
			FileUtils.cleanDirectory(logFolder);
		}
		return logFolder;
	}
	
	public static File runTestcase(final String projectName, final String testcase) throws IOException {
		File folder = new File(EXAMPLE_PROJECT_FOLDER, projectName);
		File logFolder = createLogFolder(folder);

		callTest(testcase, folder);
		return logFolder;
	}

	private static void callTest(final String testcase, final File folder) throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder("gradle", "clean", "test", "--tests", testcase);
		processBuilder.directory(folder);
		StreamGobbler.showFullProcess(processBuilder.start());
	}

	public static List<String> getLatestLogRecord(final File monitoringFolder) throws IOException {
		File dataFolder = monitoringFolder.listFiles()[0];
		File dataFile = dataFolder.listFiles((FilenameFilter) new WildcardFileFilter("*.dat"))[0];
		List<String> lines = FileUtils.readLines(dataFile, "utf-8");
		return lines;
	}
}
