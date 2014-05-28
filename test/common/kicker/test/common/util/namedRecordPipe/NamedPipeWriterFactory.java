/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.common.util.namedRecordPipe;

import java.util.concurrent.atomic.AtomicInteger;

import kicker.common.namedRecordPipe.Broker;
import kicker.common.namedRecordPipe.IPipeWriter;
import kicker.common.namedRecordPipe.Pipe;
import kicker.common.record.IMonitoringRecord;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public final class NamedPipeWriterFactory {
	private static final AtomicInteger NEXT_PIPE_ID = new AtomicInteger(0);
	private static final String PIPE_NAME_PREFIX = "pipeName_" + NamedPipeWriterFactory.class.getName() + "_";

	private NamedPipeWriterFactory() {}

	/**
	 * This method should be used in tests to generate unique names for {@link kicker.monitoring.core.configuration.Configuration}s with
	 * {@link kicker.monitoring.writer.namedRecordPipe.PipeWriter}s and {@link java.io.PipedReader}s
	 * in order to avoid naming conflicts.
	 * 
	 * @return a unique name
	 */
	public static final String createPipeName() {
		return NamedPipeWriterFactory.PIPE_NAME_PREFIX + NamedPipeWriterFactory.NEXT_PIPE_ID.getAndIncrement();
	}

	/**
	 * Creates an {@link kicker.common.namedRecordPipe.IPipeWriter} that writes records
	 * to a {@link Pipe} with the given name.
	 * 
	 * @param pipeName
	 *            The name of the pipe to use.
	 * @return the {@link kicker.common.namedRecordPipe.IPipeWriter}
	 */
	public static final IPipeWriter createAndRegisterNamedPipeRecordWriter(final String pipeName) {
		final Pipe namedPipe = Broker.INSTANCE.acquirePipe(pipeName);
		final IPipeWriter writer = new IPipeWriter() {

			@Override
			public boolean newMonitoringRecord(final IMonitoringRecord record) {
				return namedPipe.writeMonitoringRecord(record);
			}
		};
		return writer;
	}
}
