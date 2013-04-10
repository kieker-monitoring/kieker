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

package kieker.test.monitoring.aspectj.loadTimeWeaving.exceptions;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * Experimental test for Kieker in combination with exceptions.
 * 
 * @author Matthias Rohr
 * 
 * @since < 0.9
 */
public final class Main {

	private Main() {}

	/**
	 * This main method executes some of the dummy test methods.
	 * 
	 * @param args
	 *            The command line arguments. They have no effect.
	 */
	public static void main(final String[] args) {

		for (int i = 0; i < 10; i++) {
			try {
				Main.helloKieker(true);
			} catch (final Exception e) { // NOPMD NOCS (Exception)
			}
		}

		for (int i = 0; i < 10; i++) {
			try {
				Main.helloKieker(true);
			} catch (final Exception e) { // NOPMD NOCS (Exception)
			}

			try {
				Main.helloKieker(false);
			} catch (final Exception e) { // NOPMD NOCS (Exception)
			}
		}

	}

	/**
	 * A simple dummy method.
	 * 
	 * @param throwException
	 *            Determines whether this method throws an exception or not.
	 * 
	 * @throws Exception
	 *             If the parameter is set to true.
	 */
	@OperationExecutionMonitoringProbe
	public static void helloKieker(final boolean throwException) throws Exception {
		System.out.println("Hello World (look at your monitoring log ...)." + Thread.currentThread().getId() + " "); // NOPMD (System.out)
		if (throwException) {
			System.out.println("For test purposes, I will throw an exception now"); // NOPMD (System.out)
			throw new Exception("Test Exception"); // NOPMD NOCS (Exception)
		}
	}
}
