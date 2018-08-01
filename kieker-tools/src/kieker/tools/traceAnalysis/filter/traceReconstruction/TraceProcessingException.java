/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.traceReconstruction;

import kieker.analysis.exception.EventProcessingException;

/**
 * This exceptions shows that something went wrong during the processing of a trace.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class TraceProcessingException extends EventProcessingException {
	private static final long serialVersionUID = 189899L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param msg
	 *            The message of this exception.
	 */
	public TraceProcessingException(final String msg) {
		super(msg);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param msg
	 *            The message of this exception.
	 * @param t
	 *            The cause of this exception.
	 */
	public TraceProcessingException(final String msg, final Throwable t) {
		super(msg, t);
	}
}
