/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.timer.ITimeSource;
import kieker.monitoring.timer.SystemNanoTimer;

/**
 * This class is a JUnit test for the {@link SystemNanoTimer}, testing the timer with different configurations.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class TestSystemNanoTimer extends AbstractTestTimeSource {

	/**
	 * Default constructor.
	 */
	public TestSystemNanoTimer() {
		// empty default constructor
	}

	/**
	 * This method tests the {@link SystemNanoTimer} with default configuration.
	 */
	@Test
	public final void testDefault() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		final ITimeSource ts = new SystemNanoTimer(configuration);
		super.testTime(ts, TimeUnit.NANOSECONDS);
	}

	/**
	 * This method tests the {@link SystemNanoTimer} with nanoseconds as time unit.
	 */
	@Test
	public final void testNanoseconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemNanoTimer.CONFIG_UNIT, "0");
		final ITimeSource ts = new SystemNanoTimer(configuration);
		super.testTime(ts, TimeUnit.NANOSECONDS);
	}

	/**
	 * This method tests the {@link SystemNanoTimer} with microseconds as time unit.
	 */
	@Test
	public final void testMicroseconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemNanoTimer.CONFIG_UNIT, "1");
		final ITimeSource ts = new SystemNanoTimer(configuration);
		super.testTime(ts, TimeUnit.MICROSECONDS);
	}

	/**
	 * This method tests the {@link SystemNanoTimer} with milliseconds as time unit.
	 */
	@Test
	public final void testMilliseconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemNanoTimer.CONFIG_UNIT, "2");
		final ITimeSource ts = new SystemNanoTimer(configuration);
		super.testTime(ts, TimeUnit.MILLISECONDS);
	}

	/**
	 * This method tests the {@link SystemNanoTimer} with seconds as time unit.
	 */
	@Test
	public final void testSeconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemNanoTimer.CONFIG_UNIT, "3");
		final ITimeSource ts = new SystemNanoTimer(configuration);
		super.testTime(ts, TimeUnit.SECONDS);
	}
}
