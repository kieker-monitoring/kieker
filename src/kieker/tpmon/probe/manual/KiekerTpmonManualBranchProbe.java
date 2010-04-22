package kieker.tpmon.probe.manual;

/*
 * kieker.tpmon.KiekerTpmonManualBranchProbe
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

import kieker.tpmon.core.TpmonController;
import kieker.common.record.BranchingRecord;
import kieker.tpmon.probe.IKiekerMonitoringProbe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convenience class which provides a static method to log branching.
 *
 * @author Andre van Hoorn
 */
public class KiekerTpmonManualBranchProbe implements IKiekerMonitoringProbe {
    private static final Log log = LogFactory.getLog(KiekerTpmonManualBranchProbe.class);
    private static final TpmonController ctrlInst = TpmonController.getInstance();

    public static void monitorBranch(int branchID, int branchingOutcome){
        /* try-catch in order to avoid that any exception is propagated
         * to the application code. */
        try{
            ctrlInst.newMonitoringRecord(new BranchingRecord(ctrlInst.getTime(), branchID, branchingOutcome));
        }catch(Exception exc){
            log.error("Error monitoring branching", exc);
        }
    }
}
