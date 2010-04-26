package mySimpleKiekerExample.probe;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.probe.IMonitoringProbe;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import src.mySimpleKiekerExample.record.MyRTMonitoringRecord;

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
 *
 * @author Andre van Hoorn
 */
@Aspect
public class MyRTMonitoringProbe implements IMonitoringProbe {

    protected static final TpmonController CTRL = TpmonController.getInstance();

    @Around(value = "execution(@mySimpleKiekerExample.annotation.MyRTProbe * *.*(..))")
    public Object probe(ProceedingJoinPoint j) throws Throwable {
        MyRTMonitoringRecord record = new MyRTMonitoringRecord();
        record.component = j.getSignature().getDeclaringTypeName();
        record.service = j.getSignature().getName();
        Object retval; 
        long tin = CTRL.getTime();
        try {
            retval = j.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            record.rt = CTRL.getTime() - tin;
            CTRL.newMonitoringRecord(record);
        }
        return retval;
    }
}
