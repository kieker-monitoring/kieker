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

import java.io.File;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipe;
import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipeFactory;
import kieker.panalysis.framework.core.CompositeFilter;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.stage.File2TextLinesFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class DatFile2RecordFilter extends CompositeFilter {

	public final IInputPort<File2TextLinesFilter, File> fileInputPort;

	public final IOutputPort<TextLine2RecordFilter, IMonitoringRecord> recordOutputPort;

	// TODO
	private Map<Integer, String> stringRegistry;

	public DatFile2RecordFilter() {
		final File2TextLinesFilter file2TextLinesFilter = new File2TextLinesFilter();
		final TextLine2RecordFilter textLine2RecordFilter = new TextLine2RecordFilter(this.stringRegistry);

		final ConcurrentWorkStealingPipeFactory<String> concurrentWorkStealingPipeFactory = new ConcurrentWorkStealingPipeFactory<String>();
		final ConcurrentWorkStealingPipe<String> pipe = concurrentWorkStealingPipeFactory.create();
		pipe.connect(file2TextLinesFilter.textLineOutputPort, textLine2RecordFilter.textLineInputPort);

		this.fileInputPort = file2TextLinesFilter.fileInputPort;
		this.recordOutputPort = textLine2RecordFilter.recordOutputPort;

		this.schedulableStages.add(file2TextLinesFilter);
		this.schedulableStages.add(textLine2RecordFilter);
	}
}
