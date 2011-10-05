/***************************************************************************
 * Copyright 2011 by
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

package kieker.test.monitoring.aspectJ.loadTimeWeaving.exceptions;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * Experimental test for Kieker in combination with exceptions.
 * 
 * @author Matthias Rohr
 *         History:
 *         2008/01/10: Refactoring for the first release of
 *         Kieker and publication under an open source licence
 *         2007-05-10: Initial version
 * 
 */

public class Main {

	public static void main(final String[] args) {

		for (int i = 0; i < 10; i++) {
			try {
				Main.helloKieker(true);
			} catch (final Exception e) {}
		}

		for (int i = 0; i < 10; i++) {
			try {
				Main.helloKieker(true);
			} catch (final Exception e) {}

			try {
				Main.helloKieker(false);
			} catch (final Exception e) {}
		}

	}

	@OperationExecutionMonitoringProbe()
	public static void helloKieker(final boolean throwException) throws Exception {
		System.out.println("Hello World (look at your monitoring log ...)." + Thread.currentThread().getId() + " ");
		if (throwException) {
			System.out.println("For test purposes, I will throw an exception now");
			throw new Exception("");
		}
	}
}
