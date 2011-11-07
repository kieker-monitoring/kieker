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

package kieker.analysis.configuration;

import java.util.Properties;

import kieker.common.configuration.AbstractConfiguration;

/**
 * @author Jan Waller
 */
public class Configuration extends AbstractConfiguration implements Keys {
	private static final long serialVersionUID = 1L;

	public Configuration(final Properties defaultValues) {
		super(defaultValues);
	}

	@Override
	public final Configuration getPropertiesStartingWith(final String prefix) {
		return (Configuration) this.getPropertiesStartingWith(new Configuration(null), prefix);
	}
}
