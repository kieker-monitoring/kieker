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

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.registry.IRegistry;
import kieker.monitoring.core.registry.Registry;

/**
 * @author Jan Waller
 */
public final class RegistryController extends AbstractController implements IRegistryController {
	private static final Log LOG = LogFactory.getLog(RegistryController.class);

	private IRegistry<String> stringRegistry;

	protected RegistryController(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected final void init() {
		this.stringRegistry = new Registry<String>(this.monitoringController);
	}

	@Override
	protected final void cleanup() {
		RegistryController.LOG.debug("Shutting down Registry Controller");
		// TODO: check if this could lead to NullPointerExceptions
		this.stringRegistry = null;
	}

	@Override
	public final String toString() {
		return null;
	}

	@Override
	public final int getIdForString(final String string) {
		return this.stringRegistry.get(string);
	}
}
