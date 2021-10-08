/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.controller;

import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public interface ISamplingController {

	/**
	 * Schedules the given {@link ISampler} with given initial delay, and period.
	 *
	 * @param sampler
	 *            The sampler to schedule.
	 * @param initialDelay
	 *            The initial delay.
	 * @param period
	 *            The period.
	 * @param timeUnit
	 *            The time unit which determines how to interpret the given parameter.
	 *
	 * @return a {@link ScheduledSamplerJob} as a handler for removing the scheduled sampler later on by using the method
	 *         {@link #removeScheduledSampler(ScheduledSamplerJob)}.
	 *
	 * @since 1.3
	 */
	ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler, final long initialDelay, final long period, final TimeUnit timeUnit);

	/**
	 * Stops future executions of the given periodic {@link ScheduledSamplerJob} .
	 *
	 * @param sampler
	 *            The sampler to be removed.
	 *
	 * @return true if the sensor is not registered
	 *
	 * @since 1.3
	 */
	boolean removeScheduledSampler(final ScheduledSamplerJob sampler);
}
