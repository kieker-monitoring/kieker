/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.common.util.filesystem;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.test.common.junit.AbstractDynamicKiekerTest;

/**
 * @author Christian Wulf
 *
 * @since 1.14
 */
public class FSUtilTest {

	@Rule
	public final TemporaryFolder tempFolder = new TemporaryFolder();

	private Path startDirectory;

	public FSUtilTest() {
		// empty constructor
	}

	@Before
	public void setUp() throws Exception {
		this.startDirectory = this.tempFolder.newFolder(this.getClass().getName()).toPath();

		final List<String> resourceFileNames = Arrays.asList("module-info.testjava", "package-info.testjava",
				"RegexTestSubject.testjava");

		// Eclipse also compiles java files in resource folders.
		// Hence, we store java files in resource folders with the extension "javatest"
		// and rename their extension to "java" in a temp folder.
		final ClassLoader classLoader = this.getClass().getClassLoader();
		for (final String resourceFileName : resourceFileNames) {
			final URL resourceFileURL = classLoader.getResource(resourceFileName);
			final Path sourceFilePath = Paths.get(resourceFileURL.toURI());
			final Path targetFileName = sourceFilePath.getFileName();
			if (targetFileName != null) {
				final String targetFileNameString = targetFileName.toString().replaceFirst("testjava", "java");
				Files.copy(sourceFilePath, this.startDirectory.resolve(targetFileNameString));
			}
		}
	}

	@Ignore // NOCS (test not fully implemented)
	@Test
	public void listFilesRecursivelyShouldDetectAllJavaFiles() throws Exception { // NOPMD NOCS (test not fully implemented)
		final List<File> files = FSUtil.listFilesRecursively(this.startDirectory, ".*java");

		MatcherAssert.assertThat(files.get(0).getName(), Matchers.is("module-info.java"));
		MatcherAssert.assertThat(files.get(1).getName(), Matchers.is("package-info.java"));
		MatcherAssert.assertThat(files.get(2).getName(), Matchers.is("RegexTestSubject.java"));
		MatcherAssert.assertThat(files.size(), Matchers.is(3));
	}

	@Test
	public void listFilesRecursivelyShouldDetectJavaClassesOnly() throws Exception { // NOPMD (uses MatcherAssert)
		final List<File> files = FSUtil.listFilesRecursively(this.startDirectory, AbstractDynamicKiekerTest.REGEX_PATTERN_JAVA_SOURCE_FILES);

		MatcherAssert.assertThat(files.get(0).getName(), Matchers.is("RegexTestSubject.java"));
		MatcherAssert.assertThat(files.size(), Matchers.is(1));
	}
}
