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

package kieker.monitoring.timer;

import java.util.Date;

import kieker.common.configuration.Configuration;

/**
 * A timer implementation, counting in milliseconds since a specified offset.
 * 
 * @author Jan Waller
 */
public final class SystemMilliTimer extends AbstractTimeSource {
	public static final String CONFIG_OFFSET = SystemMilliTimer.class.getName() + ".offset";

	private final long offset;

	public SystemMilliTimer(final Configuration configuration) {
		super(configuration);
		if (configuration.getStringProperty(SystemMilliTimer.CONFIG_OFFSET).length() == 0) {
			this.offset = System.currentTimeMillis();
		} else {
			this.offset = configuration.getLongProperty(SystemMilliTimer.CONFIG_OFFSET);
		}
	}

	@Override
	public final long getTime() {
		return System.currentTimeMillis() - this.offset;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Time in milliseconds since ");
		sb.append(new Date(this.offset));
		return sb.toString();
	}
}
