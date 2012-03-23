/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.analysis.manual;

import java.io.File;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.analysis.plugin.filter.select.TypeFilter;
import kieker.analysis.plugin.filter.trace.TraceIdFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTraceGenerationFilter;
import kieker.tools.traceAnalysis.filter.flow.EventTrace2ExecutionAndMessageTraceFilter;
import kieker.tools.traceAnalysis.filter.systemModel.SystemModel2FileFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.filter.visualization.sequenceDiagram.SequenceDiagramFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * @author Jan Waller
 */
public class TestAnalysis {
	private static final Log LOG = LogFactory.getLog(TestAnalysis.class);
	private static final boolean LOADCONFIG = false;

	public static void main(final String[] args) {
		try {
			final AnalysisController analysisController;
			final SystemModelRepository traceRepo = new SystemModelRepository(new Configuration());
			if (TestAnalysis.LOADCONFIG) {
				analysisController = new AnalysisController(new File("tmp/testproject.kax"));
			} else {
				analysisController = new AnalysisController("TestProject");

				/* Reader */
				final Configuration confFSReader = new Configuration();
				confFSReader.setProperty(FSReader.CONFIG_INPUTDIRS, "tmp/testdata-ascii/");
				final FSReader reader = new FSReader(confFSReader);

				/* TypeFilter */
				final Configuration confTypeFilter = new Configuration();
				final TypeFilter typeFilter = new TypeFilter(confTypeFilter);

				/* TeeFilter */
				final Configuration confTeeFilter1 = new Configuration();
				confTeeFilter1.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDOUT);
				final TeeFilter teeFilter1 = new TeeFilter(confTeeFilter1);

				final Configuration confTeeFilter2 = new Configuration();
				confTeeFilter2.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDERR);
				confTeeFilter2.setProperty(AbstractPlugin.CONFIG_NAME, "CountBegin");
				final TeeFilter teeFilter2 = new TeeFilter(confTeeFilter2);

				final Configuration confTeeFilter3 = new Configuration();
				confTeeFilter3.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDERR);
				confTeeFilter3.setProperty(AbstractPlugin.CONFIG_NAME, "CountAfter");
				final TeeFilter teeFilter3 = new TeeFilter(confTeeFilter3);

				/* CountingFilter */
				final Configuration confCountingFilter1 = new Configuration();
				final CountingFilter countingFilter1 = new CountingFilter(confCountingFilter1);

				final Configuration confCountingFilter2 = new Configuration();
				final CountingFilter countingFilter2 = new CountingFilter(confCountingFilter2);

				final Configuration confTimestampFilter = new Configuration();
				final TimestampFilter timestampFilter = new TimestampFilter(confTimestampFilter);

				/* Filter */
				final Configuration confTraceIdFilter = new Configuration();
				final TraceIdFilter traceIdFilter = new TraceIdFilter(confTraceIdFilter);

				final Configuration confEventRecordTraceGenerationFilter = new Configuration();
				final EventRecordTraceGenerationFilter eventRecordTraceGenerationFilter =
						new EventRecordTraceGenerationFilter(confEventRecordTraceGenerationFilter);

				final Configuration confEventTrace2ExecutionTraceFilter = new Configuration();
				final EventTrace2ExecutionAndMessageTraceFilter eventTrace2ExecutionTraceFilter =
						new EventTrace2ExecutionAndMessageTraceFilter(confEventTrace2ExecutionTraceFilter);

				/* Visualization */
				final Configuration confSequenceDiagramFilter = new Configuration();
				confSequenceDiagramFilter.setProperty(SequenceDiagramFilter.CONFIG_OUTPUT_FN_BASE, "tmp/SequenceAssembly");
				final SequenceDiagramFilter sequenceDiagramFilter = new SequenceDiagramFilter(confSequenceDiagramFilter);

				final Configuration confComponentDependencyGraphAllocationFilter = new Configuration();
				confComponentDependencyGraphAllocationFilter.setProperty(
						ComponentDependencyGraphAllocationFilter.CONFIG_OUTPUT_FN_BASE, "tmp/dependency");
				final ComponentDependencyGraphAllocationFilter componentDependencyGraphAllocationFilter =
						new ComponentDependencyGraphAllocationFilter(confComponentDependencyGraphAllocationFilter);

				final Configuration confOperationDependencyGraphAllocationFilter = new Configuration();
				confOperationDependencyGraphAllocationFilter.setProperty(
						OperationDependencyGraphAllocationFilter.CONFIG_DOT_OUTPUT_FILE, "tmp/dependency-operation");
				final OperationDependencyGraphAllocationFilter operationDependencyGraphAllocationFilter =
						new OperationDependencyGraphAllocationFilter(confOperationDependencyGraphAllocationFilter);

				/* Visualization */
				final Configuration confSystemModel2FileFilter = new Configuration();
				confSystemModel2FileFilter.setProperty(SystemModel2FileFilter.CONFIG_HTML_OUTPUT_FN, "tmp/system-model.html");
				final SystemModel2FileFilter systemModel2FileFilter = new SystemModel2FileFilter(confSystemModel2FileFilter);

				analysisController.registerRepository(traceRepo);

				analysisController.registerReader(reader);

				analysisController.registerFilter(countingFilter1);
				analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME, countingFilter1, CountingFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(typeFilter);
				analysisController.connect(countingFilter1, CountingFilter.OUTPUT_PORT_NAME, typeFilter, TypeFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(timestampFilter);
				analysisController.connect(typeFilter, TypeFilter.OUTPUT_PORT_NAME, timestampFilter, TimestampFilter.INPUT_PORT_NAME_FLOW);

				analysisController.registerFilter(teeFilter2);
				analysisController.connect(countingFilter1, CountingFilter.OUTPUT_PORT_NAME_COUNT, teeFilter2, TeeFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(traceIdFilter);
				analysisController.connect(timestampFilter, TimestampFilter.OUTPUT_PORT_NAME, traceIdFilter, TraceIdFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(countingFilter2);
				analysisController.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME, countingFilter2, CountingFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(teeFilter3);
				analysisController.connect(countingFilter2, CountingFilter.OUTPUT_PORT_NAME_COUNT, teeFilter3, TeeFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(eventRecordTraceGenerationFilter);
				analysisController.connect(countingFilter2, CountingFilter.OUTPUT_PORT_NAME, eventRecordTraceGenerationFilter,
						EventRecordTraceGenerationFilter.INPUT_PORT_NAME);
				analysisController.connect(eventRecordTraceGenerationFilter, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(eventTrace2ExecutionTraceFilter);
				analysisController.connect(eventRecordTraceGenerationFilter, EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME, eventTrace2ExecutionTraceFilter,
						EventTrace2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME);
				analysisController.connect(eventTrace2ExecutionTraceFilter, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(teeFilter1);
				analysisController.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, teeFilter1,
						TeeFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(sequenceDiagramFilter);
				analysisController.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, sequenceDiagramFilter, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME);
				analysisController.connect(sequenceDiagramFilter, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(componentDependencyGraphAllocationFilter);
				analysisController.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, componentDependencyGraphAllocationFilter,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME);
				analysisController.connect(componentDependencyGraphAllocationFilter, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(operationDependencyGraphAllocationFilter);
				analysisController.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, operationDependencyGraphAllocationFilter,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME);
				analysisController.connect(operationDependencyGraphAllocationFilter, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(systemModel2FileFilter);
				analysisController.connect(systemModel2FileFilter, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.saveToFile(new File("tmp/testproject.kax"));
			}
			analysisController.run();
		} catch (final Exception ex) {
			TestAnalysis.LOG.error("Error initializing AnalysisController", ex);
		}
	}
}
