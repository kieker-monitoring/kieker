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

package kieker.monitoring.core.controller;

import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.timer.ITimeSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public class TimeSourceController extends AbstractController implements ITimeSourceController {
	private static final Log LOG = LogFactory.getLog(TimeSourceController.class);

	/** the ITimeSource used by this instance */
	private final ITimeSource timeSource;

	protected TimeSourceController(final Configuration configuration) {
		this.timeSource = AbstractController.createAndInitialize(ITimeSource.class, configuration.getStringProperty(Configuration.TIMER_CLASSNAME), configuration);
		if (this.timeSource == null) {
			terminate();
		}
	}

	@Override
	protected void init() {
		// do nothing
	}

	@Override
	protected final void cleanup() {
		TimeSourceController.LOG.debug("Shutting down TimeSource Controller");
		// nothing to do
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TimeSourceController: ");
		if (this.timeSource != null) {
			sb.append("TimeSource: '");
			sb.append(getTimeSource().getClass().getName());
			sb.append("'");
		} else {
			sb.append("No TimeSource available");
		}
		return sb.toString();
	}

	@Override
	public final ITimeSource getTimeSource() {
		return this.timeSource;
	}
}
