/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.spring;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.BeforeReceivedRemoteEvent;
import kieker.common.record.flow.trace.BeforeSentRemoteEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.timer.ITimeSource;

/**
 * Aspect for transmitting the Kieker traceId and orderIndex from a caller
 * application to a callee application with Spring.
 *
 * @author Felix Eichhorst
 *
 * @since 1.14
 */
@Aspect
public class RestCommunicationAspect {

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;
	private static final ITimeSource TIME = CTRLINST.getTimeSource();

	private static final String KIEKER_TRACE_ID_HEADER = "kieker-traceId";
	private static final String KIEKER_ORDER_INDEX_HEADER = "kieker-orderIndex";
	private static final String TECHNOLOGY = "REST";

	public RestCommunicationAspect() {
		// empty default constructor
	}

	/**
	 * Pointcut listening to the request creation after the use of RestTemplate.
	 */
	@Pointcut("execution(protected * org.springframework.http.client.support.HttpAccessor.createRequest(..))")
	private void requestCreation() { // NOPMD (pointcut)
		// pointcut
	}

	/** Pointcut listening to the service method on incoming messages. */
	@Pointcut("execution(protected * org.springframework.web.servlet.FrameworkServlet.service(..))")
	private void frameworkServletService() { // NOPMD (pointcut)
		// pointcut
	}

	/**
	 * Before an incoming message is processed by the service method of Spring,
	 * we read the traceId and orderIndex from the Http header of our message.
	 * If existing, we create a new BeforeReceivedEvent and pass it to the
	 * MonitoringController.
	 */
	@Before("frameworkServletService() && args(request, ..)")
	public void doBeforeDispatcherService(final HttpServletRequest request) {
		final String traceIdStr = request.getHeader(KIEKER_TRACE_ID_HEADER);
		if (traceIdStr == null) { // performance optimization
			return;
		}

		final long traceId;
		try {
			traceId = Long.parseLong(traceIdStr);
		} catch (final NumberFormatException exc) {
			return;
		}

		final String orderIndexStr = request.getHeader(KIEKER_ORDER_INDEX_HEADER);
		if (orderIndexStr == null) { // performance optimization
			return;
		}

		final int orderIndex;
		try {
			orderIndex = Integer.parseInt(orderIndexStr);
		} catch (final NumberFormatException exc) {
			return;
		}

		if (TRACEREGISTRY.getTrace() == null) {
			TRACEREGISTRY.registerTrace();
		}

		final IMonitoringRecord newRecord = new BeforeReceivedRemoteEvent(TIME.getTime(), traceId, orderIndex,
				TRACEREGISTRY.getTrace().getTraceId(), TRACEREGISTRY.getTrace().getNextOrderId());

		CTRLINST.newMonitoringRecord(newRecord);

	}

	/**
	 * After a request creation with a RestTemplate, we get the current traceId
	 * and orderIndex and add them to the Http header of our message. Also, we
	 * create a new BeforeSentEvent, which we pass to the MonitoringController.
	 */
	@AfterReturning(pointcut = "requestCreation()", returning = "request")
	public void doAfterRequestCreation(final ClientHttpRequest request) {
		final long traceId = TRACEREGISTRY.getTrace().getTraceId();
		final int orderIndex = TRACEREGISTRY.getTrace().getNextOrderId();

		final HttpHeaders headers = request.getHeaders();

		headers.add(KIEKER_TRACE_ID_HEADER, Long.toString(traceId));
		headers.add(KIEKER_ORDER_INDEX_HEADER, Integer.toString(orderIndex));

		CTRLINST.newMonitoringRecord(new BeforeSentRemoteEvent(TIME.getTime(), traceId, orderIndex, TECHNOLOGY));
	}

}
