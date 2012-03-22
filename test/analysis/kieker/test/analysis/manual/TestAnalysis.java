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
import kieker.analysis.filter.CountingFilter;
import kieker.analysis.filter.TeeFilter;
import kieker.analysis.filter.TimestampFilter;
import kieker.analysis.filter.TypeFilter;
import kieker.analysis.filter.trace.TraceIdFilter;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTraceGenerationFilter;
import kieker.tools.traceAnalysis.filter.flow.EventTrace2ExecutionTraceFilter;
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
				final Configuration confReader = new Configuration();
				confReader.setProperty(FSReader.CONFIG_INPUTDIRS, "tmp/testdata-ascii/");
				final FSReader reader = new FSReader(confReader);

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
				final EventTrace2ExecutionTraceFilter eventTrace2ExecutionTraceFilter =
						new EventTrace2ExecutionTraceFilter(confEventTrace2ExecutionTraceFilter);

				/* Visualization */
				final Configuration confSequenceDiagramPlugin = new Configuration();
				confSequenceDiagramPlugin.setProperty(SequenceDiagramFilter.CONFIG_OUTPUT_FN_BASE, "tmp/SequenceAssembly");
				final SequenceDiagramFilter sequenceDiagramPlugin = new SequenceDiagramFilter(confSequenceDiagramPlugin);

				final Configuration confComponentDependencyGraphPluginAllocation = new Configuration();
				confComponentDependencyGraphPluginAllocation.setProperty(
						ComponentDependencyGraphAllocationFilter.CONFIG_OUTPUT_FN_BASE, "tmp/dependency");
				final ComponentDependencyGraphAllocationFilter componentDependencyGraphPluginAllocation =
						new ComponentDependencyGraphAllocationFilter(confComponentDependencyGraphPluginAllocation);

				final Configuration confOperationDependencyGraphPluginAllocation = new Configuration();
				confOperationDependencyGraphPluginAllocation.setProperty(
						OperationDependencyGraphAllocationFilter.CONFIG_DOT_OUTPUT_FILE, "tmp/dependency-operation");
				final OperationDependencyGraphAllocationFilter operationDependencyGraphPluginAllocation =
						new OperationDependencyGraphAllocationFilter(confOperationDependencyGraphPluginAllocation);

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

				analysisController.registerFilter(eventTrace2ExecutionTraceFilter);
				analysisController.connect(eventRecordTraceGenerationFilter, EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME, eventTrace2ExecutionTraceFilter,
						EventTrace2ExecutionTraceFilter.INPUT_PORT_NAME);
				analysisController.connect(eventTrace2ExecutionTraceFilter, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(teeFilter1);
				analysisController.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, teeFilter1,
						TeeFilter.INPUT_PORT_NAME);

				analysisController.registerFilter(sequenceDiagramPlugin);
				analysisController.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, sequenceDiagramPlugin, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME);
				analysisController.connect(sequenceDiagramPlugin, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(componentDependencyGraphPluginAllocation);
				analysisController.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, componentDependencyGraphPluginAllocation,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME);
				analysisController.connect(componentDependencyGraphPluginAllocation, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerFilter(operationDependencyGraphPluginAllocation);
				analysisController.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, operationDependencyGraphPluginAllocation,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME);
				analysisController.connect(operationDependencyGraphPluginAllocation, AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.saveToFile(new File("tmp/testproject.kax"));
			}
			analysisController.run();
			traceRepo.saveSystemToHTMLFile("tmp/system");
		} catch (final Exception ex) {
			TestAnalysis.LOG.error("Error initializing AnalysisController", ex);
		}
	}
}
