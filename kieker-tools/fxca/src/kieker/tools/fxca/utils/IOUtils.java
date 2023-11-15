/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.fxca.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Henning Schnoor
 *
 * @since 2.0.0
 */
public final class IOUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

	public final static Predicate<Path> IS_DIRECTORY = element -> Files.isDirectory(element);

	private IOUtils() {
		// utility class
	}

	public static PrintStream printStreamPrefixWrapper(final PrintStream printWithPrefix,
			final PrintStream printWithoutPrefix, final String prefix) {
		return new PrintStream(printWithoutPrefix, false) {
			@Override
			public void println(final String x) {
				printWithPrefix.println(prefix + x);
				printWithoutPrefix.println(x);
			}
		};
	}

	public static PrintStream printToFileAnd(final PrintStream out, final Path filename) throws FileNotFoundException {
		return IOUtils.printStreamPrefixWrapper(out, new PrintStream(filename.toFile()),
				"[" + filename.toString() + "] ");
	}

	public static PrintStream printToFileAnd(final PrintStream out, final String filename)
			throws FileNotFoundException {
		return IOUtils.printStreamPrefixWrapper(out, new PrintStream(filename), "[" + filename + "] ");
	}

	public static void createDirectory(final Path directoryPath) {
		directoryPath.toFile().mkdirs();
	}

	public static Predicate<Path> isDirectoryWithTheseFiles(final Iterable<String> filenames) {
		Predicate<Path> result = IOUtils.IS_DIRECTORY;
		for (final String filename : filenames) {
			result = result.and(directory -> Files.exists(directory.resolve(Paths.get(filename))));
		}
		return result;
	}

	public static Predicate<Path> endsWith(final String suffix) {
		return element -> element.toAbsolutePath().toString().endsWith(suffix);
	}

	public static List<Path> pathsInDirectory(final Path directory) throws IOException {
		return IOUtils.pathsInDirectory(directory, o -> true, o -> true, false);
	}

	public static List<Path> pathsInDirectory(final Path directory, final Predicate<Path> fileFilter)
			throws IOException {
		return IOUtils.pathsInDirectory(directory, fileFilter, null);
	}

	private static List<Path> pathsInDirectory(final Path directory, final Predicate<Path> fileFilter,
			final Collection<Path> collectNotMatchingFiles) throws IOException {
		return IOUtils.pathsInDirectory(directory, fileFilter, o -> true, false, collectNotMatchingFiles);
	}

	public static List<Path> pathsInDirectory(final Path directory, final Predicate<Path> fileFilter,
			final Predicate<Path> directoryFilter, final boolean addEntriesForDirectories) throws IOException {
		return IOUtils.pathsInDirectory(directory, fileFilter, directoryFilter, addEntriesForDirectories, null);
	}

	private static List<Path> pathsInDirectory(final Path directory, final Predicate<Path> fileFilter,
			final Predicate<Path> directoryFilter, final boolean addEntriesForDirectories,
			final Collection<Path> collectNotMatchingFiles) throws IOException {

		final List<Path> result = ListUtils.ofM();

		final SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<>() {

			@Override
			public FileVisitResult visitFile(final Path filePath, final BasicFileAttributes attrs) {
				if (!fileFilter.test(filePath)) {
					if (collectNotMatchingFiles != null) {
						collectNotMatchingFiles.add(filePath);
					}
					return FileVisitResult.CONTINUE;
				}
				if (!Files.isDirectory(filePath)) {
					result.add(filePath);
					IOUtils.LOGGER.debug(filePath.toString());
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path filePath, final IOException exc) {
				IOUtils.LOGGER.warn("could not visit {}: ", filePath.toString(), exc.getClass()); // NOPMD
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
				if (!directoryFilter.test(dir) && !dir.equals(directory)) { // no matter what the
																			// filter, we should
																			// visit the original
																			// directory.
					return FileVisitResult.SKIP_SUBTREE;
				}

				if (addEntriesForDirectories && fileFilter.test(dir)) {
					result.add(dir);
				}

				try {
					return super.preVisitDirectory(dir, attrs);
				} catch (final IOException e) {
					IOUtils.LOGGER.warn("skipping subdir {} due to I/O exception: {}", dir, e.getLocalizedMessage()); // NOPMD
					return FileVisitResult.SKIP_SUBTREE;
				}
			}
		};

		Files.walkFileTree(directory, visitor);
		return result;
	}

	public static void printWithCommas(final Iterable<String> items) {

		final StringBuilder result = new StringBuilder();

		for (final String item : items) {
			if (result.length() != 0) {
				result.append(", ");
			}
			result.append(item);
		}

		IOUtils.LOGGER.debug(result.toString());
	}
}
