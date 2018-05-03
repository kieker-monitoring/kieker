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

package kieker.monitoring.probe.aspectj.operationExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Jan Waller
 * 
 * @since 1.3
 */
@Aspect
public class OperationExecutionAspectFullServlet extends AbstractOperationExecutionAspectServlet {

	/**
	 * Default constructor.
	 */
	public OperationExecutionAspectFullServlet() {
		// empty default constructor
	}

	@Override
	@Pointcut("execution(* *.do*(..)) && args(request,response)")
	public void monitoredServlet(final HttpServletRequest request, final HttpServletResponse response) {
		// Aspect Declaration (MUST be empty)
	}

	@Override
	@Pointcut("execution(* *(..)) || execution(new(..))")
	public void monitoredOperation() {
		// Aspect Declaration (MUST be empty)
	}
}
