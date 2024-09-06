/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis;

/**
 * Exception thrown on errors of the TraceAnalysisTool (old version)
 *
 * @author Reiner Jung
 * @since 1.15
 * @deprecated since 1.15 this exception was added to fix the misuse of Exeception in legacy code.
 */
@Deprecated
public class AnalysisTerminationException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 16849274046247645L;

	public AnalysisTerminationException(final String message) {
		super(message);
	}

}
