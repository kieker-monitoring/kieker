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

package kieker.test.common.junit;

import kieker.common.logging.LogFactory;

/**
 * @author Jan Waller
 */
public abstract class AbstractKiekerTest { // NOPMD (no abstract methods)

	/**
	 * Log the currently executing class before any test is executed!
	 */
	public AbstractKiekerTest() {
		LogFactory.getLog(this.getClass()).info(this.getClass().getName());
	}
}
