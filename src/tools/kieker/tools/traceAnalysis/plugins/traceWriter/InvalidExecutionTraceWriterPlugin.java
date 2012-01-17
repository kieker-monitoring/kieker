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
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractInvalidExecutionTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;

/**
 * 
 * @author Andre van Hoorn
 */
public class InvalidExecutionTraceWriterPlugin extends AbstractInvalidExecutionTraceProcessingPlugin {

	public static final String CONFIG_OUTPUT_FN = InvalidExecutionTraceWriterPlugin.class.getName() + ".outputFn";
	public static final String INVALID_EXECUTION_TRACES_INPUT_PORT_NAME = "newEvent";
	private static final Log LOG = LogFactory.getLog(InvalidExecutionTraceWriterPlugin.class);
	private final String outputFn;
	private final BufferedWriter ps;

	public InvalidExecutionTraceWriterPlugin(final Configuration configuration, final AbstractRepository repositories[])
			throws IOException {
		super(configuration, repositories);
		this.outputFn = configuration.getStringProperty(InvalidExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN);
		this.ps = new BufferedWriter(new FileWriter(this.outputFn));
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		System.out.println("Wrote " + numTraces + " execution trace artifact" + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn + "'"); // NOCS
	}

	@Override
	public void terminate(final boolean error) {
		if (this.ps != null) {
			try {
				this.ps.close();
			} catch (final IOException ex) {
				InvalidExecutionTraceWriterPlugin.LOG.error("IOException", ex);
			}
		}
	}

	@Override
	public boolean execute() {
		return true; // no need to do anything here
	}

	@Override
	public String getInvalidExecutionTraceInputPortName() {
		return InvalidExecutionTraceWriterPlugin.INVALID_EXECUTION_TRACES_INPUT_PORT_NAME;
	}

	@InputPort(description = "Invalid Execution traces", eventTypes = { InvalidExecutionTrace.class })
	public void newEvent(final Object obj) {
		final InvalidExecutionTrace et = (InvalidExecutionTrace) obj;
		try {
			InvalidExecutionTraceWriterPlugin.this.ps.append(et.getInvalidExecutionTraceArtifacts().toString());
			InvalidExecutionTraceWriterPlugin.this.reportSuccess(et.getInvalidExecutionTraceArtifacts().getTraceId());
		} catch (final IOException ex) {
			InvalidExecutionTraceWriterPlugin.this.reportError(et.getInvalidExecutionTraceArtifacts().getTraceId());
			InvalidExecutionTraceWriterPlugin.LOG.error("", ex);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.put(InvalidExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, "");

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.put(InvalidExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, this.outputFn);

		return configuration;
	}

	@Override
	protected AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}
}
