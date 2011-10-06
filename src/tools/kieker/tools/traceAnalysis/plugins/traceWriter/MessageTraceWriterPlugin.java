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

package kieker.tools.traceAnalysis.plugins.traceWriter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class MessageTraceWriterPlugin extends AbstractMessageTraceProcessingPlugin {

	private static final Log LOG = LogFactory.getLog(MessageTraceWriterPlugin.class);
	private final String outputFn;
	private final BufferedWriter ps;

	public MessageTraceWriterPlugin(final String name, final SystemModelRepository systemEntityFactory, final String outputFn) throws FileNotFoundException,
			IOException {
		super(name, systemEntityFactory);
		this.outputFn = outputFn;
		this.ps = new BufferedWriter(new FileWriter(outputFn));
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		System.out.println("Wrote " + numTraces + " trace" + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn + "'"); // NOCS
	}

	@Override
	public void terminate(final boolean error) {
		if (this.ps != null) {
			try {
				this.ps.close();
			} catch (final IOException ex) {
				MessageTraceWriterPlugin.LOG.error("IOException", ex);
			}
		}
	}

	@Override
	public boolean execute() {
		return true; // no need to do anything here
	}

	@Override
	public IInputPort<MessageTrace> getMessageTraceInputPort() {
		return this.messageTraceInputPort;
	}

	private final IInputPort<MessageTrace> messageTraceInputPort = new AbstractInputPort<MessageTrace>("Message traces") {

		@Override
		public void newEvent(final MessageTrace mt) {
			try {
				MessageTraceWriterPlugin.this.ps.append(mt.toString());
				MessageTraceWriterPlugin.this.reportSuccess(mt.getTraceId());
			} catch (final IOException ex) {
				MessageTraceWriterPlugin.LOG.error("IOException", ex);
				MessageTraceWriterPlugin.this.reportError(mt.getTraceId());
			}
		}
	};
}
