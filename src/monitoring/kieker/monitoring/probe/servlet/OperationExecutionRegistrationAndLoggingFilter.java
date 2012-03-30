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

package kieker.monitoring.probe.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Register session and trace id for incoming requests.
 * This probe also logs activations of this probe as execution records.
 * 
 * It can be integrated into the web.xml as follows:
 * 
 * <filter>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <filter-class>kieker.monitoring.probe.servlet.OperationExecutionRegistrationAndLoggingFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * 
 * @deprecated To be removed in Kieker 1.6. Use {@link SessionAndTraceRegistrationFilter} instead.
 * 
 * @author Andre van Hoorn
 */
@Deprecated
public class OperationExecutionRegistrationAndLoggingFilter extends OperationExecutionRegistrationFilter {

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		this.doFilter(request, response, chain, true); // log this execution
	}
}
