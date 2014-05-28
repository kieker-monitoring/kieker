/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.examples.userguide.ch3and4bookstore;

import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.core.controller.MonitoringController;

public class CRM {

	private static final IMonitoringController MONITORING_CONTROLLER = MonitoringController.getInstance();

	private final Catalog catalog;

	public CRM(final Catalog catalog) {
		this.catalog = catalog;
	}

	public void getOffers() {
		// Invoke catalog.getBook() and monitor response time
		final long tin = CRM.MONITORING_CONTROLLER.getTimeSource().getTime();
		this.catalog.getBook(false);
		final long tout = CRM.MONITORING_CONTROLLER.getTimeSource().getTime();
		// Create a new record and set values
		final MyResponseTimeRecord e = new MyResponseTimeRecord(new Object[] { "mySimpleKickerExample.bookstoreTracing.Catalog", "getBook(..)", tout - tin });
		// Pass the record to the monitoring controller
		CRM.MONITORING_CONTROLLER.newMonitoringRecord(e);
	}
}
