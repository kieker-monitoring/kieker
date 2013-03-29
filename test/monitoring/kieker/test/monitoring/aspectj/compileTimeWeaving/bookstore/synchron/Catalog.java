/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.aspectj.compileTimeWeaving.bookstore.synchron;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test and demonstration scenario for Kieker's monitoring component.
 * 
 * THIS VARIANT IS nearly identical TO
 * kieker.tests.compileTimeWeaving.bookstore.Catalog. Method getBook is
 * synchronized here. This allows to test the (negative) performance influence
 * of synchronized method invocation.
 * 
 * @author Matthias Rohr History: 2008/20/10: Created this variant based on
 *         kieker.tests.compileTimeWeaving.bookstore.Catalog 2008/01/09:
 *         Refactoring for the first release of Kieker and publication under an
 *         open source licence 2007-04-18: Initial version
 * 
 * @since 0.91
 */
public final class Catalog {

	private Catalog() {}

	/**
	 * A dummy method which waits a little bit.
	 * 
	 * @param complexQuery
	 *            Determines whether to wait 20 ms or 2 ms.
	 */
	@OperationExecutionMonitoringProbe
	public static void getBook(final boolean complexQuery) {
		synchronized (Catalog.class) {
			if (complexQuery) {
				// complex query;
				Bookstore.waitabit(20);
			} else {
				// simple query;
				Bookstore.waitabit(2);
			}
		}
	}
}
