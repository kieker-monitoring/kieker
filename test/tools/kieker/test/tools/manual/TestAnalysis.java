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

package kieker.test.tools.manual;

import java.io.File;

import kieker.analysis.AnalysisController;
import kieker.analysis.filter.CountingFilter;
import kieker.analysis.filter.TeeFilter;
import kieker.analysis.filter.TimestampFilter;
import kieker.analysis.filter.TypeFilter;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.flow.EventRecordTraceGenerationFilter;
import kieker.tools.traceAnalysis.plugins.flow.EventTrace2ExecutionTraceFilter;
import kieker.tools.traceAnalysis.plugins.flow.TraceIdFilter;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.ComponentDependencyGraphPluginAllocation;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.OperationDependencyGraphPluginAllocation;
import kieker.tools.traceAnalysis.plugins.visualization.sequenceDiagram.SequenceDiagramPlugin;
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
				analysisController = new AnalysisController(new File("analysisproject/flowproject.xml"));
			} else {
				analysisController = new AnalysisController();

				/* Reader */
				final Configuration confReader = new Configuration();
				confReader.setProperty(FSReader.CONFIG_INPUTDIRS, "analysisproject/testdata-ascii/");
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
				final TeeFilter teeFilter2 = new TeeFilter(confTeeFilter2);
				teeFilter2.setName("CountBegin");

				final Configuration confTeeFilter3 = new Configuration();
				confTeeFilter3.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDERR);
				final TeeFilter teeFilter3 = new TeeFilter(confTeeFilter3);
				teeFilter3.setName("CountAfter");

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
				confSequenceDiagramPlugin.setProperty(SequenceDiagramPlugin.CONFIG_OUTPUT_FN_BASE, "analysisproject/out/SequenceAssembly");
				final SequenceDiagramPlugin sequenceDiagramPlugin = new SequenceDiagramPlugin(confSequenceDiagramPlugin);

				final Configuration confComponentDependencyGraphPluginAllocation = new Configuration();
				confComponentDependencyGraphPluginAllocation.setProperty(
						ComponentDependencyGraphPluginAllocation.CONFIG_OUTPUT_FN_BASE, "analysisproject/out/dependency");
				final ComponentDependencyGraphPluginAllocation componentDependencyGraphPluginAllocation =
						new ComponentDependencyGraphPluginAllocation(confComponentDependencyGraphPluginAllocation);

				final Configuration confOperationDependencyGraphPluginAllocation = new Configuration();
				confOperationDependencyGraphPluginAllocation.setProperty(
						OperationDependencyGraphPluginAllocation.CONFIG_DOT_OUTPUT_FILE, "analysisproject/out/dependency-operation");
				final OperationDependencyGraphPluginAllocation operationDependencyGraphPluginAllocation =
						new OperationDependencyGraphPluginAllocation(confOperationDependencyGraphPluginAllocation);

				analysisController.registerRepository(traceRepo);

				analysisController.setReader(reader);

				analysisController.registerPlugin(countingFilter1);
				AbstractPlugin.connect(reader, FSReader.OUTPUT_PORT_NAME, countingFilter1, CountingFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(typeFilter);
				AbstractPlugin.connect(countingFilter1, CountingFilter.OUTPUT_PORT_NAME, typeFilter, TypeFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(timestampFilter);
				AbstractPlugin.connect(typeFilter, TypeFilter.OUTPUT_PORT_NAME, timestampFilter, TimestampFilter.INPUT_PORT_NAME_FLOW);

				analysisController.registerPlugin(teeFilter2);
				AbstractPlugin.connect(countingFilter1, CountingFilter.OUTPUT_PORT_NAME_COUNT, teeFilter2, TeeFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(traceIdFilter);
				AbstractPlugin.connect(timestampFilter, TimestampFilter.OUTPUT_PORT_NAME, traceIdFilter, TraceIdFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(countingFilter2);
				AbstractPlugin.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME, countingFilter2, CountingFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(teeFilter3);
				AbstractPlugin.connect(countingFilter2, CountingFilter.OUTPUT_PORT_NAME_COUNT, teeFilter3, TeeFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(eventRecordTraceGenerationFilter);
				AbstractPlugin.connect(countingFilter2, CountingFilter.OUTPUT_PORT_NAME, eventRecordTraceGenerationFilter,
						EventRecordTraceGenerationFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(eventTrace2ExecutionTraceFilter);
				AbstractPlugin.connect(eventRecordTraceGenerationFilter, EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME, eventTrace2ExecutionTraceFilter,
						EventTrace2ExecutionTraceFilter.INPUT_PORT_NAME);
				eventTrace2ExecutionTraceFilter.connect(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerPlugin(teeFilter1);
				AbstractPlugin.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, teeFilter1,
						TeeFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(sequenceDiagramPlugin);
				AbstractPlugin.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, sequenceDiagramPlugin, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				sequenceDiagramPlugin.connect(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerPlugin(componentDependencyGraphPluginAllocation);
				AbstractPlugin.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, componentDependencyGraphPluginAllocation,
						AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				componentDependencyGraphPluginAllocation.connect(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.registerPlugin(operationDependencyGraphPluginAllocation);
				AbstractPlugin.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME, operationDependencyGraphPluginAllocation,
						AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				operationDependencyGraphPluginAllocation.connect(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				analysisController.saveToFile(new File("analysisproject/flowproject.xml"), "FlowProject");
			}
			analysisController.run();
			traceRepo.saveSystemToHTMLFile("analysisproject/out/system");
		} catch (final Exception ex) {
			TestAnalysis.LOG.error("Error initializing AnalysisController", ex);
		}
	}
}
