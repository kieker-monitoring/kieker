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

import java.io.IOException;

import kieker.common.util.StringUtils;
import kieker.monitoring.core.registry.RegistryRecord;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class StringMappingFileWriter implements MappingFileWriter {

	private final StringBuilder sb;

	public StringMappingFileWriter() {
		this.sb = new StringBuilder();
	}

	public final void write(final RegistryRecord hashRecord) throws IOException {
		synchronized (this.sb) {
			final StringBuilder sbl = this.sb;
			sbl.append('$');
			sbl.append(hashRecord.getId());
			sbl.append('=');
			sbl.append(StringUtils.encodeNewline(String.valueOf(hashRecord.getObject())));
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
