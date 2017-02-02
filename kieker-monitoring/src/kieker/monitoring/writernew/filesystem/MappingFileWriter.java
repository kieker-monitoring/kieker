/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writernew.filesystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import kieker.common.util.filesystem.FSUtil;

/**
 * Encapsulates the writer for Kieker's mapping file.
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
class MappingFileWriter {

	private final PrintWriter printWriter;

	public MappingFileWriter(final Path folder, final String charsetName) {
		// we expect "folder" to exist
		final Path newMappingFile = folder.resolve(FSUtil.MAP_FILENAME);
		final Charset charset = Charset.forName(charsetName);

		try {
			final Writer w = Files.newBufferedWriter(newMappingFile, charset, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			this.printWriter = new PrintWriter(w);
		} catch (final IOException e) {
			throw new IllegalStateException("Error on creating Kieker's mapping file.", e);
		}
	}

	public PrintWriter getFileWriter() {
		return this.printWriter;
	}

	public void close() {
		this.printWriter.close();
	}

}
