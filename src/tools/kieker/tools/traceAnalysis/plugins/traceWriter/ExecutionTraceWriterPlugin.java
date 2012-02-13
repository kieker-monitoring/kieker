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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.plugin.port.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractExecutionTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class ExecutionTraceWriterPlugin extends AbstractExecutionTraceProcessingPlugin {

	public static final String CONFIG_OUTPUT_FN = ExecutionTraceWriterPlugin.class.getName() + ".outputFn";
	public static final String EXECUTION_TRACES_INPUT_PORT_NAME = "newEvent";
	private static final Log LOG = LogFactory.getLog(ExecutionTraceWriterPlugin.class);

	private static final String ENCODING = "UTF-8";

	private final String outputFn;
	private final BufferedWriter ps;

	public ExecutionTraceWriterPlugin(final Configuration configuration) throws IOException {
		super(configuration);
		this.outputFn = configuration.getStringProperty(ExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN);
		this.ps = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputFn), ExecutionTraceWriterPlugin.ENCODING));
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
	public String getExecutionTraceInputPortName() {
		return ExecutionTraceWriterPlugin.EXECUTION_TRACES_INPUT_PORT_NAME;
	}

	@InputPort(
			name = ExecutionTraceWriterPlugin.EXECUTION_TRACES_INPUT_PORT_NAME,
			description = "Execution traces",
			eventTypes = { ExecutionTrace.class })
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

		configuration.setProperty(ExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, "");

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(ExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, this.outputFn);

		return configuration;
	}
}
