package mySimpleKiekerJMSExample.bookstoreDifferentRecordTypes60Sek;

import kieker.monitoring.annotation.TpmonExecutionMonitoringProbe;
import mySimpleKiekerJMSExample.annotation.MyRTProbe;

/**
 * kieker.tests.bookstore.Catalog.java
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 *
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component tpmon. See the kieker tutorial 
 * for more information 
 * (http://www.matthias-rohr.com/kieker/tutorial.html)
 *
 * @author Matthias Rohr, Andre van Hoorn
 * History:
 * 2009/06/23: Adapted for "different record type test"
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */
public class Catalog {
    @MyRTProbe()
    @TpmonExecutionMonitoringProbe()
    public static void getBook(boolean complexQuery) {
        if (complexQuery) {
            //KiekerTpmonManualBranchProbe.monitorBranch(1,0);
            Bookstore.waitabit(20);
        } else {
            //KiekerTpmonManualBranchProbe.monitorBranch(1,1);
            Bookstore.waitabit(2);
        }
    }
}
