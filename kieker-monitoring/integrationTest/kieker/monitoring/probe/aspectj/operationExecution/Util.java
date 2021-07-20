package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * Helper class for aspect integration tests
 * 
 * @author DaGeRe
 *
 */
public enum Util {
	;

	public static final File EXAMPLE_PROJECT_FOLDER = new File("test-resources/example-projects-aspectj");

	/**
	 * Creates a temporary project folder, containing the example project (for all
	 * tests with standard traces)
	 * 
	 * @param aopXML
	 * @return
	 * @throws IOException
	 */
	public static File createTemporaryProject(final File aopXML) throws IOException {
		final File temporaryFile = new File("build/kieker-test-aspectj/example-project");
		if (!temporaryFile.exists()) {
			temporaryFile.mkdirs();
		} else {
			FileUtils.cleanDirectory(temporaryFile);
		}
		FileUtils.copyDirectory(new File(EXAMPLE_PROJECT_FOLDER, "example-pure"), temporaryFile);
		final File aopXMLFile = new File(temporaryFile, "src/test/resources/META-INF/aop.xml");
		FileUtils.copyFile(aopXML, aopXMLFile);
		return temporaryFile;
	}

	/**
	 * Runs a test case in a generated project
	 * 
	 * @param projectFolder
	 * @param testcase
	 * @return
	 * @throws IOException
	 */
	public static File runTestcase(final File projectFolder, final String testcase) throws IOException {
		final File logFolder = createLogFolder(projectFolder);

		callTest(testcase, projectFolder);

		return logFolder;
	}

	private static File createLogFolder(final File projectFolder) throws IOException {
		final File logFolder = new File(projectFolder, "monitoring-logs");
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		} else {
			FileUtils.cleanDirectory(logFolder);
		}
		return logFolder;
	}

	/**
	 * Runs a test case in an individual project
	 * 
	 * @param projectName
	 * @param testcase
	 * @return
	 * @throws IOException
	 */
	public static File runTestcase(final String projectName, final String testcase) throws IOException {
		final File folder = new File(EXAMPLE_PROJECT_FOLDER, projectName);
		final File logFolder = createLogFolder(folder);

		callTest(testcase, folder);
		return logFolder;
	}

	private static void callTest(final String testcase, final File folder) throws IOException {
		final ProcessBuilder processBuilder = new ProcessBuilder("gradle", "clean", "test", "--tests", testcase);
		processBuilder.directory(folder);
		StreamGobbler.showFullProcess(processBuilder.start());
	}

	public static List<String> getLatestLogRecord(final File monitoringFolder) throws IOException {
		final File dataFolder = monitoringFolder.listFiles()[0];
		final File dataFile = dataFolder.listFiles((FilenameFilter) new WildcardFileFilter("*.dat"))[0];
		return FileUtils.readLines(dataFile, "utf-8");
	}
}
