package kieker.test.monitoring.junit.core.configuration;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 the Kieker Project
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
 * ==================================================
 */

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.ConfigurationProperty;
import kieker.monitoring.core.configuration.MonitoringConfiguration;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestDefaultConfiguration extends TestCase {
	public void testDefaultConfigurationHasDefaultValues() {
		final MonitoringConfiguration config = MonitoringConfiguration
				.createDefaultConfiguration("Default config");
		Assert.assertEquals("Wrong default value for debug property",
				ConfigurationProperty.DEBUG_ENABLED.defaultValue(),
				Boolean.toString(config.isDebugEnabled()));
	}
}
