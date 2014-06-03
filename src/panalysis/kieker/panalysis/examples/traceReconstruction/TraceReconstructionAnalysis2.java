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
import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.panalysis.framework.concurrent.StageTerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.core.Pipeline;
import kieker.panalysis.framework.sequential.QueuePipe;
import kieker.panalysis.predicate.IsIMonitoringRecordInRange;
import kieker.panalysis.predicate.IsOperationExecutionRecordTraceIdPredicate;
import kieker.panalysis.stage.Cache;
import kieker.panalysis.stage.CountingFilter;
import kieker.panalysis.stage.InstanceOfFilter;
import kieker.panalysis.stage.basic.PredicateFilter;
import kieker.panalysis.stage.io.File2TextLinesFilter;
import kieker.panalysis.stage.kieker.ClassNameRegistryCreationFilter;
import kieker.panalysis.stage.kieker.MonitoringLogDirectory2Files;
import kieker.panalysis.stage.kieker.fileToRecord.textLine.TextLine2RecordFilter;
import kieker.panalysis.stage.kieker.traceReconstruction.TraceReconstructionFilter;
import kieker.panalysis.stage.stringBuffer.StringBufferFilter;
import kieker.panalysis.stage.util.TextLine;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class TraceReconstructionAnalysis2 extends Analysis {
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
		// predicates TODO
		final IsIMonitoringRecordInRange isIMonitoringRecordInRange = new IsIMonitoringRecordInRange(0, 1000);
		final IsOperationExecutionRecordTraceIdPredicate isOperationExecutionRecordTraceIdPredicate = new IsOperationExecutionRecordTraceIdPredicate(false, null);

		final ClassNameRegistryCreationFilter classNameRegistryCreationFilter = new ClassNameRegistryCreationFilter(this.classNameRegistryRepository);
		final MonitoringLogDirectory2Files directory2FilesFilter = new MonitoringLogDirectory2Files();
		final File2TextLinesFilter file2TextLinesFilter = new File2TextLinesFilter();
		final Cache<TextLine> cache = new Cache<TextLine>();

		final TextLine2RecordFilter textLine2RecordFilter = new TextLine2RecordFilter(this.classNameRegistryRepository);
		final StringBufferFilter<IMonitoringRecord> stringBufferFilter = new StringBufferFilter<IMonitoringRecord>();
		final PredicateFilter<IMonitoringRecord> timestampFilter = new PredicateFilter<IMonitoringRecord>(isIMonitoringRecordInRange);
		final PredicateFilter<OperationExecutionRecord> traceIdFilter = new PredicateFilter<OperationExecutionRecord>(isOperationExecutionRecordTraceIdPredicate);
		final InstanceOfFilter<IMonitoringRecord, IFlowRecord> instanceOfFilter = new InstanceOfFilter<IMonitoringRecord, IFlowRecord>(IFlowRecord.class);
		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter();
		final CountingFilter<TraceEventRecords> countingFilter = new CountingFilter<TraceEventRecords>();

		// add each stage to a stage list
		final LinkedList<IStage> startStages = new LinkedList<IStage>();
		startStages.add(classNameRegistryCreationFilter);

		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(classNameRegistryCreationFilter);
		stages.add(directory2FilesFilter);
		stages.add(file2TextLinesFilter);
		stages.add(cache);

		stages.add(textLine2RecordFilter);
		stages.add(stringBufferFilter);
		stages.add(timestampFilter);
		stages.add(traceIdFilter);
		stages.add(instanceOfFilter);
		stages.add(traceReconstructionFilter);
		stages.add(countingFilter);

		// connect pipes
		QueuePipe.connect(classNameRegistryCreationFilter.filePrefixOutputPort, directory2FilesFilter.filePrefixInputPort);
		QueuePipe.connect(classNameRegistryCreationFilter.relayDirectoryOutputPort, directory2FilesFilter.directoryInputPort);
		QueuePipe.connect(directory2FilesFilter.fileOutputPort, file2TextLinesFilter.fileInputPort);
		QueuePipe.connect(file2TextLinesFilter.textLineOutputPort, cache.objectInputPort);
		// QueuePipe.connect(XXX, cache.sendInputPort);
		QueuePipe.connect(cache.objectOutputPort, textLine2RecordFilter.textLineInputPort);
		QueuePipe.connect(textLine2RecordFilter.recordOutputPort, stringBufferFilter.objectInputPort);
		QueuePipe.connect(stringBufferFilter.objectOutputPort, timestampFilter.inputPort);
		QueuePipe.connect(timestampFilter.matchingOutputPort, traceIdFilter.inputPort);
		// QueuePipe.connect(timestampFilter.mismatchingOutputPort, YYY); // ignore this case
		QueuePipe.connect(traceIdFilter.matchingOutputPort, instanceOfFilter.inputPort);
		// QueuePipe.connect(traceIdFilter.mismatchingOutputPort, traceIdFilter.inputPort); // ignore this case
		QueuePipe.connect(XXX, traceReconstructionFilter.timestampInputPort);
		QueuePipe.connect(instanceOfFilter.matchingOutputPort, traceReconstructionFilter.recordInputPort);
		// QueuePipe.connect(instanceOfFilter.mismatchingOutputPort, instanceOfFilter.inputPort); // ignore this case
		QueuePipe.connect(traceReconstructionFilter.traceValidOutputPort, countingFilter.INPUT_OBJECT);
		// QueuePipe.connect(traceReconstructionFilter.traceInvalidOutputPort, XXX); // ignore this case

		final Pipeline pipeline = new Pipeline();
		pipeline.setStartStages(startStages);
		pipeline.setStages(stages);
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
