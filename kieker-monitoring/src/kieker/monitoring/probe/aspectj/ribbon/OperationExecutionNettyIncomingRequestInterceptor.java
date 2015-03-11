/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.ribbon;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.sun.jersey.spi.container.ContainerResponse;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.probe.aspectj.jersey.JerseyHeaderConstants;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
@Aspect
@DeclarePrecedence("kieker.monitoring.probe.aspectj.ribbon.*,kieker.monitoring.probe.aspectj.operationExecution.*")
public class OperationExecutionNettyIncomingRequestInterceptor extends AbstractAspectJProbe {
	private static final Log LOG = LogFactory.getLog(OperationExecutionNettyIncomingRequestInterceptor.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	private static final NettyTraceRegistry NETTY_REGISTRY = NettyTraceRegistry.getInstance();

	public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	@Around("execution(public void com.netflix.recipes.rss.netty.NettyHandlerContainer.messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent))")
	public Object operationRequest(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}

		// boolean entrypoint = true;
		// final String hostname = VMNAME;

		// Set traceId, sessionId, eoi, ess
		final MessageEvent messageEvent = (MessageEvent) thisJoinPoint.getArgs()[1];
		final HttpRequest request = (HttpRequest) messageEvent.getMessage();

		// This is a hack to get all values
		final List<String> headerList = request.getHeaders(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER);
		if ((headerList == null) || (headerList.size() == 0)) {
			LOG.error("No monitoring data found in the incoming request header");
			LOG.error("Will continue without sending back reponse header");
			// CTRLINST.terminateMonitoring();
			return thisJoinPoint.proceed();
		}
		final String operationExecutionHeader = headerList.get(0);
		// LOG.info("");
		LOG.info("requestHeader: " + operationExecutionHeader);
		// LOG.info("");
		final String[] headerArray = operationExecutionHeader.split(",");

		// Extract session id
		String sessionId = headerArray[1];
		if (sessionId.equals("null")) {
			sessionId = OperationExecutionRecord.NO_SESSION_ID;
		}

		// Extract EOI
		final String eoiStr = headerArray[2];
		int eoi = -1;
		try {
			// LOG.error("EOI before = " + eoiStr);
			// eoi = 1 + Integer.parseInt(eoiStr);
			eoi = Integer.parseInt(eoiStr);
			// LOG.error("EOI after = " + Integer.toString(eoi));
		} catch (final NumberFormatException exc) {
			LOG.warn("Invalid eoi", exc);
		}

		// Extract ESS
		final String essStr = headerArray[3];
		int ess = -1;
		try {
			ess = Integer.parseInt(essStr);
		} catch (final NumberFormatException exc) {
			LOG.warn("Invalid ess", exc);
		}

		// Extract trace id
		final String traceIdStr = headerArray[0];
		long traceId = -1;
		if (traceIdStr != null) {
			try {
				traceId = Long.parseLong(traceIdStr);
			} catch (final NumberFormatException exc) {
				LOG.warn("Invalid trace id", exc);
			}
		} else {
			traceId = CF_REGISTRY.getUniqueTraceId();
			sessionId = SESSION_ID_ASYNC_TRACE;
			// entrypoint = true;
			eoi = 0; // EOI of this execution
			ess = 0; // ESS of this execution
		}

		// Store thread-local values
		CF_REGISTRY.storeThreadLocalTraceId(traceId);
		CF_REGISTRY.storeThreadLocalEOI(eoi); // this execution has EOI=eoi; next execution will get eoi with incrementAndRecall
		// CF_REGISTRY.storeThreadLocalESS(ess + 1); // this execution has ESS=ess
		CF_REGISTRY.storeThreadLocalESS(ess); // this execution has ESS=ess
		SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);

		// measure before
		final long tin = TIME.getTime();
		NETTY_REGISTRY.storeThreadLocalInRequestTin(tin);
		NETTY_REGISTRY.storeThreadLocalInRequestEOI(eoi);
		NETTY_REGISTRY.storeThreadLocalInRequestESS(ess);
		// LOG.info("registry tin = " + NETTY_REGISTRY.recallThreadLocalInRequestTin());
		// LOG.info("registry eoi = " + NETTY_REGISTRY.recallThreadLocalInRequestEOI());
		// LOG.info("registry ess = " + NETTY_REGISTRY.recallThreadLocalInRequestESS());
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
			eoi = NETTY_REGISTRY.recallThreadLocalInRequestEOI();
		} finally {
			// measure after
			// final long tout = TIME.getTime();
			// CTRLINST.newMonitoringRecord(new OperationExecutionRecord("messageReceivedandWriteStatusAndHeader", sessionId, traceId, tin, tout, hostname, eoi,
			// ess));
			this.unsetKiekerThreadLocalData();
			this.unsetKiekerNettyRegistry();
		}
		return retval;
	}

	@Around("execution(public java.io.OutputStream com.netflix.recipes.rss.netty.NettyHandlerContainer.Writer.writeStatusAndHeaders(..))")
	public Object operationResponse(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}

		// final String hostname = VMNAME;

		final String sessionId;
		final long traceId = CF_REGISTRY.recallThreadLocalTraceId();
		final long tin = NETTY_REGISTRY.recallThreadLocalInRequestTin();
		// LOG.info("registry tin = " + NETTY_REGISTRY.recallThreadLocalInRequestTin());
		// LOG.info("registry eoi = " + NETTY_REGISTRY.recallThreadLocalInRequestEOI());
		// LOG.info("registry ess = " + NETTY_REGISTRY.recallThreadLocalInRequestESS());

		// final boolean isEntryCall = true;
		int eoi = -1;
		int ess = -1;
		// final int myEoi = -1;
		// final int myEss = -1;

		if (traceId == -1) {
			// Kieker trace Id not registered. Should not happen, since this is a response message!
			LOG.warn("Kieker traceId not registered. Will unset all threadLocal variables and return.");
			this.unsetKiekerThreadLocalData(); // unset all variables
			this.unsetKiekerNettyRegistry();
			return null;
		} else {
			// thread-local traceId exists: eoi, ess, and sessionID should have been registered before
			// eoi = CF_REGISTRY.recallThreadLocalEOI();
			// ess = CF_REGISTRY.recallThreadLocalESS();
			eoi = NETTY_REGISTRY.recallThreadLocalInRequestEOI();
			ess = NETTY_REGISTRY.recallThreadLocalInRequestESS();
			sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
		}

		final Object[] args = thisJoinPoint.getArgs();
		if (args[1] instanceof ContainerResponse) {
			final ContainerResponse cResponse = (ContainerResponse) args[1];
			final MultivaluedMap<String, Object> responseHeader = cResponse.getHttpHeaders();

			// Pass back trace id, session id, eoi but not ess (use old value before the request)
			final List<Object> responseHeaderList = new ArrayList<Object>();
			responseHeaderList.add(Long.toString(traceId) + ","
					+ sessionId + ","
					+ Integer.toString(CF_REGISTRY.recallThreadLocalEOI()));
			// LOG.info("");
			LOG.info("responseHeader = " + responseHeaderList.toString());
			// LOG.info("");
			responseHeader.put(RibbonHeaderConstants.OPERATION_EXECUTION_HEADER, responseHeaderList);
		}

		// execution of the called method
		final Object retval;
		try {

			if ((tin == -1) || (eoi == -1) || (ess == -1)) {
				LOG.warn("Continue without sending back response header");
				retval = thisJoinPoint.proceed();
			} else {
				retval = thisJoinPoint.proceed(args);
			}
		} finally {
			// measure after
			// final long tout = TIME.getTime();
			// CTRLINST.newMonitoringRecord(new OperationExecutionRecord("messageReceivedandWriteStatusAndHeader", sessionId, traceId, tin, tout, hostname, eoi,
			// ess));
			// cleanup
			// if (entrypoint) {
			// this.unsetKiekerThreadLocalData();
			// this.unsetKiekerNettyRegistry();
			// } else {
			// CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			// }
		}
		return retval;
	}

	private final void unsetKiekerThreadLocalData() {
		CF_REGISTRY.unsetThreadLocalTraceId();
		CF_REGISTRY.unsetThreadLocalEOI();
		CF_REGISTRY.unsetThreadLocalESS();
	}

	private final void unsetKiekerNettyRegistry() {
		NETTY_REGISTRY.unsetThreadLocalInRequestTin();
		NETTY_REGISTRY.unsetThreadLocalInRequestEOI();
		NETTY_REGISTRY.unsetThreadLocalInRequestESS();
	}
}
