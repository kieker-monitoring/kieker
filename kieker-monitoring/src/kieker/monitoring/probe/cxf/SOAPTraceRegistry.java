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

package kieker.monitoring.probe.cxf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CXF does not provide an "around advice" for SOAP requests.
 * For this reason, we introduced this class wrapping access to
 * some thread-local variables used to pass information between
 * in- and out-interceptors.
 *
 * @author Andre van Hoorn
 *
 * @since 1.0
 */
public final class SOAPTraceRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOAPTraceRegistry.class);

	private static final SOAPTraceRegistry INSTANCE = new SOAPTraceRegistry();

	private final ThreadLocal<Long> threadLocalInRequestTin = new ThreadLocal<>();
	private final ThreadLocal<Long> threadLocalOutRequestTin = new ThreadLocal<>();
	private final ThreadLocal<Boolean> threadLocalInRequestIsEntryCall = new ThreadLocal<>();
	private final ThreadLocal<Boolean> threadLocalOutRequestIsEntryCall = new ThreadLocal<>();
	private final ThreadLocal<Integer> threadLocalInRequestEoi = new ThreadLocal<>();
	private final ThreadLocal<Integer> threadLocalInRequestEss = new ThreadLocal<>();

	/**
	 * Private constructor to avoid instantiation. This class is a singleton.
	 */
	private SOAPTraceRegistry() {}

	/**
	 * Delivers the one and only instance of this class.
	 *
	 * @return The singleton instance.
	 */
	public static final SOAPTraceRegistry getInstance() {
		synchronized (SOAPTraceRegistry.class) {
			return INSTANCE;
		}
	}

	/**
	 * Used to explicitly register the time tin of an incoming request.
	 * The thread is responsible for invalidating the stored value using
	 * the method unsetThreadLocalTin()!
	 *
	 * @param tin
	 *            The tin time of the request.
	 */
	public final void storeThreadLocalInRequestTin(final long tin) {
		this.threadLocalInRequestTin.set(tin);
	}

	/**
	 * This method returns the thread-local traceid previously
	 * registered using the method registerTraceId(curTraceId).
	 *
	 * @return the time tin. -1 if not registered before.
	 */
	public final long recallThreadLocalInRequestTin() {
		final Long curTin = this.threadLocalInRequestTin.get();
		if (curTin == null) {
			LOGGER.error("tin has not been registered before");
			return -1;
		}
		return curTin;
	}

	/**
	 * This method unsets a previously registered entry time tin.
	 */
	public final void unsetThreadLocalInRequestTin() {
		this.threadLocalInRequestTin.remove();
	}

	/**
	 * Used to explicitly register the time tin of an outgoing request.
	 * The thread is responsible for invalidating the stored value using
	 * the method unsetThreadLocalTin()!
	 *
	 * @param tin
	 *            The tin time of the request.
	 */
	public final void storeThreadLocalOutRequestTin(final long tin) {
		this.threadLocalOutRequestTin.set(tin);
	}

	/**
	 * This method returns the thread-local traceid previously
	 * registered using the method registerTraceId(curTraceId).
	 *
	 * @return the time tin. -1 if not registered before.
	 */
	public final long recallThreadLocalOutRequestTin() {
		final Long curTin = this.threadLocalOutRequestTin.get();
		if (curTin == null) {
			LOGGER.error("tin has not been registered before");
			return -1;
		}
		return curTin;
	}

	/**
	 * This method unsets a previously registered entry time tin.
	 */
	public final void unsetThreadLocalOutRequestTin() {
		this.threadLocalOutRequestTin.remove();
	}

	/**
	 * This method is used to store whether or not an incoming SOAP call
	 * was the entry point to the current trace.
	 *
	 * @param isEntry
	 *            Determines whether the call was the entry point to the trace or not.
	 */
	public final void storeThreadLocalInRequestIsEntryCall(final boolean isEntry) {
		this.threadLocalInRequestIsEntryCall.set(isEntry);
	}

	/**
	 * Returns the value of the ThreadLocal variable threadLocalInRequestIsEntryCall.
	 *
	 * @return the variable's value; true if value not set.
	 */
	public final boolean recallThreadLocalInRequestIsEntryCall() {
		final Boolean curIsEntryCall = this.threadLocalInRequestIsEntryCall.get();
		if (curIsEntryCall == null) {
			LOGGER.error("isEntryCall has not been registered before");
			return true;
		}
		return curIsEntryCall;
	}

	/**
	 * This method unsets a previously registered variable isInRequestEntryCall.
	 */
	public final void unsetThreadLocalInRequestIsEntryCall() {
		this.threadLocalInRequestIsEntryCall.remove();
	}

	/**
	 * This method is used to store whether or not an outgoing SOAP call
	 * was the entry point to the current trace.
	 *
	 * @param isEntry
	 *            Determines whether the call was the entry point to the trace or not.
	 */
	public final void storeThreadLocalOutRequestIsEntryCall(final boolean isEntry) {
		this.threadLocalOutRequestIsEntryCall.set(isEntry);
	}

	/**
	 * Returns the value of the ThreadLocal variable threadLocalOutRequestIsEntryCall.
	 *
	 * @return the variable's value; true if value not set.
	 */
	public final boolean recallThreadLocalOutRequestIsEntryCall() {
		final Boolean curIsEntryCall = this.threadLocalOutRequestIsEntryCall.get();
		if (curIsEntryCall == null) {
			LOGGER.error("isEntryCall has not been registered before");
			return true;
		}
		return curIsEntryCall;
	}

	/**
	 * This method unsets a previously registered variable isOutRequestEntryCall.
	 */
	public final void unsetThreadLocalOutRequestIsEntryCall() {
		this.threadLocalOutRequestIsEntryCall.remove();
	}

	/**
	 * Used to explicitly register an eoi for an incoming SOAP request.
	 * The thread is responsible for invalidating the stored curTraceId using
	 * the method unsetThreadLocalEOI()!
	 *
	 * @param eoi
	 *            The execution order index to store.
	 */
	public final void storeThreadLocalInRequestEOI(final int eoi) {
		// log.info(Thread.currentThread().getId());
		this.threadLocalInRequestEoi.set(eoi);
	}

	/**
	 * This method returns the thread-local eoi previously
	 * registered using the method registerTraceId(curTraceId).
	 *
	 * @return the eoi. -1 if no eoi registered.
	 */
	public final int recallThreadLocalInRequestEOI() {
		final Integer curEoi = this.threadLocalInRequestEoi.get();
		if (curEoi == null) {
			LOGGER.error("eoi has not been registered before");
			return -1;
		}
		return curEoi;
	}

	/**
	 * This method unsets a previously registered eoi.
	 */
	public final void unsetThreadLocalInRequestEOI() {
		this.threadLocalInRequestEoi.remove();
	}

	/**
	 * Used to explicitly register an ess for an incoming SOAP request.
	 * The thread is responsible for invalidating the stored curTraceId using
	 * the method unsetThreadLocalESS()!
	 *
	 * @param ess
	 *            The execution stack size to store.
	 */
	public final void storeThreadLocalInRequestESS(final int ess) {
		// log.info(Thread.currentThread().getId());
		this.threadLocalInRequestEss.set(ess);
	}

	/**
	 * This method returns the thread-local ess previously
	 * registered using the method registerTraceId(curTraceId).
	 *
	 * @return the ess. -1 if no ess registered.
	 */
	public final int recallThreadLocalInRequestESS() {
		final Integer curEss = this.threadLocalInRequestEss.get();
		if (curEss == null) {
			LOGGER.error("ess has not been registered before");
			return -1;
		}
		return curEss;
	}

	/**
	 * This method unsets a previously registered ess.
	 */
	public final void unsetThreadLocalInRequestESS() {
		this.threadLocalInRequestEss.remove();
	}
}
