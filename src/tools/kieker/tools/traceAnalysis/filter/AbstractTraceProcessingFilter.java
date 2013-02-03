/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public abstract class AbstractTraceProcessingFilter extends AbstractTraceAnalysisFilter {

	public static final long MAX_DURATION_MILLIS = Integer.MAX_VALUE;

	private int numTracesProcessed;
	private int numTracesSucceeded;
	private int numTracesFailed;

	private long lastTraceIdSuccess = -1;
	private long lastTraceIdError = -1;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 * 
	 * @since 1.7
	 */
	public AbstractTraceProcessingFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	public AbstractTraceProcessingFilter(final Configuration configuration) {
		this(configuration, null);
	}

	protected final void reportSuccess(final long traceId) {
		this.lastTraceIdSuccess = traceId;
		this.numTracesSucceeded++;
		this.numTracesProcessed++;
	}

	protected final void reportError(final long traceId) {
		this.lastTraceIdError = traceId;
		this.numTracesFailed++;
		this.numTracesProcessed++;
	}

	public final int getSuccessCount() {
		return this.numTracesSucceeded;
	}

	public final int getErrorCount() {
		return this.numTracesFailed;
	}

	public final int getTotalCount() {
		return this.numTracesProcessed;
	}

	public final long getLastTraceIdError() {
		return this.lastTraceIdError;
	}

	public final long getLastTraceIdSuccess() {
		return this.lastTraceIdSuccess;
	}

	/**
	 * Returns a user-addressed status message to be logged by the calling tool.
	 * Extending classes may override this method but should call the then-inherited method first.
	 * 
	 */
	public void printStatusMessage() {
		this.printMessage(new String[] { "Trace processing summary: " + this.numTracesProcessed + " total; "
				+ this.numTracesSucceeded + " succeeded; " + this.numTracesFailed + " failed.", });
	}
}
