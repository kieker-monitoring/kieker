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

package kieker.test.monitoring.aspectJ.compileTimeWeaving.twoConcurrentMethodsExample;

import java.util.Random;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test and demonstration scenario for Kieker's monitoring component.
 * 
 * @author Matthias Rohr
 * 
 *         History:
 *         2009/02/20: Reduced text length
 *         2008/10/20: Initial version
 * 
 */
public class Starter extends Thread {

	private static volatile boolean boolvar = true;
	private final Random random = new Random();

	public Starter() {
		// nothing to do
	}

	public static void main(final String[] args) throws InterruptedException {
		for (int i = 0; i < 10000; i++) { // NOCS (MagicNumberCheck)
			new Starter().start(); // NOPMD (new in loop)
			// wait between requests
			Thread.sleep((int) (Math.max(0, (Math.random() * 115d) - (i / 142d)) + 1)); // NOCS (MagicNumberCheck)
		}
	}

	@Override
	public void run() {
		final double ranVal = this.random.nextDouble();
		if (ranVal < 0.5) { // NOCS (MagicNumberCheck)
			if (ranVal >= 0.25) { // NOCS (MagicNumberCheck)
				this.waitP(300); // NOCS (MagicNumberCheck)
			}
		} else {
			if (ranVal > 0.75) { // NOCS (MagicNumberCheck)
				this.work();
				this.waitP(300); // NOCS (MagicNumberCheck)
			} else {
				this.work();
			}
		}
	}

	@OperationExecutionMonitoringProbe
	public void waitP(final long sleeptime) {
		try {
			Thread.sleep(sleeptime);
		} catch (final InterruptedException e) {
		}
	}

	@OperationExecutionMonitoringProbe
	private void work() {
		int a = this.random.nextInt(6); // NOCS (MagicNumberCheck)
		for (int i = 0; i < 2000000; i++) { // NOCS (MagicNumberCheck)
			a += i / 1000; // NOCS (MagicNumberCheck)
		}
		if ((a % 10000) == 0) { // NOCS (MagicNumberCheck)
			Starter.boolvar ^= Starter.boolvar;
		}
	}
}
