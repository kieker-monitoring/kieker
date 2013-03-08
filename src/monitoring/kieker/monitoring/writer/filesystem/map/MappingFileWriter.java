/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.util.StringUtils;
import kieker.monitoring.core.registry.RegistryRecord;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class MappingFileWriter {
	public static final String KIEKER_MAP_FN = "kieker.map";

	private static final String ENCODING = "UTF-8";

	private final File mappingFile;

	public MappingFileWriter(final String path) throws IOException {
		final StringBuilder sbm = new StringBuilder(path.length() + 11);
		sbm.append(path).append(File.separatorChar).append(KIEKER_MAP_FN);
		final String mappingFileFn = sbm.toString();
		this.mappingFile = new File(mappingFileFn);
		if (!this.mappingFile.createNewFile()) {
			throw new IOException("Mapping File '" + mappingFileFn + "' already exists.");
		}
	}

	public final void write(final RegistryRecord hashRecord) throws IOException {
		synchronized (this.mappingFile) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.mappingFile, true), ENCODING));
				pw.write('$');
				pw.write(String.valueOf(hashRecord.getId()));
				pw.write('=');
				pw.write(StringUtils.encodeNewline(String.valueOf(hashRecord.getObject())));
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
