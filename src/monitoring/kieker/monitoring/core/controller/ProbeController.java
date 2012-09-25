/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

/**
 * @author Jan Waller
 */
public class ProbeController extends AbstractController implements IProbeController {
	private static final Log LOG = LogFactory.getLog(ProbeController.class);

	protected ProbeController(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected void init() {
		// nothing to do
	}

	@Override
	protected void cleanup() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down Probe Controller");
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(40);
		sb.append("ProbeController: not yet implemented.\n");
		return sb.toString();
	}

	public boolean activateProbe(final String signature) {
		return false;
	}

	public boolean deactivateProbe(final String signature) {
		return false;
	}

	public boolean isProbeActive(final String signature) {
		return this.monitoringController.isMonitoringEnabled();
	}
}
