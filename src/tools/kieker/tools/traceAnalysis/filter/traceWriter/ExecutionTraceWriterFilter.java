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

package kieker.tools.traceAnalysis.filter.traceWriter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractExecutionTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public class ExecutionTraceWriterFilter extends AbstractExecutionTraceProcessingFilter {

	public static final String INPUT_PORT_NAME_EXECUTION_TRACES = "executionTraces";

	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FN = "outputFn";

	private static final Log LOG = LogFactory.getLog(ExecutionTraceWriterFilter.class);

	private static final String ENCODING = "UTF-8";

	private final String outputFn;
	private final BufferedWriter ps;

	public ExecutionTraceWriterFilter(final Configuration configuration) throws IOException {
		super(configuration);
		this.outputFn = configuration.getStringProperty(ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN);
		this.ps = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputFn), ExecutionTraceWriterFilter.ENCODING));
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		this.stdOutPrintln("Wrote " + numTraces + " execution trace" + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn + "'"); // NOCS
	}

	@Override
	public void terminate(final boolean error) {
		if (this.ps != null) {
			try {
				this.ps.close();
			} catch (final IOException ex) {
				ExecutionTraceWriterFilter.LOG.error("IOException while terminating", ex);
			}
		}
	}

	@Override
	public String getExecutionTraceInputPortName() {
		return ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES;
	}

	@InputPort(
			name = ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES,
			description = "Receives the execution traces to be written",
			eventTypes = { ExecutionTrace.class })
	public void newExecutionTrace(final ExecutionTrace et) {
		try {
			ExecutionTraceWriterFilter.this.ps.append(et.toString()).append("\n");
			ExecutionTraceWriterFilter.this.reportSuccess(et.getTraceId());
		} catch (final IOException ex) {
			ExecutionTraceWriterFilter.this.reportError(et.getTraceId());
			ExecutionTraceWriterFilter.LOG.error("IOException", ex);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, "");

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, this.outputFn);

		return configuration;
	}
}
