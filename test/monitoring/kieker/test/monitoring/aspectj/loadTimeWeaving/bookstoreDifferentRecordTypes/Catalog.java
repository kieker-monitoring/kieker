/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.monitoring.aspectj.loadTimeWeaving.bookstoreDifferentRecordTypes;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;
import kieker.monitoring.probe.manual.BranchingProbe;

/**
 * A simple test and demonstration scenario for Kieker's
 * monitoring component.
 * 
 * @author Matthias Rohr, Andre van Hoorn
 *         History:
 *         2009/06/23: Adapted for "different record type test"
 *         2008/01/09: Refactoring for the first release of
 *         Kieker and publication under an open source licence
 *         2007-04-18: Initial version
 * 
 */
public final class Catalog {

	private Catalog() {}

	@OperationExecutionMonitoringProbe
	public static void getBook(final boolean complexQuery) {
		if (complexQuery) {
			BranchingProbe.monitorBranch(1, 0);
			Bookstore.waitabit(20);
		} else {
			BranchingProbe.monitorBranch(1, 1);
			Bookstore.waitabit(2000);
		}
	}
}
