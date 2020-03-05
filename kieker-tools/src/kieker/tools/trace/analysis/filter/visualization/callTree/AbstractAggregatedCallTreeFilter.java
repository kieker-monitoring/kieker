/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.io.File;
import java.io.IOException;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.traceReconstruction.TraceProcessingException;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.SynchronousCallMessage;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * This class has exactly one input port named "in". The data which is send to this plugin is not delegated in any way.
 *
 * @param <T>
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
@Plugin(description = "An abstract filter for aggregated call trees", repositoryPorts = {
	@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class) },
		configuration = {
			@Property(name = AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS,
					defaultValue = AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_VALUE_INCLUDE_WEIGHTS_DEFAULT),
			@Property(name = AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
					defaultValue = AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT),
			@Property(name = AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME,
					defaultValue = AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT) })
public abstract class AbstractAggregatedCallTreeFilter<T> extends AbstractCallTreeFilter<T> {

	/** The name of the configuration determining the dot output file name. */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FILENAME = "dotOutputFn";
	/** The name of the configuration determining whether to include weights or not. */
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	/** The name of the configuration determining whether to use short labels in the call tree or not. */
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";
	/** The default used output file name. */
	public static final String CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT = "calltree.dot";
	/** The default used value determining whether to include weights or not. */
	public static final String CONFIG_PROPERTY_VALUE_INCLUDE_WEIGHTS_DEFAULT = "true";
	/** The default used value determining whether to use short labels in the call tree or not. */
	public static final String CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT = "true";

	private volatile AbstractAggregatedCallTreeNode<T> root;
	private final String dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private int numGraphsSaved; // no need for volatile, only used in synchronized blocks

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AbstractAggregatedCallTreeFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.dotOutputFile = configuration.getPathProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME);
	}

	/**
	 * Sets the root of the call tree.
	 *
	 * @param root
	 *            The new root.
	 */
	protected void setRoot(final AbstractAggregatedCallTreeNode<T> root) {
		synchronized (this) {
			this.root = root;
		}
	}

	/**
	 * This method tries to convert the current tree into the specified file as a valid dot file, which can later be
	 * transformed into a visual representation by dot itself.
	 *
	 * @throws IOException
	 *             If something went wrong during the converting.
	 */
	public void saveTreeToDotFile() throws IOException {
		synchronized (this) {
			final String outputFn = new File(this.dotOutputFile).getCanonicalPath();
			AbstractCallTreeFilter.saveTreeToDotFile(this.root, outputFn, this.includeWeights, false, // do not include
					// EOIs
					this.shortLabels);
			this.numGraphsSaved++;
			this.printDebugLogMessage(new String[] { "Wrote call tree to file '" + outputFn + "'",
				"Dot file can be converted using the dot tool",
				"Example: dot -T svg " + outputFn + " > " + outputFn + ".svg", });
		}
	}

	@Override
	public void printStatusMessage() {
		synchronized (this) {
			super.printStatusMessage();
			LOGGER.debug("Saved {} call tree{}", this.numGraphsSaved, (this.numGraphsSaved > 1 ? "s" : "")); // NOCS
		}
	}

	/**
	 * Saves the call tree to the dot file if error is not true.
	 *
	 * @param error
	 *            Determines whether the plugin terminated due to an error or not.
	 */
	@Override
	public void terminate(final boolean error) {
		synchronized (this) {
			if (!error) {
				try {
					this.saveTreeToDotFile();
				} catch (final IOException ex) {
					this.logger.error("IOException while saving to dot file", ex);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = super.getCurrentConfiguration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(this.shortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, this.dotOutputFile);

		return configuration;
	}

	@Override
	@InputPort(name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES, description = "Receives the message traces to be processed",
			eventTypes = {
				MessageTrace.class })
	public void inputMessageTraces(final MessageTrace trace) {
		synchronized (this) {
			try {
				final SynchronousCallMessagePairFactory<T> pairFactory = new SynchronousCallMessagePairFactory<>(this);

				AbstractCallTreeFilter.addTraceToTree(this.root, trace, pairFactory, true); // aggregated
				AbstractAggregatedCallTreeFilter.this.reportSuccess(trace.getTraceId());
			} catch (final TraceProcessingException ex) {
				this.logger.error("TraceProcessingException", ex);
				AbstractAggregatedCallTreeFilter.this.reportError(trace.getTraceId());
			}
		}
	}

	/**
	 * HACK. Inheriting classes should implement this method to deliver the actual pair.
	 *
	 * @param callMsg
	 *            The call message which contains the information necessary to create the pair.
	 *
	 * @return The actual pair.
	 */
	protected abstract T concreteCreatePair(SynchronousCallMessage callMsg);

	/**
	 * @author Christian Wulf
	 *
	 * @param <T>
	 */
	private static class SynchronousCallMessagePairFactory<T> implements IPairFactory<T> {

		private final AbstractAggregatedCallTreeFilter<T> filter;

		public SynchronousCallMessagePairFactory(final AbstractAggregatedCallTreeFilter<T> filter) {
			this.filter = filter;
		}

		@Override
		public T createPair(final SynchronousCallMessage callMsg) {
			return this.filter.concreteCreatePair(callMsg);
		}
	}
}
