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

package kieker.panalysis.composite;

import kieker.panalysis.CountingFilter;
import kieker.panalysis.base.IPipe;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CycledCountingFilter extends CountingFilter {

	public CycledCountingFilter(final IPipe countingPipe) {
		countingPipe.connect(this, CountingFilter.OUTPUT_PORT.NEW_COUNT, this,
				CountingFilter.INPUT_PORT.CURRENT_COUNT);
	}

	public Long getCurrentCount() {
		return (Long) super.read(CountingFilter.INPUT_PORT.CURRENT_COUNT);
	}

	public static final class INPUT_PORT { // mechanism to override the visibility of particular enum values of the super class // NOCS
		public static final CountingFilter.INPUT_PORT INPUT_OBJECT = CountingFilter.INPUT_PORT.INPUT_OBJECT;
	}

	public static final class OUTPUT_PORT { // mechanism to override the visibility of particular enum values of the super class // NOCS
		public static final CountingFilter.OUTPUT_PORT RELAYED_OBJECT = CountingFilter.OUTPUT_PORT.RELAYED_OBJECT;
	}

}
