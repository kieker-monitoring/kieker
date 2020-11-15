/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter.traceWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.2
 */
@Plugin(description = "A filter allowing to write the incoming MessageTraces into a configured file", repositoryPorts = {
	@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
}, configuration = {
	@Property(name = MessageTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, defaultValue = "messageTraces-yyyyMMdd-HHmmssSSS.txt")
})
public class MessageTraceWriterFilter extends AbstractMessageTraceProcessingFilter {

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
	 *             If either the default encoding is not supported or the given file is somehow invalid.
	 */
	public MessageTraceWriterFilter(final Configuration configuration, final IProjectContext projectContext) throws IOException {
		super(configuration, projectContext);

		this.outputFn = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FN);
		this.ps = new PrintStream(new FileOutputStream(this.outputFn), false, ENCODING);
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		LOGGER.debug("Wrote {} trace{} to file '{}'", numTraces, (numTraces > 1 ? "s" : ""), this.outputFn); // NOCS
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
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = super.getCurrentConfiguration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FN, this.outputFn);

		return configuration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@InputPort(name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES, description = "Receives message traces to be processed",
			eventTypes = MessageTrace.class)
	public void inputMessageTraces(final MessageTrace mt) {
		MessageTraceWriterFilter.this.ps.println(mt.toString());
		MessageTraceWriterFilter.this.reportSuccess(mt.getTraceId());
	}
}
