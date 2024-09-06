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

package kieker.monitoring.timer;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * A test for the class {@link SystemMilliTimer}.
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class SystemMilliTimerTest extends AbstractTimeSourceTest {

	/**
	 * Default constructor.
	 */
	public SystemMilliTimerTest() {
		// empty default constructor
	}

	/**
	 * This method tests the timer with default configuration.
	 */
	@Test
	public void testDefault() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		final ITimeSource ts = new SystemMilliTimer(configuration);
		super.testTime(ts, TimeUnit.MILLISECONDS);
	}

	/**
	 * This method tests the timer with nanoseconds as used time unit.
	 */
	@Test
	public void testNanoseconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemMilliTimer.CONFIG_UNIT, TimeUnit.NANOSECONDS.name());
		final ITimeSource ts = new SystemMilliTimer(configuration);
		super.testTime(ts, TimeUnit.NANOSECONDS);
	}

	/**
	 * This method tests the timer with microseconds as used time unit.
	 */
	@Test
	public void testMicroseconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemMilliTimer.CONFIG_UNIT, TimeUnit.MICROSECONDS.name());
		final ITimeSource ts = new SystemMilliTimer(configuration);
		super.testTime(ts, TimeUnit.MICROSECONDS);
	}

	/**
	 * This method tests the timer with milliseconds as used time unit.
	 */
	@Test
	public void testMilliseconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemMilliTimer.CONFIG_UNIT, TimeUnit.MILLISECONDS.name());
		final ITimeSource ts = new SystemMilliTimer(configuration);
		super.testTime(ts, TimeUnit.MILLISECONDS);
	}

	/**
	 * This method tests the timer with seconds as used time unit.
	 */
	@Test
	public void testSeconds() { // NOPMD (assert in superclass)
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(SystemMilliTimer.CONFIG_UNIT, TimeUnit.SECONDS.name());
		final ITimeSource ts = new SystemMilliTimer(configuration);
		super.testTime(ts, TimeUnit.SECONDS);
	}
}
