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
package kieker.panalysis.examples.recordReader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import kieker.common.record.IMonitoringRecord;
import kieker.panalysis.framework.concurrent.TerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.ISink;
import kieker.panalysis.framework.core.ISource;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.framework.sequential.QueuePipe;
import kieker.panalysis.framework.util.BaseStage2StageExtractor;
import kieker.panalysis.stage.Collector;
import kieker.panalysis.stage.kieker.File2RecordFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class RecordReaderAnalysis extends Analysis {

	private static final int SECONDS = 1000;

	private WorkerThread workerThread;

	private File2RecordFilter file2RecordFilter;
	private Collector<IMonitoringRecord> collector;

	@Override
	public void init() {
		super.init();
		final IPipeline pipeline = this.buildPipeline();
		this.workerThread = new WorkerThread(pipeline, 0);
	}

	@Override
	public void start() {
		super.start();

		this.workerThread.terminate(TerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);

		this.workerThread.start();
		try {
			this.workerThread.join(60 * SECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	void setInputFile(final File file) {
		this.file2RecordFilter.fileInputPort.setAssociatedPipe(new MethodCallPipe<File>(file));
	}

	private IPipeline buildPipeline() {
		final BaseStage2StageExtractor baseStage2StageExtractor = new BaseStage2StageExtractor();

		// create stages
		this.file2RecordFilter = new File2RecordFilter();
		this.collector = new Collector<IMonitoringRecord>();

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.addAll(baseStage2StageExtractor.extract(this.file2RecordFilter));
		stages.add(this.collector);

		// connect stages by pipes
		final List<IPipe<?>> pipes = new LinkedList<IPipe<?>>();
		pipes.add(this.connectWithSequentialPipe(this.file2RecordFilter.recordOutputPort, this.collector.objectInputPort));

		final IPipeline pipeline = new IPipeline() {
			@SuppressWarnings("unchecked")
			public List<? extends IStage> getStartStages() {
				return baseStage2StageExtractor.extract(RecordReaderAnalysis.this.file2RecordFilter);
			}

			public List<IStage> getStages() {
				return stages;
			}

			public void fireStartNotification() throws Exception {
				// notify each stage
				for (final IStage stage : stages) {
					stage.onPipelineStarts();
				}
				// notify each pipe
				for (final IPipe<?> pipe : pipes) {
					pipe.onPipelineStarts();
				}
			}

			public void fireStopNotification() {
				// notify each stage
				for (final IStage stage : stages) {
					stage.onPipelineStops();
				}
				// notify each pipe
				for (final IPipe<?> pipe : pipes) {
					pipe.onPipelineStops();
				}
			}
		};

		return pipeline;
	}

	private <A extends ISource, B extends ISink<B>, T> IPipe<T> connectWithSequentialPipe(final IOutputPort<A, T> sourcePort,
			final IInputPort<B, T> targetPort) {
		final IPipe<T> pipe = new QueuePipe<T>();
		pipe.setSourcePort(sourcePort);
		pipe.setTargetPort(targetPort);
		return pipe;
	}

	WorkerThread getWorkerThread() {
		return this.workerThread;
	}

	public static void main(final String[] args) {
		final RecordReaderAnalysis analysis = new RecordReaderAnalysis();
		analysis.init();
		final long start = System.currentTimeMillis();
		analysis.start();
		final long end = System.currentTimeMillis();
		// analysis.terminate();
		final long duration = end - start;
		System.out.println("duration: " + duration + " ms"); // NOPMD (Just for example purposes)

		final IPipeline pipeline = analysis.workerThread.getPipeline();

		for (final IStage stage : pipeline.getStages()) {
			if (stage instanceof AbstractFilter<?>) {
				System.out.println(stage.getClass().getName() + ": " + ((AbstractFilter<?>) stage).getOverallDuration()); // NOPMD (Just for example purposes)
			}
		}
	}

	void setOutputRecordList(final List<IMonitoringRecord> records) {
		this.collector.setObjects(records);
	}
}
