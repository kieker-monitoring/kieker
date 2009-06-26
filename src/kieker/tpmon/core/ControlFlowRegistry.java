package kieker.tpmon.core;

import java.util.concurrent.atomic.AtomicLong;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.monitoringRecord.executions.RemoteCallMetaData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * kieker.tpmon.core.ControlFlowRegistry
 *
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
 *
 * @author Andre van Hoorn
 */

public class ControlFlowRegistry {
    private static final Log log = LogFactory.getLog(TpmonController.class);

    private static ControlFlowRegistry instance = null;

    private AtomicLong lastThreadId = new AtomicLong(0);
    private ThreadLocal<Long> threadLocalTraceId = new ThreadLocal<Long>();

    private ThreadLocal<Integer> threadLocalEoi = new ThreadLocal<Integer>();
    private ThreadLocal<Integer> threadLocalEss = new ThreadLocal<Integer>();

    private ControlFlowRegistry(){
    }

    @TpmonInternal()
    public synchronized static ControlFlowRegistry getInstance() {
        if (instance == null) {
            instance = new ControlFlowRegistry();
        }
        return ControlFlowRegistry.instance;
    }

  /**
     * This method returns a thread-local traceid which is globally
     * unique and stored it local for the thread.
     * The thread is responsible for invalidating the stored curTraceId using
     * the method unsetThreadLocalTraceId()!
     */
    @TpmonInternal()
    public long getAndStoreUniqueThreadLocalTraceId() {
        long id = lastThreadId.incrementAndGet();
        this.threadLocalTraceId.set(id);
        return id;
    }

    /**
     * This method stores a thread-local curTraceId.
     * The thread is responsible for invalidating the stored curTraceId using
     * the method unsetThreadLocalTraceId()!
     */
    @TpmonInternal()
    public void storeThreadLocalTraceId(long traceId) {
        this.threadLocalTraceId.set(traceId);
    }

    /**
     * This method returns the thread-local traceid previously
     * registered using the method registerTraceId(curTraceId).
     *
     * @return the traceid. -1 if no curTraceId has been registered
     *         for this thread.
     */
    @TpmonInternal()
    public long recallThreadLocalTraceId() {
        //log.info("Recalling curTraceId");
        Long traceIdObj = this.threadLocalTraceId.get();
        if (traceIdObj == null) {
            //log.info("curTraceId == null");
            return -1;
        }
//log.info("curTraceId =" + traceIdObj);
        return traceIdObj;
    }

    /**
     * This method unsets a previously registered traceid.
     */
    @TpmonInternal()
    public void unsetThreadLocalTraceId() {
        this.threadLocalTraceId.remove();
    }

    /**
     * Used by the spring aspect to explicitly register an curEoi.
     * The thread is responsible for invalidating the stored curTraceId using
     * the method unsetThreadLocalEOI()!
     */
    @TpmonInternal()
    public void storeThreadLocalEOI(int eoi) {
        this.threadLocalEoi.set(eoi);
    }

    /**
     * Since this method accesses a ThreadLocal variable,
     * it is not (necessary to be) thread-safe.
     */
    @TpmonInternal()
    public int incrementAndRecallThreadLocalEOI() {
        Integer curEoi = this.threadLocalEoi.get();
        if (curEoi == null) {
            log.fatal("eoi has not been registered before");
            return -1;
        }
        int newEoi = curEoi + 1;
        this.threadLocalEoi.set(newEoi);
        return newEoi;
    }

    /**
     * This method returns the thread-local curEoi previously
     * registered using the method registerTraceId(curTraceId).
     *
     * @return the sessionid. -1 if no curEoi registered.
     */
    @TpmonInternal()
    public int recallThreadLocalEOI() {
        Integer curEoi = this.threadLocalEoi.get();
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
    public void unsetThreadLocalEOI() {
        this.threadLocalEoi.remove();
    }

    /**
     * Used by the spring aspect to explicitly register a sessionid that is to be collected within
     * a servlet method (that knows the request object).
     * The thread is responsible for invalidating the stored curTraceId using
     * the method unsetThreadLocalSessionId()!
     */
    @TpmonInternal()
    public void storeThreadLocalESS(int ess) {
        this.threadLocalEss.set(ess);
    }

    /**
     * Since this method accesses a ThreadLocal variable,
     *  it is not (necessary to be) thread-safe.
     */
    @TpmonInternal()
    public int recallAndIncrementThreadLocalESS() {
        Integer curEss = this.threadLocalEss.get();
        if (curEss == null) {
            log.fatal("ess has not been registered before");
            return -1;
        }
        this.threadLocalEss.set(curEss + 1);
        return curEss;
    }

    /**
     * This method returns the thread-local curEss previously
     * registered using the method registerTraceId(curTraceId).
     *
     * @return the sessionid. -1 if no curEss registered.
     */
    @TpmonInternal()
    public int recallThreadLocalESS() {
        Integer ess = this.threadLocalEss.get();
        if (ess == null) {
            log.fatal("ess has not been registered before");
            return -1;
        }
        return ess;
    }

    /**
     * This method unsets a previously registered curEss.
     */
    @TpmonInternal()
    public void unsetThreadLocalESS() {
        this.threadLocalEss.remove();
    }

    /**
     * Use a RemoteCallMetaData object to transport tracing data together
     * with a remote method call. This is required to allows  tracing in
     * distributed systems.
     *
     * @param threadid
     */
    @TpmonInternal()
    public RemoteCallMetaData getRemoteCallMetaData() {
        long curTraceId = this.recallThreadLocalTraceId();
        if (curTraceId == -1) { // no curTraceId was registered
            log.info("Tpmon: warning traceid was null");
            curTraceId = this.getAndStoreUniqueThreadLocalTraceId();
        }

        int curEoi = this.recallThreadLocalEOI();
        if (curEoi == -1) {
            log.info("Tpmon: warning eoi == -1");
            curEoi = 0;
            this.storeThreadLocalEOI(0);
        }

        int curEss = this.recallThreadLocalESS();
        if (curEss == -1) {
            log.info("Tpmon: warning ess == -1");
            curEss = 0;
            this.storeThreadLocalESS(0);
        }

        return new RemoteCallMetaData(curTraceId, curEoi, curEss);
    }

    /**
     * This method has to be called to register an incomming remote call
     * @param rcmd
     * @param threadid
     */
    @TpmonInternal()
    public void registerRemoteCallMetaData(RemoteCallMetaData rcmd) {
        if (rcmd == null) {
            log.info("Tpmon: RCMD == null");
            this.getAndStoreUniqueThreadLocalTraceId();
            this.storeThreadLocalEOI(0);
            this.storeThreadLocalESS(0);
        } else {
            this.storeThreadLocalTraceId(rcmd.traceid);
            this.storeThreadLocalEOI(rcmd.eoi);
            this.storeThreadLocalESS(rcmd.ess);
        }
    }
}
