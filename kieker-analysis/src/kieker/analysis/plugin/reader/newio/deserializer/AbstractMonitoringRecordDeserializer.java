/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader.newio.deserializer;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;

/**
 * @author holger
 *
 * @since 1.13
 */
public abstract class AbstractMonitoringRecordDeserializer implements IMonitoringRecordDeserializer {

	/**
	 * Creates a new record deserializer.
	 *
	 * @param configuration
	 *            The configuration to use
	 * @param projectContext
	 *            The project context to use
	 */
	public AbstractMonitoringRecordDeserializer(final Configuration configuration, final IProjectContext projectContext) { // NOPMD Constructor template for
																															// deserializers
		// Constructor template
	}

}
