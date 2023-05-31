/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter.visualization.callTree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.traceReconstruction.TraceProcessingException;
import kieker.tools.trace.analysis.filter.visualization.callTree.AbstractCallTreeFilter.IPairFactory;
import kieker.tools.trace.analysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.Operation;
import kieker.tools.trace.analysis.systemModel.SynchronousCallMessage;
import kieker.tools.trace.analysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.trace.analysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;
import kieker.tools.trace.analysis.systemModel.util.AllocationComponentOperationPair;

/**
 * Plugin providing the creation of calling trees both for individual traces
 * and an aggregated form for multiple traces.<br>
 *
 * This class has exactly one input port named "in". The data which is sent to
 * this plugin is not delegated in any way.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
@Plugin(description = "A filter allowing to write the incoming data into a calling tree",
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
		},
		configuration = {
			@Property(name = TraceCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
					defaultValue = TraceCallTreeFilter.CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT),
			@Property(name = TraceCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME,
					defaultValue = TraceCallTreeFilter.CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT)
		})
public class TraceCallTreeFilter extends AbstractMessageTraceProcessingFilter {
	/** This is the name of the property determining the output file name. */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FILENAME = "dotOutputFn";
	/** This is the name of the property determining whether to use short labels or not. */
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";
	/** This is the default used output file name. */
	public static final String CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT = "traceCalltree.dot";
	/** This is the default value whether to use short labels or not. */
	public static final String CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT = "true";

	private final String dotOutputFn;
	private final boolean shortLabels;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TraceCallTreeFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Initialize the fields based on the given parameters. */
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.dotOutputFn = configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME);
	}

	@Override
	public void printStatusMessage() {
		synchronized (this) {
			super.printStatusMessage();
			final int numPlots = this.getSuccessCount();
			final long lastSuccessTracesId = this.getLastTraceIdSuccess();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Wrote " + numPlots + " call tree" + (numPlots > 1 ? "s" : "") + " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" // NOCS
						+ this.dotOutputFn + "-<traceId>.dot'");
				LOGGER.debug("Dot files can be converted using the dot tool");
				LOGGER.debug("Example: dot -T svg " + this.dotOutputFn + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".dot > " // NOCS
						+ this.dotOutputFn + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg"); // NOCS
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = super.getCurrentConfiguration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(this.shortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, this.dotOutputFn);

		return configuration;
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES,
			description = "Receives the message traces to be processed",
			eventTypes = MessageTrace.class)
	public void inputMessageTraces(final MessageTrace mt) throws IOException {
		try {
			final TraceCallTreeNode rootNode = new TraceCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID, AllocationComponentOperationPairFactory.ROOT_PAIR,
					true, mt,
					NoOriginRetentionPolicy.createInstance()); // rootNode
			AbstractCallTreeFilter.writeDotForMessageTrace(rootNode, new IPairFactory<AllocationComponentOperationPair>() {

				@Override
				public AllocationComponentOperationPair createPair(final SynchronousCallMessage callMsg) {
					final AllocationComponent allocationComponent = callMsg.getReceivingExecution().getAllocationComponent();
					final Operation op = callMsg.getReceivingExecution().getOperation();
					final AllocationComponentOperationPair destination = TraceCallTreeFilter.this.getSystemEntityFactory().getAllocationPairFactory()
							.getPairInstanceByPair(allocationComponent, op); // will never be null!
					return destination;
				}
			}, mt, TraceCallTreeFilter.this.dotOutputFn + "-" + mt.getTraceId() + ".dot", false, TraceCallTreeFilter.this.shortLabels); // no weights
			TraceCallTreeFilter.this.reportSuccess(mt.getTraceId());
		} catch (final TraceProcessingException ex) {
			TraceCallTreeFilter.this.reportError(mt.getTraceId());
			this.logger.error("TraceProcessingException", ex);
		} catch (final FileNotFoundException ex) {
			TraceCallTreeFilter.this.reportError(mt.getTraceId());
			this.logger.error("File not found", ex);
		} catch (final UnsupportedEncodingException ex) {
			TraceCallTreeFilter.this.reportError(mt.getTraceId());
			this.logger.error("Encoding not supported", ex);
		}
	}
}
