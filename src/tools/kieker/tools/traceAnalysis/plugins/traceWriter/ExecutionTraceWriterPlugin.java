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
import kieker.tools.traceAnalysis.plugins.AbstractExecutionTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class ExecutionTraceWriterPlugin extends AbstractExecutionTraceProcessingPlugin {

	private static final Log log = LogFactory.getLog(ExecutionTraceWriterPlugin.class);
	private final String outputFn;
	private final BufferedWriter ps;

	public ExecutionTraceWriterPlugin(final String name, final SystemModelRepository systemEntityFactory, final String outputFn) throws FileNotFoundException,
			IOException {
		super(name, systemEntityFactory);
		this.outputFn = outputFn;
		this.ps = new BufferedWriter(new FileWriter(outputFn));
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = getSuccessCount();
		System.out.println("Wrote " + numTraces + " execution trace" + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn + "'");
	}

	@Override
	public void terminate(final boolean error) {
		if (this.ps != null) {
			try {
				this.ps.close();
			} catch (final IOException ex) {
				ExecutionTraceWriterPlugin.log.error("IOException", ex);
			}
		}
	}

	@Override
	public boolean execute() {
		return true; // no need to do anything here
	}

	@Override
	public IInputPort<ExecutionTrace> getExecutionTraceInputPort() {
		return this.executionTraceInputPort;
	}

	private final IInputPort<ExecutionTrace> executionTraceInputPort = new AbstractInputPort<ExecutionTrace>("Execution traces") {

		@Override
		public void newEvent(final ExecutionTrace et) {
			try {
				ExecutionTraceWriterPlugin.this.ps.append(et.toString());
				reportSuccess(et.getTraceId());
			} catch (final IOException ex) {
				reportError(et.getTraceId());
				ExecutionTraceWriterPlugin.log.error(ex, ex);
			}
		}
	};
}
