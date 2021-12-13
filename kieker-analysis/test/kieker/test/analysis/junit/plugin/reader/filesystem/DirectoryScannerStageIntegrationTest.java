/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.analysis.junit.plugin.reader.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.source.file.DirectoryScannerStage;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.framework.test.StageTester;
import teetime.stage.CollectorSink;

/**
 * Test the directory scanner.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DirectoryScannerStageIntegrationTest {

	private static final String KIEKER_MAP_NAME = "kieker.map";
	private static final String KIEKER_LOG_NAME = "kieker-xy.dat";

	private List<File> directories;
	private final List<File> results = new ArrayList<>();

	/** Create test. */
	public DirectoryScannerStageIntegrationTest() {
		// default constructor
	}

	/**
	 * @throws java.io.IOException
	 *             on errors
	 */
	@Before
	public void setUp() throws IOException {
		final File rootTempDirHandle = new File(System.getProperty("java.io.tmpdir"));
		final Path treeA = Files.createTempDirectory(rootTempDirHandle.toPath(), "tree-A");
		final Path treeB = Files.createTempDirectory(rootTempDirHandle.toPath(), "tree-B");

		this.directories = new ArrayList<>();
		this.directories.add(treeA.toFile());
		this.directories.add(treeB.toFile());

		// tree A
		this.createKiekerDirectory(treeA.resolve("sub1"));
		this.createKiekerDirectory(treeA.resolve("sub2"));
		// tree B
		this.createKiekerDirectory(treeB);
	}

	private void createKiekerDirectory(final Path path) throws IOException {
		if (path.toFile().mkdirs()) {
			if (!path.resolve(KIEKER_MAP_NAME).toFile().createNewFile()
					|| !path.resolve(KIEKER_LOG_NAME).toFile().createNewFile()) {
				Assert.fail("Cannot create file system.");
			}

			this.results.add(path.toFile());
		}
	}

	/**
	 * @throws java.io.IOException
	 *             on errors
	 */
	@After
	public void tearDown() throws IOException {
		for (final File directory : this.directories) {
			this.deleteRecursively(directory.toPath());
		}
	}

	private void deleteRecursively(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});

	}

	/**
	 * Check whether the scanner produces nothing and terminates.
	 */
	@Test
	public void testEmptyArrayOfDirectories() {
		final DirectoryScannerStage producer = new DirectoryScannerStage((List<File>) null);
		StageTester.test(producer).start();

		Assert.assertThat(producer.getOutputPort(), StageTester.producesNothing());
	}

	/**
	 * Check whether the scanner produces a File handle for each directory. There should be three
	 * directories. However, the sequence can vary depending on the operating system and file system
	 * properties. therefore, we use our own matcher.
	 */
	@Test
	public void testPopulatedArrayOfDirectories() {
		final DirectoryScannerStage producer = new DirectoryScannerStage(this.directories);

		StageTester.test(producer).start();

		Assert.assertThat(producer.getOutputPort(), new RandomContentMatcher(this.results));
	}

	/**
	 *
	 * @author Reiner Jung
	 *
	 * @since 1.15
	 */
	private static class RandomContentMatcher extends BaseMatcher<OutputPort<File>> {

		private final List<File> remainingResults;
		private final List<File> results;

		public RandomContentMatcher(final List<File> results) {
			this.remainingResults = new ArrayList<>(results);
			this.results = results;
		}

		@Override
		public boolean matches(final Object item) {
			if (item instanceof OutputPort) {
				@SuppressWarnings("unchecked")
				final CollectorSink<File> collectorSink = this.getSinkFromOutputPort((OutputPort<File>) item);
				if (collectorSink.getElements().size() != this.results.size()) {
					return false;
				}
				for (final File element : collectorSink.getElements()) {
					if (!this.checkElement(element)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}

		private boolean checkElement(final File element) {
			for (final File result : this.remainingResults) {
				if (result.equals(element)) {
					this.remainingResults.remove(result);
					return true;
				}
			}
			return false;
		}

		@Override
		public void describeTo(final Description description) {
			// do we need to do something here?
		}

		@SuppressWarnings("unchecked")
		private <T> CollectorSink<T> getSinkFromOutputPort(final OutputPort<T> outputPort) {
			final InputPort<?> targetPort = outputPort.getPipe().getTargetPort();
			final AbstractStage owningStage = targetPort.getOwningStage();
			if (owningStage instanceof CollectorSink) {
				return (CollectorSink<T>) owningStage;
			}

			final String message = String.format("%s", owningStage);
			throw new IllegalArgumentException(message);
		}

	}
}
