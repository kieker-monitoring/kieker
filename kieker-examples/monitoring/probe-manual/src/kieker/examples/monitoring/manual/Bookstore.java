/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.monitoring.manual;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class Bookstore {

	private static final IMonitoringController MONITORING_CONTROLLER = MonitoringController.getInstance();
	private final Catalog catalog = new Catalog();
	private final CRM crm = new CRM(this.catalog);

	public void searchBook() {
		// 1.) Call Catalog.getBook() and log its entry and exit timestamps.
		final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
		this.catalog.getBook(false); // <-- the monitored execution
		final long tout = MONITORING_CONTROLLER.getTimeSource().getTime();

		final OperationExecutionRecord e = new OperationExecutionRecord(
				"public void " + this.catalog.getClass().getName() + ".getBook(boolean)",
				OperationExecutionRecord.NO_SESSION_ID,
				OperationExecutionRecord.NO_TRACE_ID,
				tin, tout, "myHost",
				OperationExecutionRecord.NO_EOI_ESS,
				OperationExecutionRecord.NO_EOI_ESS);
		MONITORING_CONTROLLER.newMonitoringRecord(e);

		// 2.) Call the CRM catalog's getOffers() method (without monitoring).
		this.crm.getOffers();
	}

}
