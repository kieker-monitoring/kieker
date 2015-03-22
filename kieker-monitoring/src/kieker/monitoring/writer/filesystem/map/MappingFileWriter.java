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

package kieker.monitoring.writer.filesystem.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.filesystem.FSUtil;

/**
 * This writer is a helper class to handle the mapping files for Kieker's records, containing the mapping between short IDs and actual record classes. It provides
 * the possibility to create these mapping files and to write the mappings into them.
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.7
 */
public final class MappingFileWriter {

	private final File mappingFile;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param path
	 *            The path containing the record files. The writer will put the mapping file into this directory.
	 * 
	 * @throws IOException
	 *             If the mapping file has already been created.
	 */
	public MappingFileWriter(final String path) throws IOException {
		final StringBuilder sbm = new StringBuilder(path.length() + 11);
		sbm.append(path).append(File.separatorChar).append(FSUtil.MAP_FILENAME);
		final String mappingFileFn = sbm.toString();
		this.mappingFile = new File(mappingFileFn);
		if (!this.mappingFile.createNewFile()) {
			throw new IOException("Mapping File '" + mappingFileFn + "' already exists.");
		}
	}

	/**
	 * Writes the given mapping into the mapping file.
	 * 
	 * @param hashRecord
	 *            The mapping between the ID and the actual object.
	 * 
	 * @throws IOException
	 *             If something went wrong during the writing.
	 */
	public final void write(final RegistryRecord hashRecord) throws IOException {
		synchronized (this.mappingFile) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.mappingFile, true), FSUtil.ENCODING));
				pw.write('$');
				pw.write(String.valueOf(hashRecord.getId()));
				pw.write('=');
				pw.write(FSUtil.encodeNewline(hashRecord.getString()));
				pw.write('\n');
				if (pw.checkError()) {
					throw new IOException("Error writing to mappingFile " + this.mappingFile.toString());
				}
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}
	}
}
