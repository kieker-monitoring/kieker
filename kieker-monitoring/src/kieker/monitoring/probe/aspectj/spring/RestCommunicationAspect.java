package kieker.monitoring.probe.aspectj.spring;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

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
 * @author Felix
 *
 * @since 1.14
 */
@Aspect
public class RestCommunicationAspect {

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;
	private static final ITimeSource TIME = CTRLINST.getTimeSource();

	/* Pointcut listening to the request creation after the use of RestTemplate */
	@Pointcut("execution(protected * org.springframework.http.client.support.HttpAccessor.createRequest(..))")
	private void requestCreation() {
	}

	/* Pointcut listening to the service method on incoming messages */
	@Pointcut("execution(protected * org.springframework.web.servlet.FrameworkServlet.service(..))")
	private void frameworkServletService() {
	}

	/*
	 * After a request creation with a RestTemplate, we get the current traceId and
	 * orderIndex and add them to the Http header of our message. Also, we create a
	 * new BeforeSentEvent, which we pass to the MonitoringController.
	 */
	@AfterReturning(pointcut = "requestCreation()", returning = "request")
	public void doAfterRequestCreation(final ClientHttpRequest request) {
		final long traceId = TRACEREGISTRY.getTrace().getTraceId();
		final int orderIndex = TRACEREGISTRY.getTrace().getNextOrderId();

		final HttpHeaders headers = request.getHeaders();

		headers.add("kieker-traceId", Long.toString(traceId));
		headers.add("kieker-orderIndex", Integer.toString(orderIndex));

		CTRLINST.newMonitoringRecord(new BeforeSentRemoteEvent(TIME.getTime(), traceId, orderIndex, "REST"));
	}

	/*
	 * Before an incoming message is processed by the service method of Spring, we
	 * read the traceId and orderIndex from the Http header of our message. If
	 * existing, we create a new BeforeReceivedEvent and pass it to the
	 * MonitoringController.
	 */
	@Before("frameworkServletService() && args(request, ..)")
	public void doBeforeDispatcherService(final HttpServletRequest request) {
		final String traceIdStr = request.getHeader("kieker-traceId");
		final String orderIndexStr = request.getHeader("kieker-orderIndex");
		if (traceIdStr != null) {
			try {
				final long traceId = Long.parseLong(traceIdStr);
				final int orderIndex = Integer.parseInt(orderIndexStr);

				if (TRACEREGISTRY.getTrace() == null) {
					TRACEREGISTRY.registerTrace();
				}
				CTRLINST.newMonitoringRecord(new BeforeReceivedRemoteEvent(TIME.getTime(), traceId, orderIndex,
						TRACEREGISTRY.getTrace().getTraceId(), TRACEREGISTRY.getTrace().getNextOrderId()));
			} catch (final NumberFormatException exc) {
			}
		}
	}

}
