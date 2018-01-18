package kieker.common.util.filesystem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import kieker.test.common.junit.AbstractDynamicKiekerTest;

public class FSUtilTest {

	@Rule
	public final TemporaryFolder tempFolder = new TemporaryFolder();

	private Path startDirectory;

	@Before
	public void setUp() throws Exception {
		this.startDirectory = this.tempFolder.newFolder(this.getClass().getName()).toPath();

		final List<String> resourceFileNames = Arrays.asList("module-info.testjava", "package-info.testjava",
				"RegexTestSubject.testjava");

		// Eclipse also compiles java files in resource folders.
		// Hence, we store java files in resource folders with the extension "javatest"
		// and rename their extension to "java" in a temp folder.
		final ClassLoader classLoader = getClass().getClassLoader();
		for (String resourceFileName : resourceFileNames) {
			URL resourceFileURL = classLoader.getResource(resourceFileName);
			Path sourceFilePath = Paths.get(resourceFileURL.toURI());
			String targetFileName = sourceFilePath.getFileName().toString().replaceFirst("testjava", "java");
			Files.copy(sourceFilePath, this.startDirectory.resolve(targetFileName));
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void listFilesRecursivelyShouldDetectAllJavaFiles() throws Exception {
		List<File> files = FSUtil.listFilesRecursively(this.startDirectory, ".*java");

		assertThat(files.get(0).getName(), is("module-info.java"));
		assertThat(files.get(1).getName(), is("package-info.java"));
		assertThat(files.get(2).getName(), is("RegexTestSubject.java"));
		assertThat(files.size(), is(3));
	}

	@Test
	public void listFilesRecursivelyShouldDetectJavaClassesOnly() throws Exception {
		List<File> files = FSUtil.listFilesRecursively(this.startDirectory,
				AbstractDynamicKiekerTest.REGEX_PATTERN_JAVA_SOURCE_FILES);

		assertThat(files.get(0).getName(), is("RegexTestSubject.java"));
		assertThat(files.size(), is(1));
	}
}
