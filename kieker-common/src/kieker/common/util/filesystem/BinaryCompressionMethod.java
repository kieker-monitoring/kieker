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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This enum delivers different compression methods, which can be used to read and write compressed binary files.
 * 
 * @author Jan Waller
 * 
 * @since 1.7
 */
public enum BinaryCompressionMethod {
	/** A binary compression method using no compression format. */
	NONE(".bin") {
		@Override
		public DataOutputStream getDataOutputStream(final File outputFile, final int bufferSize) throws IOException {
			return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile), bufferSize));
		}

		@Override
		public DataInputStream getDataInputStream(final File inputFile, final int bufferSize) throws IOException {
			return new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile), bufferSize));
		}
	},
	/** A binary compression method using the compression format "deflate". */
	DEFLATE(".bin.df") {
		@Override
		public DataOutputStream getDataOutputStream(final File outputFile, final int bufferSize) throws IOException {
			return new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(new FileOutputStream(outputFile)), bufferSize));
		}

		@Override
		public DataInputStream getDataInputStream(final File inputFile, final int bufferSize) throws IOException {
			return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new FileInputStream(inputFile)), bufferSize));
		}
	},
	/** A binary compression method using the compression format "GZIP". */
	GZIP(".bin.gz") {
		@Override
		public DataOutputStream getDataOutputStream(final File outputFile, final int bufferSize) throws IOException {
			return new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(outputFile)), bufferSize));
		}

		@Override
		public DataInputStream getDataInputStream(final File inputFile, final int bufferSize) throws IOException {
			return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(inputFile)), bufferSize));
		}
	},
	/** A binary compression method using the compression format "ZIP". */
	ZIP(".bin.zip") {
		@Override
		public DataOutputStream getDataOutputStream(final File outputFile, final int bufferSize) throws IOException {
			final ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(outputFile));
			String shortname = outputFile.getName();
			shortname = shortname.substring(0, shortname.length() - 4); // strip ".zip"
			zipStream.putNextEntry(new ZipEntry(shortname));
			return new DataOutputStream(new BufferedOutputStream(zipStream, bufferSize));
		}

		@Override
		public DataInputStream getDataInputStream(final File inputFile, final int bufferSize) throws IOException {
			final ZipInputStream zipStream = new ZipInputStream(new FileInputStream(inputFile));
			zipStream.getNextEntry();
			return new DataInputStream(new BufferedInputStream(zipStream, bufferSize));
		}
	};

	private final String fileExtension;

	private BinaryCompressionMethod(final String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public final String getFileExtension() {
		return this.fileExtension;
	}

	/**
	 * Implementing compression methods should override this method to deliver an output stream which can be used to write data in a compressed way into the given
	 * file.
	 * 
	 * @param outputFile
	 *            The output file.
	 * @param bufferSize
	 *            The buffer size for the stream
	 * 
	 * @return A new output stream for the given file.
	 * 
	 * @throws IOException
	 *             If something went wrong during the initialization.
	 */
	public abstract DataOutputStream getDataOutputStream(final File outputFile, final int bufferSize) throws IOException;

	/**
	 * Implementing compression methods should override this method to deliver an input stream which can be used to read data in a non-compressed way from the given
	 * file.
	 * 
	 * @param inputFile
	 *            The input file.
	 * @param bufferSize
	 *            The buffer size for the stream
	 * 
	 * @return A new input stream for the given file.
	 * 
	 * @throws IOException
	 *             If something went wrong during the initialization.
	 */
	public abstract DataInputStream getDataInputStream(final File inputFile, final int bufferSize) throws IOException;

	/**
	 * This method checks whether there exists a suitable compression method for the extension of the file.
	 * 
	 * @param name
	 *            The name of the file.
	 * 
	 * @return true if a suitable compression method exists.
	 * 
	 * @see #getByFileExtension(String)
	 */
	public static final boolean hasValidFileExtension(final String name) {
		for (final BinaryCompressionMethod method : BinaryCompressionMethod.values()) {
			if (name.endsWith(method.getFileExtension())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method tries to search for a suitable compression method using the extension of the file.
	 * 
	 * @param name
	 *            The name of the file.
	 * 
	 * @return A suitable compression method if it exists.
	 * 
	 * @throws IllegalArgumentException
	 *             If a suitable method was not found.
	 * 
	 * @see #hasValidFileExtension(String)
	 */
	public static final BinaryCompressionMethod getByFileExtension(final String name) throws IllegalArgumentException {
		for (final BinaryCompressionMethod method : BinaryCompressionMethod.values()) {
			if (name.endsWith(method.getFileExtension())) {
				return method;
			}
		}
		throw new IllegalArgumentException();
	}
}
