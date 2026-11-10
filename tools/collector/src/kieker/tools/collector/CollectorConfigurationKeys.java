/***************************************************************************
 * Copyright 2026 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.collector;

import kieker.tools.common.CommonConfigurationKeys;

/**
 * Configuration keys for the collector.
 *
 * @author Reiner Jung
 *
 * @since 2.0.5
 *
 */
public final class CollectorConfigurationKeys extends CommonConfigurationKeys {
	
	public static final String NO_DELAY = CommonConfigurationKeys.PREFIX + "no.delay";
	public static final String DELAY_FACTOR = CommonConfigurationKeys.PREFIX + "delay.factor";
	public static final String SHOW_RECORD_COUNT = CommonConfigurationKeys.PREFIX + "show.record.count";
	public static final String TIME_REWRITE = CommonConfigurationKeys.PREFIX + "time.rewrite"; 
	public static final String IGNORE_RECORDS_AFTER = CommonConfigurationKeys.PREFIX + "ignore.records.after.date";
	public static final String IGNORE_RECORDS_BEFORE = CommonConfigurationKeys.PREFIX + "ignore.records.before.date";
		
	
	/** do not instantiate factory. */
	private CollectorConfigurationKeys() {
		// factory class constructor
	}
}
