/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 *
 * @since 1.3
 */
public final class TimeSourceController extends AbstractController implements ITimeSourceController {
	private static final Log LOG = LogFactory.getLog(TimeSourceController.class);

	/** the ITimeSource used by this instance. */
	private final ITimeSource timeSource;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration which is used to configure this controller.
	 */
	protected TimeSourceController(final Configuration configuration) {
		super(configuration);
		this.timeSource = AbstractController.createAndInitialize(ITimeSource.class, configuration.getStringProperty(ConfigurationKeys.TIMER_CLASSNAME),
				configuration);
		if (this.timeSource == null) {
			this.terminate();
		}
	}

	@Override
	protected final void init() {
		// do nothing
	}

	@Override
	protected final void cleanup() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down TimeSource Controller");
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("TimeSource: ");
		if (this.timeSource != null) {
			sb.append('\'');
			sb.append(this.getTimeSource().getClass().getName());
			sb.append("'\n\t");
			sb.append(this.getTimeSource().toString());
			sb.append("'\n");
		} else {
			sb.append("No TimeSource available\n");
		}
		return sb.toString();
	}

	@Override
	public final ITimeSource getTimeSource() {
		return this.timeSource;
	}
}
