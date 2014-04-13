/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.stage;

import kieker.panalysis.examples.countWords.AbstractDefaultFilter;

/**
 * @author Jan Waller, Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class CountingFilter<T> extends AbstractDefaultFilter<CountingFilter<T>> {

	public static enum INPUT_PORT { // NOCS
		INPUT_OBJECT, CURRENT_COUNT
	}

	public static enum OUTPUT_PORT { // NOCS
		RELAYED_OBJECT, NEW_COUNT
	}

	/**
	 * @since 1.10
	 */
	public boolean execute() {
		final T inputObject = super.tryTake(INPUT_PORT.INPUT_OBJECT);
		if (inputObject == null) {
			return false;
		}
		final Long count = super.tryTake(INPUT_PORT.CURRENT_COUNT);
		if (count == null) {
			return false;
		}

		super.put(OUTPUT_PORT.RELAYED_OBJECT, inputObject);
		super.put(OUTPUT_PORT.NEW_COUNT, count + 1); // BETTER support pipes with primitive values to improve performance by avoiding auto-boxing

		return true;
	}

}
