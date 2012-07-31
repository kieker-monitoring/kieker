/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.traceAnalysis.filter.visualization.callTree;

import java.io.File;
import java.io.IOException;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceProcessingException;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public abstract class AbstractAggregatedCallTreeFilter<T> extends AbstractCallTreeFilter<T> {
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FILENAME = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";

	public static final String CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT = "calltree.dot";
	public static final String CONFIG_PROPERTY_VALUE_INCLUDE_WEIGHTS_DEFAULT = Boolean.TRUE.toString();
	public static final String CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT = Boolean.TRUE.toString();

	private static final Log LOG = LogFactory.getLog(AbstractAggregatedCallTreeFilter.class);

	private volatile AbstractAggregatedCallTreeNode<T> root;
	private final String dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private int numGraphsSaved = 0; // no need for volatile, only used in synchronized blocks

	public AbstractAggregatedCallTreeFilter(final Configuration configuration) {
		super(configuration);

		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.dotOutputFile = configuration.getProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME);
	}

	protected void setRoot(final AbstractAggregatedCallTreeNode<T> root) {
		synchronized (this) {
			this.root = root;
		}
	}

	public void saveTreeToDotFile() throws IOException {
		synchronized (this) {
			final String outputFn = new File(this.dotOutputFile).getCanonicalPath();
			AbstractCallTreeFilter.saveTreeToDotFile(this.root, outputFn, this.includeWeights, false, // do not include EOIs
					this.shortLabels);
			this.numGraphsSaved++;
			this.printMessage(new String[] { "Wrote call tree to file '" + outputFn + "'", "Dot file can be converted using the dot tool",
				"Example: dot -T svg " + outputFn + " > " + outputFn + ".svg", });
		}
	}

	@Override
	public void printStatusMessage() {
		synchronized (this) {
			super.printStatusMessage();
			this.stdOutPrintln("Saved " + this.numGraphsSaved + " call tree" + (this.numGraphsSaved > 1 ? "s" : "")); // NOCS
		}
	}

	/**
	 * Saves the call tree to the dot file if error is not true.
	 * 
	 * @param error
	 */

	@Override
	public void terminate(final boolean error) {
		synchronized (this) {
			if (!error) {
				try {
					this.saveTreeToDotFile();
				} catch (final IOException ex) {
					LOG.error("IOException while saving to dot file", ex);
				}
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, CONFIG_PROPERTY_VALUE_INCLUDE_WEIGHTS_DEFAULT);
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT);
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT);
		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(this.shortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, this.dotOutputFile);
		return configuration;
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES,
			description = "Receives the message traces to be processed",
			eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		synchronized (this) {
			try {
				AbstractCallTreeFilter.addTraceToTree(this.root, t, new IPairFactory() {

					public Object createPair(final SynchronousCallMessage callMsg) {
						return AbstractAggregatedCallTreeFilter.this.concreteCreatePair(callMsg);
					}
				}, true); // aggregated
				AbstractAggregatedCallTreeFilter.this.reportSuccess(t.getTraceId());
			} catch (final TraceProcessingException ex) {
				LOG.error("TraceProcessingException", ex);
				AbstractAggregatedCallTreeFilter.this.reportError(t.getTraceId());
			}
		}
	}

	/**
	 * HACK
	 * 
	 * @param callMsg
	 * @return
	 */
	protected abstract Object concreteCreatePair(SynchronousCallMessage callMsg);

}
