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
package kieker.panalysis.examples.traceReconstruction;

import java.util.LinkedList;
import java.util.List;

import kieker.analysis.ClassNameRegistryRepository;
import kieker.panalysis.framework.concurrent.StageTerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.stage.FileExtensionFilter;
import kieker.panalysis.stage.io.File2TextLinesFilter;
import kieker.panalysis.stage.kieker.ClassNameRegistryCreationFilter;
import kieker.panalysis.stage.kieker.MonitoringLogDirectory2Files;
import kieker.panalysis.stage.kieker.fileToRecord.textLine.TextLine2RecordFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class TraceReconstructionAnalysis extends Analysis {
	private static final int SECONDS = 1000;

	private WorkerThread workerThread;

	private ClassNameRegistryRepository classNameRegistryRepository;

	@Override
	public void init() {
		super.init();
		final IPipeline pipeline = this.buildPipeline();
		this.workerThread = new WorkerThread(pipeline, 0);
	}

	/**
	 * @since 1.10
	 */
	private IPipeline buildPipeline() {
		final ClassNameRegistryCreationFilter classNameRegistryCreationFilter = new ClassNameRegistryCreationFilter(this.classNameRegistryRepository);
		final MonitoringLogDirectory2Files directory2FilesFilter = new MonitoringLogDirectory2Files();
		final FileExtensionFilter fileExtensionFilter = new FileExtensionFilter();
		final File2TextLinesFilter file2TextLinesFilter = new File2TextLinesFilter();
		final TextLine2RecordFilter textLine2RecordFilter = new TextLine2RecordFilter(this.classNameRegistryRepository);

		// TODO Auto-generated method stub

		// add each stage to a stage list
		final LinkedList<IStage> startStages = new LinkedList<IStage>();

		final List<IStage> stages = new LinkedList<IStage>();

		final IPipeline pipeline = new IPipeline() {
			@SuppressWarnings("unchecked")
			public List<? extends IStage> getStartStages() {
				return startStages;
			}

			public List<IStage> getStages() {
				return stages;
			}

			public void fireStartNotification() throws Exception {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStarts();
				}
			}

			public void fireStopNotification() {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStops();
				}
			}
		};

		return pipeline;
	}

	@Override
	public void start() {
		super.start();

		this.workerThread.terminate(StageTerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);

		this.workerThread.start();
		try {
			this.workerThread.join(60 * SECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}
