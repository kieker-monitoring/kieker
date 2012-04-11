/***************************************************************************
 * Copyright 2012 by
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

package kieker.monitoring.probe.servlet;

/**
 * Register the session id (if it exists) and trace information of incoming request into
 * a thread-local data structure then accessible to other application-level probes.
 * The execution of the filter is not logged.
 * 
 * Servlet filter used to register session ids.
 * 
 * It can be integrated into the web.xml as follows:
 * 
 * <filter>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <filter-class>kieker.monitoring.probe.servlet.OperationExecutionRegistrationFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * 
 * @deprecated To be removed in Kieker 1.6. Use {@link SessionAndTraceRegistrationFilter} instead.
 * 
 * @author Andre van Hoorn, Marco Luebcke, Jan Waller
 */
@Deprecated
public class OperationExecutionRegistrationFilter extends SessionAndTraceRegistrationFilter {

	public OperationExecutionRegistrationFilter() {
		super(false); // do not log filter execution
	}

	// No need to override getFilterOperationSignatureString() because the executions are not logged.
}
