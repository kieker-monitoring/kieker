package kieker.monitoring.probe.aspectJ.executions;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

import kieker.monitoring.core.ControlFlowRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/*
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
 * @author Andre van Hoorn
 */
@Aspect
public abstract class AbstractOperationExecutionAspect implements IMonitoringProbe {

    protected static final MonitoringController ctrlInst = MonitoringController.getInstance();
    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    protected static final String vmName = ctrlInst.getVmName();

    
    protected OperationExecutionRecord initExecutionData(ProceedingJoinPoint thisJoinPoint) {
       // e.g. "getBook" 
        String methodname = thisJoinPoint.getSignature().getName();
        // toLongString provides e.g. "public kieker.tests.springTest.Book kieker.tests.springTest.CatalogService.getBook()"
        String paramList = thisJoinPoint.getSignature().toLongString();
        int paranthIndex = paramList.lastIndexOf('(');
        paramList = paramList.substring(paranthIndex); // paramList is now e.g.,  "()"

        OperationExecutionRecord execData = new OperationExecutionRecord(
                thisJoinPoint.getSignature().getDeclaringTypeName() /* component */, 
                methodname + paramList /* operation */, 
                cfRegistry.recallThreadLocalTraceId() /* traceId, -1 if entry point*/);
        
        execData.isEntryPoint = false;
        //execData.traceId = ctrlInst.recallThreadLocalTraceId(); // -1 if entry point
        if (execData.traceId == -1) { 
            execData.traceId = cfRegistry.getAndStoreUniqueThreadLocalTraceId();
            execData.isEntryPoint = true;
        }
        execData.vmName = vmName;
        execData.experimentId = ctrlInst.getExperimentId();
        return execData;
    }
    
    
    public abstract Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable;
    
    
    protected void proceedAndMeasure(ProceedingJoinPoint thisJoinPoint,
            OperationExecutionRecord execData) throws Throwable {
        execData.tin = ctrlInst.getTime(); // startint stopwatch    
        try {
            execData.retVal = thisJoinPoint.proceed();
        } catch (Exception e) {
            throw e; // exceptions are forwarded
        } finally {
            execData.tout = ctrlInst.getTime();
            if (execData.isEntryPoint) {
                cfRegistry.unsetThreadLocalTraceId();
            }
        }
    }
}
