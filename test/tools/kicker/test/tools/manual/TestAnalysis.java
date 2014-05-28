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

package kicker.test.tools.manual;

import java.io.File;
import java.io.IOException;

import kicker.analysis.AnalysisController;
import kicker.analysis.IAnalysisController;
import kicker.analysis.analysisComponent.AbstractAnalysisComponent;
import kicker.analysis.exception.AnalysisConfigurationException;
import kicker.analysis.plugin.filter.flow.EventRecordTraceReconstructionFilter;
import kicker.analysis.plugin.filter.forward.CountingFilter;
import kicker.analysis.plugin.filter.forward.StringBufferFilter;
import kicker.analysis.plugin.filter.forward.TeeFilter;
import kicker.analysis.plugin.filter.select.TimestampFilter;
import kicker.analysis.plugin.filter.select.TraceIdFilter;
import kicker.analysis.plugin.filter.select.TypeFilter;
import kicker.analysis.plugin.reader.filesystem.FSReader;
import kicker.common.configuration.Configuration;
import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kicker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kicker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
import kicker.tools.traceAnalysis.filter.flow.TraceEventRecords2ExecutionAndMessageTraceFilter;
import kicker.tools.traceAnalysis.filter.systemModel.SystemModel2FileFilter;
import kicker.tools.traceAnalysis.filter.visualization.GraphWriterPlugin;
import kicker.tools.traceAnalysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAllocationFilter;
import kicker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAllocationFilter;
import kicker.tools.traceAnalysis.filter.visualization.sequenceDiagram.SequenceDiagramFilter;
import kicker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class TestAnalysis {
	private static final Log LOG = LogFactory.getLog(TestAnalysis.class);
	private static final boolean LOADCONFIG = false;

	private static final String KAX_FILENAME = "tmp/testproject.kax";
	private static final String SRC_FILENAME = "tmp/testdata-ascii/";

	private TestAnalysis() {}

	public static void main(final String[] args) {
		final IAnalysisController analysisController;
		if (TestAnalysis.LOADCONFIG) {
			try {
				analysisController = new AnalysisController(new File(TestAnalysis.KAX_FILENAME));

			} catch (final IOException ex) {
				TestAnalysis.LOG.error("Failed to load " + TestAnalysis.KAX_FILENAME, ex);
				return;
			} catch (final AnalysisConfigurationException ex) {
				TestAnalysis.LOG.error("Failed to load " + TestAnalysis.KAX_FILENAME, ex);
				return;
			}
		} else {
			analysisController = new AnalysisController("TestProject");
			TestAnalysis.createAndConnectPlugins(analysisController);
		}
		try {
			analysisController.run();
		} catch (final AnalysisConfigurationException ex) {
			TestAnalysis.LOG.error("Failed to start the example project.", ex);
		}
	}

	private static void createAndConnectPlugins(final IAnalysisController analysisController) {
		final SystemModelRepository traceRepo = new SystemModelRepository(new Configuration(), analysisController);

		// Reader
		final Configuration confFSReader = new Configuration();
		confFSReader.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, SRC_FILENAME);
		final FSReader reader = new FSReader(confFSReader, analysisController);

		// String Buffer
		final StringBufferFilter buffer = new StringBufferFilter(new Configuration(), analysisController);

		// TypeFilter
		final Configuration confTypeFilter = new Configuration();
		final TypeFilter typeFilter = new TypeFilter(confTypeFilter, analysisController);

		// TeeFilter
		final Configuration confTeeFilter1 = new Configuration();
		confTeeFilter1.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
		// confTeeFilter1.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_NULL);
		final TeeFilter teeFilter1 = new TeeFilter(confTeeFilter1, analysisController);

		final Configuration confTeeFilter2 = new Configuration();
		confTeeFilter2.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDERR);
		// confTeeFilter2.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_NULL);
		confTeeFilter2.setProperty(AbstractAnalysisComponent.CONFIG_NAME, "CountBegin");
		final TeeFilter teeFilter2 = new TeeFilter(confTeeFilter2, analysisController);

		final Configuration confTeeFilter3 = new Configuration();
		confTeeFilter3.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDERR);
		// confTeeFilter3.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_NULL);
		confTeeFilter3.setProperty(AbstractAnalysisComponent.CONFIG_NAME, "CountAfter");
		final TeeFilter teeFilter3 = new TeeFilter(confTeeFilter3, analysisController);

		// CountingFilter
		final Configuration confCountingFilter1 = new Configuration();
		final CountingFilter countingFilter1 = new CountingFilter(confCountingFilter1, analysisController);

		final Configuration confCountingFilter2 = new Configuration();
		final CountingFilter countingFilter2 = new CountingFilter(confCountingFilter2, analysisController);

		final Configuration confTimestampFilter = new Configuration();
		final TimestampFilter timestampFilter = new TimestampFilter(confTimestampFilter, analysisController);

		// Filter
		final Configuration confTraceIdFilter = new Configuration();
		final TraceIdFilter traceIdFilter = new TraceIdFilter(confTraceIdFilter, analysisController);

		final Configuration confEventTraceReconstructionFilter = new Configuration();
		final EventRecordTraceReconstructionFilter eventTraceReconstructionFilter =
				new EventRecordTraceReconstructionFilter(confEventTraceReconstructionFilter, analysisController);

		final Configuration confTraceEvents2ExecutionAndMessageTraceFilter = new Configuration();
		final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter =
				new TraceEventRecords2ExecutionAndMessageTraceFilter(confTraceEvents2ExecutionAndMessageTraceFilter, analysisController);

		// Visualization
		final Configuration confSequenceDiagramFilter = new Configuration();
		confSequenceDiagramFilter.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE, "tmp/SequenceAssembly");
		final SequenceDiagramFilter sequenceDiagramFilter = new SequenceDiagramFilter(confSequenceDiagramFilter, analysisController);

		final ComponentDependencyGraphAllocationFilter componentDependencyGraphAllocationFilter =
				new ComponentDependencyGraphAllocationFilter(new Configuration(), analysisController);

		final Configuration componentAllocationWriterConfiguration = new Configuration();
		componentAllocationWriterConfiguration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, "tmp/");
		componentAllocationWriterConfiguration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME, "dependency.dot");
		final GraphWriterPlugin componentAllocationGraphWriter = new GraphWriterPlugin(componentAllocationWriterConfiguration, analysisController);

		final OperationDependencyGraphAllocationFilter operationDependencyGraphAllocationFilter =
				new OperationDependencyGraphAllocationFilter(new Configuration(), analysisController);

		final Configuration operationAllocationWriterConfiguration = new Configuration();
		operationAllocationWriterConfiguration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, "tmp/");
		operationAllocationWriterConfiguration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME, "dependency-operation.dot");
		final GraphWriterPlugin operationAllocationGraphWriter = new GraphWriterPlugin(operationAllocationWriterConfiguration, analysisController);

		// Visualization
		final Configuration confSystemModel2FileFilter = new Configuration();
		confSystemModel2FileFilter.setProperty(SystemModel2FileFilter.CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN, "tmp/system-model.html");
		final SystemModel2FileFilter systemModel2FileFilter = new SystemModel2FileFilter(confSystemModel2FileFilter, analysisController);

		try { // connect everything
			analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, buffer, StringBufferFilter.INPUT_PORT_NAME_EVENTS);

			analysisController.connect(buffer, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, countingFilter1, CountingFilter.INPUT_PORT_NAME_EVENTS);

			analysisController.connect(countingFilter1, CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, typeFilter, TypeFilter.INPUT_PORT_NAME_EVENTS);

			analysisController.connect(typeFilter, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH, timestampFilter, TimestampFilter.INPUT_PORT_NAME_FLOW);

			analysisController.connect(countingFilter1, CountingFilter.OUTPUT_PORT_NAME_COUNT, teeFilter2, TeeFilter.INPUT_PORT_NAME_EVENTS);

			analysisController.connect(timestampFilter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, traceIdFilter, TraceIdFilter.INPUT_PORT_NAME_COMBINED);

			analysisController.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, countingFilter2, CountingFilter.INPUT_PORT_NAME_EVENTS);

			analysisController.connect(countingFilter2, CountingFilter.OUTPUT_PORT_NAME_COUNT, teeFilter3, TeeFilter.INPUT_PORT_NAME_EVENTS);

			analysisController.connect(
					countingFilter2, CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, eventTraceReconstructionFilter,
					EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);

			analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, traceRepo);
			analysisController.connect(
					eventTraceReconstructionFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
					traceEvents2ExecutionAndMessageTraceFilter, TraceEventRecords2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME_EVENT_TRACE);

			analysisController.connect(
					traceEvents2ExecutionAndMessageTraceFilter, TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
					teeFilter1, TeeFilter.INPUT_PORT_NAME_EVENTS);

			analysisController.connect(sequenceDiagramFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, traceRepo);
			analysisController.connect(
					teeFilter1, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
					sequenceDiagramFilter, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);

			analysisController.connect(componentDependencyGraphAllocationFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, traceRepo);
			analysisController.connect(
					teeFilter1, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
					componentDependencyGraphAllocationFilter, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);

			analysisController.connect(operationDependencyGraphAllocationFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, traceRepo);
			analysisController.connect(
					teeFilter1, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
					operationDependencyGraphAllocationFilter, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);

			analysisController.connect(
					componentDependencyGraphAllocationFilter, IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH,
					componentAllocationGraphWriter, GraphWriterPlugin.INPUT_PORT_NAME_GRAPHS);

			analysisController.connect(
					operationDependencyGraphAllocationFilter, IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH,
					operationAllocationGraphWriter, GraphWriterPlugin.INPUT_PORT_NAME_GRAPHS);

			analysisController.connect(systemModel2FileFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, traceRepo);

		} catch (final AnalysisConfigurationException ex) {
			TestAnalysis.LOG.error("Failed to wire the example project.", ex);
		}
		try {
			analysisController.saveToFile(new File(TestAnalysis.KAX_FILENAME));
		} catch (final IOException ex) {
			TestAnalysis.LOG.error("Failed to save configuration to " + TestAnalysis.KAX_FILENAME, ex);
		} catch (final AnalysisConfigurationException ex) {
			TestAnalysis.LOG.error("Failed to save configuration to " + TestAnalysis.KAX_FILENAME, ex);
		}
	}
}
