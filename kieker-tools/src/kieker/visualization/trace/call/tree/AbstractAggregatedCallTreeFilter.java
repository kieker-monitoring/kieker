/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace.call.tree;

import java.io.File;
import java.io.IOException;

import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.SynchronousCallMessage;
import kieker.tools.trace.analysis.filter.traceReconstruction.TraceProcessingException;

/**
 * This class has exactly one input port named "in". The data which is send to this plugin is not delegated in any way.
 *
 * @param <T>
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
public abstract class AbstractAggregatedCallTreeFilter<T> extends AbstractCallTreeFilter<T> {

	private volatile AbstractAggregatedCallTreeNode<T> root;
	private final String dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private int numGraphsSaved; // no need for volatile, only used in synchronized blocks

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 *            system model repository
	 * @param includeWeights
	 *            include weights ingraph
	 * @param shortLabels
	 *            use short labels
	 * @param dotOutputFile
	 *            output file name
	 */
	public AbstractAggregatedCallTreeFilter(final SystemModelRepository repository, final boolean includeWeights, final boolean shortLabels,
			final String dotOutputFile) {
		super(repository);

		this.includeWeights = includeWeights;
		this.shortLabels = shortLabels;
		this.dotOutputFile = dotOutputFile;
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
			this.logger.debug("Saved {} call tree{}", this.numGraphsSaved, (this.numGraphsSaved > 1 ? "s" : "")); // NOCS
		}
	}

	/**
	 * Saves the call tree to the dot file if error is not true.
	 */
	@Override
	public void onTerminating() {
		synchronized (this) {
			try {
				this.saveTreeToDotFile();
			} catch (final IOException ex) {
				this.logger.error("IOException while saving to dot file", ex);
			}
		}
		super.onTerminating();
	}

	@Override
	protected void execute(final MessageTrace trace) throws Exception {
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
