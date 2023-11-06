/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import kieker.analysis.trace.AbstractMessageTraceProcessingFilter;
import kieker.model.repository.AbstractRepository;
import kieker.model.repository.AllocationComponentOperationPairFactory;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.Operation;
import kieker.model.system.model.SynchronousCallMessage;
import kieker.model.system.model.util.AllocationComponentOperationPair;
import kieker.tools.trace.analysis.filter.traceReconstruction.TraceProcessingException;
import kieker.tools.trace.analysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.visualization.trace.call.tree.AbstractCallTreeFilter.IPairFactory;

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
 */
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
	 */
	public TraceCallTreeFilter(final SystemModelRepository repository, final boolean shortLabels, final String dotOutputFn) {
		super(repository);

		// Initialize the fields based on the given parameters. */
		this.shortLabels = shortLabels;
		this.dotOutputFn = dotOutputFn;
	}

	@Override
	public void printStatusMessage() {
		synchronized (this) {
			super.printStatusMessage();
			final int numPlots = this.getSuccessCount();
			final long lastSuccessTracesId = this.getLastTraceIdSuccess();
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Wrote " + numPlots + " call tree" + (numPlots > 1 ? "s" : "") + " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" // NOCS
						+ this.dotOutputFn + "-<traceId>.dot'");
				this.logger.debug("Dot files can be converted using the dot tool");
				this.logger.debug("Example: dot -T svg " + this.dotOutputFn + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".dot > " // NOCS
						+ this.dotOutputFn + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg"); // NOCS
			}
		}
	}

	@Override
	protected void execute(final MessageTrace mt) throws Exception {
		try {
			final TraceCallTreeNode rootNode = new TraceCallTreeNode(AbstractRepository.ROOT_ELEMENT_ID, AllocationComponentOperationPairFactory.ROOT_PAIR,
					true, mt,
					NoOriginRetentionPolicy.createInstance()); // rootNode
			AbstractCallTreeFilter.writeDotForMessageTrace(rootNode, new IPairFactory<AllocationComponentOperationPair>() {

				@Override
				public AllocationComponentOperationPair createPair(final SynchronousCallMessage callMsg) {
					final AllocationComponent allocationComponent = callMsg.getReceivingExecution().getAllocationComponent();
					final Operation op = callMsg.getReceivingExecution().getOperation();
					final AllocationComponentOperationPair destination = TraceCallTreeFilter.this.getSystemModelRepository().getAllocationPairFactory()
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
