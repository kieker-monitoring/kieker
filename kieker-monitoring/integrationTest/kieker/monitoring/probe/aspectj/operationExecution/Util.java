package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public enum Util {
	;

	private static final File EXAMPLE_PROJECT_FOLDER = new File("test-resources/example-projects-aspectj");

	public static File runTestcase(final String projectName, final String testcase) throws IOException {
		File folder = new File(EXAMPLE_PROJECT_FOLDER, projectName);
		File logFolder = new File(folder, "monitoring-logs");
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		} else {
			FileUtils.cleanDirectory(logFolder);
		}

		ProcessBuilder processBuilder = new ProcessBuilder("gradle", "clean", "test", "--tests", testcase);
		processBuilder.directory(folder);
		StreamGobbler.showFullProcess(processBuilder.start());
		return logFolder;
	}

	public static List<String> getLatestLogRecord(final File monitoringFolder) throws IOException {
		File dataFolder = monitoringFolder.listFiles()[0];
		File dataFile = dataFolder.listFiles((FilenameFilter) new WildcardFileFilter("*.dat"))[0];
		List<String> lines = FileUtils.readLines(dataFile, "utf-8");
		return lines;
	}
}
