package kieker.tpmon.probe.cxf;

import kieker.tpmon.annotation.TpmonInternal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * ==================================================
 */

/**
 * CXF does not provide an "around advice" for SOAP requests.
 * For this reason, we introduced this class wrapping access to
 * some thread-local variables used to pass information between 
 * in- and out-interceptors.
 *
 * @author Andre van Hoorn
 */
public class SOAPTraceRegistry {
    private static final Log log = LogFactory.getLog(SOAPTraceRegistry.class);

    private static final SOAPTraceRegistry instance = new SOAPTraceRegistry();

    private final ThreadLocal<Long> threadLocalInRequestTin = new ThreadLocal<Long>();
    private final ThreadLocal<Long> threadLocalOutRequestTin = new ThreadLocal<Long>();
    private final ThreadLocal<Boolean> threadLocalInRequestIsEntryCall = new ThreadLocal<Boolean>();
    private final ThreadLocal<Boolean> threadLocalOutRequestIsEntryCall = new ThreadLocal<Boolean>();
    private final ThreadLocal<Integer> threadLocalOutRequestEoi = new ThreadLocal<Integer>();

    private SOAPTraceRegistry(){
    }

    @TpmonInternal()
    public synchronized static final SOAPTraceRegistry getInstance() {
        return SOAPTraceRegistry.instance;
    }

    /**
     * Used to explicitly register the time tin of an incoming request.
     * The thread is responsible for invalidating the stored value using
     * the method unsetThreadLocalTin()!
     */
    @TpmonInternal()
    public final void storeThreadLocalInRequestTin(long tin) {
        this.threadLocalInRequestTin.set(tin);
    }

    /**
     * This method returns the thread-local traceid previously
     * registered using the method registerTraceId(curTraceId).
     *
     * @return the time tin. -1 if not registered before.
     */
    @TpmonInternal()
    public final long recallThreadLocalInRequestTin() {
        Long curTin = this.threadLocalInRequestTin.get();
        if (curTin == null) {
            log.fatal("tin has not been registered before");
            return -1;
        }
        return curTin;
    }

    /**
     * This method unsets a previously registered entry time tin.
     */
    @TpmonInternal()
    public final void unsetThreadLocalInRequestTin() {
        this.threadLocalInRequestTin.remove();
    }

    /**
     * Used to explicitly register the time tin of an outgoing request.
     * The thread is responsible for invalidating the stored value using
     * the method unsetThreadLocalTin()!
     */
    @TpmonInternal()
    public final void storeThreadLocalOutRequestTin(long tin) {
        this.threadLocalOutRequestTin.set(tin);
    }

    /**
     * This method returns the thread-local traceid previously
     * registered using the method registerTraceId(curTraceId).
     *
     * @return the time tin. -1 if not registered before.
     */
    @TpmonInternal()
    public final long recallThreadLocalOutRequestTin() {
        Long curTin = this.threadLocalOutRequestTin.get();
        if (curTin == null) {
            log.fatal("tin has not been registered before");
            return -1;
        }
        return curTin;
    }

    /**
     * This method unsets a previously registered entry time tin.
     */
    @TpmonInternal()
    public final void unsetThreadLocalOutRequestTin() {
        this.threadLocalOutRequestTin.remove();
    }

    /**
     * This method is used to store whether or not an incoming SOAP call
     * was the entry point to the current trace.
     */
    @TpmonInternal()
    public final void storeThreadLocalInRequestIsEntryCall(boolean isEntry){
        this.threadLocalInRequestIsEntryCall.set(isEntry);
    }

    /**
     * Returns the value of the ThreadLocal variable threadLocalInRequestIsEntryCall.
     *
     * @return the variable's value; true if value not set.
     */
    public final boolean recallThreadLocalInRequestIsEntryCall(){
        Boolean curIsEntryCall = this.threadLocalInRequestIsEntryCall.get();
        if (curIsEntryCall == null) {
            log.fatal("isEntryCall has not been registered before");
            return true;
        }
        return curIsEntryCall;
    }

    /**
     * This method unsets a previously registered variable isInRequestEntryCall.
     */
    @TpmonInternal()
    public final void unsetThreadLocalInRequestIsEntryCall() {
        this.threadLocalInRequestIsEntryCall.remove();
    }

    /**
     * This method is used to store whether or not an outgoing SOAP call
     * was the entry point to the current trace.
     */
    @TpmonInternal()
    public final void storeThreadLocalOutRequestIsEntryCall(boolean isEntry){
        this.threadLocalOutRequestIsEntryCall.set(isEntry);
    }

    /**
     * Returns the value of the ThreadLocal variable threadLocalOutRequestIsEntryCall.
     *
     * @return the variable's value; true if value not set.
     */
    public final boolean recallThreadLocalOutRequestIsEntryCall(){
        Boolean curIsEntryCall = this.threadLocalOutRequestIsEntryCall.get();
        if (curIsEntryCall == null) {
            log.fatal("isEntryCall has not been registered before");
            return true;
        }
        return curIsEntryCall;
    }

    /**
     * This method unsets a previously registered variable isOutRequestEntryCall.
     */
    @TpmonInternal()
    public final void unsetThreadLocalOutRequestIsEntryCall() {
        this.threadLocalOutRequestIsEntryCall.remove();
    }

    /**
     * Used to explicitly register an eoi for an outgoing SOAP request.
     * The thread is responsible for invalidating the stored curTraceId using
     * the method unsetThreadLocalEOI()!
     */
    @TpmonInternal()
    public final void storeThreadLocalInRequestEOI(int eoi) {
        //log.info(Thread.currentThread().getId());
        this.threadLocalOutRequestEoi.set(eoi);
    }

    /**
     * This method returns the thread-local eoi previously
     * registered using the method registerTraceId(curTraceId).
     *
     * @return the eoi. -1 if no eoi registered.
     */
    @TpmonInternal()
    public final int recallThreadLocalInRequestEOI() {
        Integer curEoi = this.threadLocalOutRequestEoi.get();
        if (curEoi == null) {
            log.fatal("eoi has not been registered before");
            return -1;
        }
        return curEoi;
    }

    /**
     * This method unsets a previously registered traceid.
     */
    @TpmonInternal()
    public final void unsetThreadLocalInRequestEOI() {
        this.threadLocalOutRequestEoi.remove();
    }
}
