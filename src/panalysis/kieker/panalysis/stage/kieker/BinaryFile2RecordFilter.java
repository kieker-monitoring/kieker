/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.stage.kieker;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.kieker.RecordFromBinaryFileCreator;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class BinaryFile2RecordFilter extends AbstractFilter<BinaryFile2RecordFilter> {

	private static final int MB = 1024 * 1024;

	public final IInputPort<BinaryFile2RecordFilter, File> fileInputPort = this.createInputPort();
	public final IOutputPort<BinaryFile2RecordFilter, IMonitoringRecord> recordOutputPort = this.createOutputPort();

	private Map<Integer, String> stringRegistry;

	private final RecordFromBinaryFileCreator recordFromBinaryFileCreator;

	public BinaryFile2RecordFilter() {
		// FIXME stringRegistry
		this.recordFromBinaryFileCreator = new RecordFromBinaryFileCreator(this.logger, this.stringRegistry);
	}

	@Override
	protected boolean execute(final Context<BinaryFile2RecordFilter> context) {
		final File file = context.tryTake(this.fileInputPort);
		if (file == null) {
			return false;
		}

		try {
			final BinaryCompressionMethod method = BinaryCompressionMethod.getByFileExtension(file.getName());
			final DataInputStream in = method.getDataInputStream(file, 1 * MB);
			try {
				IMonitoringRecord record = this.recordFromBinaryFileCreator.createRecordFromBinaryFile(in);
				while (record != null) {
					context.put(this.recordOutputPort, record);
					record = this.recordFromBinaryFileCreator.createRecordFromBinaryFile(in);
				}
			} catch (final MonitoringRecordException e) {
				this.logger.error("Error reading file: " + file, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (final IOException ex) {
						this.logger.error("Exception while closing input stream for processing input file", ex);
					}
				}
			}
		} catch (final IOException e) {
			this.logger.error("Error reading file: " + file, e);
		} catch (final IllegalArgumentException e) {
			this.logger.warn("Unknown file extension for file: " + file);
		}

		return true;
	}

}
