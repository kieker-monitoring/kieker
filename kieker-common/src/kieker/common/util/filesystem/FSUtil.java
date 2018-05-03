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
 * @author Jan Waller
 *
 * @since 1.7
 */
public final class FSUtil { // NOCS NOPMD (constants interface)

	/** The prefix of Kieker's record files. */
	public static final String FILE_PREFIX = "kieker";
	/** The name of Kieker's map files. */
	public static final String MAP_FILENAME = "kieker.map";

	/** The old prefix of Kieker's record files. */
	public static final String LEGACY_FILE_PREFIX = "tpmon";
	/** The old name of Kieker's map files. */
	public static final String LEGACY_MAP_FILENAME = "tpmon.map";

	/** The usual extension of Kieker's record files. */
	public static final String DAT_FILE_EXTENSION = ".dat";
	/** The extension of Kieker's zipped record files. */
	public static final String ZIP_FILE_EXTENSION = ".zip";
	/** The extension of Kieker's gzipped record files. */
	public static final String GZIP_FILE_EXTENSION = ".gz";
	/** The extension of Kieker's xz record files. */
	public static final String XZ_FILE_EXTENSION = ".xz";
	/** The extension of Kieker's binary record files. */
	public static final String BINARY_FILE_EXTENSION = ".bin";
	/** The extension of Kieker's mapping files. */
	public static final String MAP_FILE_EXTENSION = ".map";

	/** The encoding usually used within Kieker. */
	public static final String ENCODING = "UTF-8";

	private FSUtil() {
		// private default constructor
	}

	/**
	 * Encodes the given line (replaces {@code \\} with {@code \\\\}, {@code \r} with {@code \\r} and {@code \n} with
	 * {@code \\n}).
	 *
	 * @param str
	 *            The string to encode.
	 *
	 * @return The modified string.
	 */
	public static final String encodeNewline(final String str) {
		final int length = str.length();
		final StringBuilder sb = new StringBuilder(length + 16);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			final char c = str.charAt(i);
			if (c == '\\') {
				changed = true;
				sb.append('\\'); // NOPMD (double append is faster)
				sb.append('\\'); // NOPMD (double append is faster)
			} else if (c == '\r') {
				changed = true;
				sb.append('\\'); // NOPMD (double append is faster)
				sb.append('r'); // NOPMD (double append is faster)
			} else if (c == '\n') {
				changed = true;
				sb.append('\\'); // NOPMD (double append is faster)
				sb.append('n'); // NOPMD (double append is faster)
			} else {
				sb.append(c);
			}
		}
		if (changed) {
			return sb.toString();
		} else {
			return str;
		}
	}

	/**
	 * Decodes the given line (replaces {@code \\\\} with {@code \\}, {@code \\r} with {@code \r} and {@code \\n} with
	 * {@code \n}).
	 *
	 * @param str
	 *            The string to decode.
	 *
	 * @return The modified string.
	 */
	public static final String decodeNewline(final String str) {
		final int length = str.length();
		final StringBuilder sb = new StringBuilder(length);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			final char c = str.charAt(i);
			if (c == '\\') {
				final char d = str.charAt(++i); // NOCS
				if (d == '\\') {
					changed = true;
					sb.append('\\');
				} else if (d == 'r') {
					changed = true;
					sb.append('\r');
				} else if (d == 'n') {
					changed = true;
					sb.append('\n');
				} else {
					// we simply ignore unknown escapes
					sb.append(c);
					sb.append(d);
				}
			} else {
				sb.append(c);
			}
		}
		if (changed) {
			return sb.toString();
		} else {
			return str;
		}
	}

	/**
	 * @param startDirectory
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
	static class RecursiveFileVisitor extends SimpleFileVisitor<Path> {
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
