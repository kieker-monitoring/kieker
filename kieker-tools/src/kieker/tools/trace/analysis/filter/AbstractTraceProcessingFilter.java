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

package kieker.tools.trace.analysis.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * This is an abstract base for filters processing traces.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public abstract class AbstractTraceProcessingFilter extends AbstractTraceAnalysisFilter {

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
	 */
	public AbstractTraceProcessingFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * This method can be used to report a trace which has been processed successfully.
	 * 
	 * @param traceId
	 *            The ID of the processed trace.
	 */
	protected final void reportSuccess(final long traceId) {
		synchronized (this) {
			this.lastTraceIdSuccess = traceId;
			this.numTracesSucceeded++;
			this.numTracesProcessed++;
		}
	}

	/**
	 * This method can be used to report a trace which has <b>not</b> been processed successfully.
	 * 
	 * @param traceId
	 *            The ID of the processed trace.
	 */
	protected final void reportError(final long traceId) {
		synchronized (this) {
			this.lastTraceIdError = traceId;
			this.numTracesFailed++;
			this.numTracesProcessed++;
		}
	}

	/**
	 * Delivers the number of traces which have been processed successfully.
	 * 
	 * @return The number of traces.
	 */
	public final int getSuccessCount() {
		synchronized (this) {
			return this.numTracesSucceeded;
		}
	}

	/**
	 * Delivers the number of traces which have <b>not</b> been processed successfully.
	 * 
	 * @return The number of traces.
	 */
	public final int getErrorCount() {
		synchronized (this) {
			return this.numTracesFailed;
		}
	}

	/**
	 * Delivers the total number of traces which have been processed.
	 * 
	 * @return The number of traces.
	 */
	public final int getTotalCount() {
		synchronized (this) {
			return this.numTracesProcessed;
		}
	}

	/**
	 * Delivers the ID of the last trace which has <b>not</b> been processed successfully.
	 * 
	 * @return The trace ID.
	 */
	public final long getLastTraceIdError() {
		synchronized (this) {
			return this.lastTraceIdError;
		}
	}

	/**
	 * Delivers the ID of the last trace which has been processed successfully.
	 * 
	 * @return The trace ID.
	 */
	public final long getLastTraceIdSuccess() {
		synchronized (this) {
			return this.lastTraceIdSuccess;
		}
	}

	/**
	 * Returns a user-addressed status message to be logged by the calling tool.
	 * Extending classes may override this method but should call the then-inherited method first.
	 * 
	 */
	public void printStatusMessage() {
		synchronized (this) {
			this.printDebugLogMessage(new String[] { "Trace processing summary: " + this.numTracesProcessed + " total; "
					+ this.numTracesSucceeded + " succeeded; " + this.numTracesFailed + " failed.", });
		}
	}
}
