/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.systemModel;

import java.io.File;
import java.io.IOException;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Writes the contents of a connected {@link SystemModelRepository} to files.
 * Currently, only HTML output is supported.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.5
 */
@Plugin(
		description = "Prints the contents of a connected SystemModelRepository to an HTML file",
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
		},
		configuration = {
			@Property(name = SystemModel2FileFilter.CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN, defaultValue = SystemModel2FileFilter.DEFAULT_HTML_OUTPUT_FN)
		})
public class SystemModel2FileFilter extends AbstractTraceAnalysisFilter {
	/**
	 * Name of the configuration property to pass the filename of the HTML output.
	 */
	public static final String CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN = "outputFnHtml";

	/**
	 * By default, writes HTML output file to this file in the working directory.
	 */
	protected static final String DEFAULT_HTML_OUTPUT_FN = "system-model.html";

	private final String outputFnHTML;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public SystemModel2FileFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.outputFnHTML = configuration.getPathProperty(CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration currentConfiguration = super.getCurrentConfiguration();

		currentConfiguration.setProperty(CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN, this.outputFnHTML);

		return currentConfiguration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean errorBeforeTermination) {
		String outputFnHTMLCanonical = this.outputFnHTML; // not yet canonical here

		/**
		 * Used to keep track of whether an error occurred, regardless
		 * of whether before or during termination.
		 */
		boolean error = errorBeforeTermination;
		if (!error) {
			try {
				// Trying to create the canonical file path here. Using a code block to hide the File.
				final File outputFileHTML = new File(this.outputFnHTML);
				outputFnHTMLCanonical = outputFileHTML.getCanonicalPath(); // may throw IOExecption

				final SystemModelRepository sysModelRepo = super.getSystemEntityFactory();
				if (sysModelRepo == null) {
					final String errorMsg = "Failed to get system model repository";
					this.log.error(errorMsg);
					error = true;
				} else {
					sysModelRepo.saveSystemToHTMLFile(outputFnHTMLCanonical);
				}
			} catch (final IOException e) {
				final String errorMsg = "Failed to save system model to file " + outputFnHTMLCanonical;
				this.log.error(errorMsg, e);
				error = true;
			}
		}

		if (!error) {
			this.printDebugLogMessage(new String[] { "Wrote HTML output of system model to file '" + outputFnHTMLCanonical + "'" });
		} else {
			this.printErrorLogMessage(new String[] { "Failed to write HTML output of system model to file '" + outputFnHTMLCanonical + "'" });
		}
	}
}
