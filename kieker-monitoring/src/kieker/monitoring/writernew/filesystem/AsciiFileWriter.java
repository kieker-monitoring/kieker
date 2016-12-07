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

import java.io.PrintWriter;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writernew.AbstractMonitoringWriter;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class AsciiFileWriter extends AbstractMonitoringWriter {

	private final FileWriterPool fileWriterPool;

	public AsciiFileWriter(final Configuration configuration) {
		super(configuration);
		this.fileWriterPool = new FileWriterPool();
	}

	@Override
	public void onStarting() {}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		final PrintWriter fileWriter = this.fileWriterPool.getFileWriter();

		fileWriter.print('$');
		// ...
		fileWriter.println();
	}

	@Override
	public void onTerminating() {}

}
