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

package kieker.examples.userguide.ch3and4bookstore;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class Bookstore {

	private static final IMonitoringController MONITORING_CONTROLLER =
			MonitoringController.getInstance();

	private final Catalog catalog = new Catalog();
	private final CRM crm = new CRM(this.catalog);

	public void searchBook() {
		{
			// 1. Invoke catalog.getBook() and monitor response time
			final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
			this.catalog.getBook(false);
			final long tout = MONITORING_CONTROLLER.getTimeSource().getTime();
			// Create a new record and set values
			final MyResponseTimeRecord e = new MyResponseTimeRecord(
					"mySimpleKiekerExample.bookstoreTracing.Catalog", "getBook(..)", tout - tin);
			// Pass the record to the monitoring controller
			MONITORING_CONTROLLER.newMonitoringRecord(e);
		}

		{
			// 2. Invoke crm.getOffers() (without monitoring)
			this.crm.getOffers();
		}
	}
}
