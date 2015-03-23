/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.traceAnalysis.filter.AbstractInvalidExecutionTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.2
 */
@Plugin(description = "A filter allowing to write the incoming InvalidExecutionTraces into a configured file",
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
		},
		configuration = {
			@Property(name = InvalidExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, defaultValue = "invalidTraceArtifacts-yyyyMMdd-HHmmssSSS.txt")
		})
public class InvalidExecutionTraceWriterFilter extends AbstractInvalidExecutionTraceProcessingFilter {

	/** This is the name of the input port receiving new (invalid) execution traces. */
	public static final String INPUT_PORT_NAME_INVALID_EXECUTION_TRACES = "invalidExecutionTraces";
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
	public InvalidExecutionTraceWriterFilter(final Configuration configuration, final IProjectContext projectContext) throws IOException {
		super(configuration, projectContext);

		this.outputFn = configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FN);
		this.ps = new PrintStream(new FileOutputStream(this.outputFn), false, ENCODING);
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Wrote " + numTraces + " execution trace artifact" + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn + "'"); // NOCS
		}
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

	@Override
	public String getInvalidExecutionTraceInputPortName() {
		return INPUT_PORT_NAME_INVALID_EXECUTION_TRACES;
	}

	/**
	 * This method represents the input port of this filter.
	 * 
	 * @param et
	 *            The next execution trace.
	 */
	@InputPort(
			name = INPUT_PORT_NAME_INVALID_EXECUTION_TRACES,
			description = "Receives the invalid execution traces to be written", eventTypes = { InvalidExecutionTrace.class })
	public void newInvalidExecutionTrace(final InvalidExecutionTrace et) {
		InvalidExecutionTraceWriterFilter.this.ps.println(et.getInvalidExecutionTraceArtifacts().toString());
		InvalidExecutionTraceWriterFilter.this.reportSuccess(et.getInvalidExecutionTraceArtifacts().getTraceId());
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
