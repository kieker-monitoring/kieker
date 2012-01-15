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
import java.io.FileWriter;
import java.io.IOException;

import kieker.analysis.plugin.port.InputPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractExecutionTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractRepository;

/**
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn
 */
public class ExecutionTraceWriterPlugin extends AbstractExecutionTraceProcessingPlugin {

	public static final String CONFIG_OUTPUT_FN = ExecutionTraceWriterPlugin.class.getName() + ".outputFn";
	public static final String EXECUTION_TRACES_INPUT_PORT_NAME = "newEvent";
	private static final Log LOG = LogFactory.getLog(ExecutionTraceWriterPlugin.class);
	private final String outputFn;
	private final BufferedWriter ps;

	public ExecutionTraceWriterPlugin(final Configuration configuration, final AbstractRepository repositories[]) throws IOException {
		super(configuration, repositories);
		this.outputFn = configuration.getStringProperty(ExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN);
		this.ps = new BufferedWriter(new FileWriter(this.outputFn));
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		System.out.println("Wrote " + numTraces + " execution trace" + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn + "'"); // NOCS
	}

	@Override
	public void terminate(final boolean error) {
		if (this.ps != null) {
			try {
				this.ps.close();
			} catch (final IOException ex) {
				ExecutionTraceWriterPlugin.LOG.error("IOException", ex);
			}
		}
	}

	@Override
	public boolean execute() {
		return true; // no need to do anything here
	}

	@Override
	public String getExecutionTraceInputPortName() {
		return ExecutionTraceWriterPlugin.EXECUTION_TRACES_INPUT_PORT_NAME;
	}

	@InputPort(description = "Execution traces", eventTypes = { ExecutionTrace.class })
	public void newEvent(final Object obj) {
		final ExecutionTrace et = (ExecutionTrace) obj;
		try {
			ExecutionTraceWriterPlugin.this.ps.append(et.toString());
			ExecutionTraceWriterPlugin.this.reportSuccess(et.getTraceId());
		} catch (final IOException ex) {
			ExecutionTraceWriterPlugin.this.reportError(et.getTraceId());
			ExecutionTraceWriterPlugin.LOG.error("", ex);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.put(ExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, "");

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.put(ExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, this.outputFn);

		return configuration;
	}

	@Override
	protected AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}
}
