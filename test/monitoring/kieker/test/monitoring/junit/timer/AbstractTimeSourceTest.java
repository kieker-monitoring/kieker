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

package kieker.test.monitoring.junit.timer;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 */
public abstract class AbstractTimeSourceTest { // NOPMD (no abstract methods)

	public final void testTime(final ITimeSource timesource, final TimeUnit timeunit) { // NOPMD (only used by other tests)
		final long before = System.currentTimeMillis();
		final long measured = timesource.getTime();
		final long after = System.currentTimeMillis();

		final long beforeTU = timeunit.convert(before, TimeUnit.MILLISECONDS);
		final long afterTU = timeunit.convert(after + 1, TimeUnit.MILLISECONDS);

		Assert.assertTrue("Measured time (" + measured + ") has to be >= " + beforeTU, beforeTU <= measured);
		Assert.assertTrue("Measured time (" + measured + ") has to be <= " + afterTU, measured <= afterTU);
	}
}
