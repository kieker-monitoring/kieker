/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.common.junit.util.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * FSUtils used in tests.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public final class FSUtil4Tests {

	private FSUtil4Tests() {
		// private default constructor, is factory
	}

	/**
	 * find all files in a directory tree.
	 *
	 * @param startDirectory
	 *            root directory for the search
	 * @param postfixRegexNamePattern
	 *            to be used for the matching
	 * @return all matching files within the given <code>startDirectory</code> and all its subdirectories
	 */
	public static List<File> listFilesRecursively(final Path startDirectory, final String postfixRegexNamePattern) {
		final RecursiveFileVisitor visitor = new RecursiveFileVisitor(postfixRegexNamePattern);
		try {
			Files.walkFileTree(startDirectory, visitor);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
		return visitor.getFiles();
	}

	/**
	 * @author Christian Wulf
	 * @since 1.13
	 */
	private static class RecursiveFileVisitor extends SimpleFileVisitor<Path> {
		private final Pattern pattern;
		private final List<File> files;

		public RecursiveFileVisitor(final String postfixRegexNamePattern) {
			this.pattern = Pattern.compile(postfixRegexNamePattern);
			this.files = new ArrayList<>();
		}

		@Override
		public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
			final Path fileName = file.getFileName(); // Nullable
			if (fileName != null) {
				final String fileNameString = fileName.toString();
				if (this.pattern.matcher(fileNameString).matches()) {
					this.files.add(file.toFile());
				}
			}
			return FileVisitResult.CONTINUE;
		}

		public List<File> getFiles() {
			return this.files;
		}
	}
}
