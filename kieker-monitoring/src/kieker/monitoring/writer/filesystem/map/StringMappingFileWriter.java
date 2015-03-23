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

import java.io.IOException;

import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.filesystem.FSUtil;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.7
 */
public final class StringMappingFileWriter {

	private final StringBuilder sb; // NOPMD (we need this buffer here)

	/**
	 * Creates a new instance of this class.
	 */
	public StringMappingFileWriter() {
		this.sb = new StringBuilder(1024 * 1024); // reserve 1 MiB space
	}

	/**
	 * Writes the given mapping to the internal string builder.
	 * 
	 * @param hashRecord
	 *            The mapping to write.
	 * 
	 * @throws IOException
	 *             Should not happen under normal circumstances.
	 */
	public final void write(final RegistryRecord hashRecord) throws IOException {
		synchronized (this.sb) {
			final StringBuilder sbl = this.sb;
			sbl.append('$');
			sbl.append(hashRecord.getId());
			sbl.append('=');
			sbl.append(FSUtil.encodeNewline(hashRecord.getString()));
			sbl.append('\n');
		}
	}

	@Override
	public final String toString() {
		synchronized (this.sb) {
			return this.sb.toString();
		}
	}
}
