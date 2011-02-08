package kieker.monitoring.core;

import java.util.concurrent.TimeUnit;

import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Andre van Hoorn
 */
public interface ISamplingController extends IMonitoringRecordReceiver, IController {
	
	/**
	 * Schedules the given {@link ISampler} with given initial delay, and period.
	 * 
	 * @param sigarLogger
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler,
			final long initialDelay, final long period, final TimeUnit timeUnit);

	/**
	 * Stops future executions of the given periodic {@link ScheduledSamplerJob}.
	 * 
	 * @param sampler
	 * @return true if the sensor is not registered
	 */
	public boolean removeScheduledSampler(final ScheduledSamplerJob sampler);
}
