/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractExecutionTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 *
 * @author Andre van Hoorn
 *
 * @since 1.2
 */
@Plugin(description = "A filter allowing to write the incoming ExecutionTraces into a configured file", repositoryPorts = {
	@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
}, configuration = {
	@Property(name = ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, defaultValue = "invalidTraceArtifacts-yyyyMMdd-HHmmssSSS.txt")
})
public class ExecutionTraceWriterFilter extends AbstractExecutionTraceProcessingFilter {

	/** This is the name of the input port receiving new execution traces. */
	public static final String INPUT_PORT_NAME_EXECUTION_TRACES = "executionTraces";

	/** The name of the configuration determining the output file name. */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FN = "outputFn";

	private static final String ENCODING = "UTF-8";

	private final String outputFn;
	private final PrintStream ps;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 *
	 * @throws IOException
	 *             If the write stream could not be prepared.
	 */
	public ExecutionTraceWriterFilter(final Configuration configuration, final IProjectContext projectContext) throws IOException {
		super(configuration, projectContext);

		this.outputFn = configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FN);
		this.ps = new PrintStream(new FileOutputStream(this.outputFn), false, ENCODING);
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		LOGGER.debug("Wrote {} execution trace{} to file '{}'", numTraces, (numTraces > 1 ? "s" : ""), this.outputFn); // NOCS
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		if (this.ps != null) {
			this.ps.close();
		}
	}

	/**
	 * This method represents the input port of this filter.
	 *
	 * @param et
	 *            The next execution trace.
	 */
	@InputPort(name = INPUT_PORT_NAME_EXECUTION_TRACES, description = "Receives the execution traces to be written", eventTypes = { ExecutionTrace.class })
	public void newExecutionTrace(final ExecutionTrace et) {
		ExecutionTraceWriterFilter.this.ps.println(et.toString());
		ExecutionTraceWriterFilter.this.reportSuccess(et.getTraceId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = super.getCurrentConfiguration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FN, this.outputFn);

		return configuration;
	}
}
