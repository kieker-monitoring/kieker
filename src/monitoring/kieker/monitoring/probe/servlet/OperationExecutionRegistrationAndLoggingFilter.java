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

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import kieker.common.util.ClassOperationSignaturePair;
import kieker.common.util.Signature;

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
 * @author Andre van Hoorn, Marco Luebcke, Jan Waller
 */
@Deprecated
public class OperationExecutionRegistrationAndLoggingFilter extends SessionAndTraceRegistrationFilter {

	/**
	 * Signature for the {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} which will be used when logging
	 * executions of this method.
	 */
	private final String filterOperationSignatureString;

	public OperationExecutionRegistrationAndLoggingFilter() {
		super(true); // *do* log filter executions
		final Signature methodSignature =
				new Signature("doFilter", // operation name
						new String[] { "public", "void" }, // modifier list
						"void", // return type
						new String[] { ServletRequest.class.getName(), ServletResponse.class.getName(), FilterChain.class.getName() }); // arg types
		final ClassOperationSignaturePair filterOperationSignaturePair =
				new ClassOperationSignaturePair(OperationExecutionRegistrationAndLoggingFilter.class.getName(), methodSignature);
		this.filterOperationSignatureString = filterOperationSignaturePair.toString();
	}

	@Override
	protected String getFilterOperationSignatureString() {
		return this.filterOperationSignatureString; // provide the signature including *this* classname
	}
}
