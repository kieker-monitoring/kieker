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
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public abstract class AggregatedCallTreeFilter<T> extends AbstractCallTreeFilter<T> {
	private static final Log LOG = LogFactory.getLog(AggregatedCallTreeFilter.class);

	public static final String CONFIG_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_SHORT_LABELS = "shortLabels";

	private AbstractAggregatedCallTreeNode<T> root;
	private File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private int numGraphsSaved = 0;

	public AggregatedCallTreeFilter(final Configuration configuration) {
		super(configuration);

		this.includeWeights = configuration.getBooleanProperty(AggregatedCallTreeFilter.CONFIG_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(AggregatedCallTreeFilter.CONFIG_SHORT_LABELS);
	}

	public void setRoot(final AbstractAggregatedCallTreeNode<T> root) {
		this.root = root;
	}

	public void setDotOutputFile(final File dotOutputFile) {
		this.dotOutputFile = dotOutputFile;
	}

	public void saveTreeToDotFile() throws IOException {
		final String outputFnBase = this.dotOutputFile.getCanonicalPath();
		AbstractCallTreeFilter.saveTreeToDotFile(this.root, outputFnBase, this.includeWeights, false, // do not include EOIs
				this.shortLabels);
		this.numGraphsSaved++;
		this.printMessage(new String[] { "Wrote call tree to file '" + outputFnBase + ".dot" + "'", "Dot file can be converted using the dot tool",
			"Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg", });
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		this.stdOutPrintln("Saved " + this.numGraphsSaved + " call tree" + (this.numGraphsSaved > 1 ? "s" : "")); // NOCS
	}

	/**
	 * Saves the call tree to the dot file if error is not true.
	 * 
	 * @param error
	 */

	@Override
	public void terminate(final boolean error) {
		if (!error) {
			try {
				this.saveTreeToDotFile();
			} catch (final IOException ex) {
				AggregatedCallTreeFilter.LOG.error("IOException while saving to dot file", ex);
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		// TODO: Save the current configuration

		return configuration;
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME,
			description = "Message traces",
			eventTypes = { MessageTrace.class })
	public void msgTraceInput(final MessageTrace t) {
		try {
			AbstractCallTreeFilter.addTraceToTree(this.root, t, new PairFactory() {

				public Object createPair(final SynchronousCallMessage callMsg) {
					return AggregatedCallTreeFilter.this.concreteCreatePair(callMsg);
				}
			}, true); // aggregated
			AggregatedCallTreeFilter.this.reportSuccess(t.getTraceId());
		} catch (final TraceProcessingException ex) {
			AggregatedCallTreeFilter.LOG.error("TraceProcessingException", ex);
			AggregatedCallTreeFilter.this.reportError(t.getTraceId());
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
