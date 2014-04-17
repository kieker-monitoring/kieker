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

package kieker.panalysis.stage.composite;

import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.stage.CountingFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CycledCountingFilter<T> extends CountingFilter<T> {

	// BETTER hide the internal ports; the following uncommented lines do not work however
	// private final IInputPort<CountingFilter<T>, Long> CURRENT_COUNT = this.createInputPort();
	// private final IOutputPort<CountingFilter<T>, Long> NEW_COUNT = this.createOutputPort();

	/**
	 * @since 1.10
	 * @param countingPipe
	 */
	private CycledCountingFilter(final IPipe<Long, ?> countingPipe) {
		countingPipe
				.source(this.NEW_COUNT)
				.target(this, this.CURRENT_COUNT);
		// FIXME counting pipe needs to be added to a group
	}

	// this constructor prevents the programmer from repeating the type argument
	public static <T> CycledCountingFilter<T> create(final IPipe<Long, ?> countingPipe) {
		return new CycledCountingFilter<T>(countingPipe);
	}

	/**
	 * @since 1.10
	 * @return
	 */
	public Long getCurrentCount() {
		return super.read(this.CURRENT_COUNT);
	}

}
