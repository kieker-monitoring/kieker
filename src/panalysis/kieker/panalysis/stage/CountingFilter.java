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

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CountingFilter<T> extends AbstractFilter<CountingFilter<T>> {

	public final IInputPort<CountingFilter<T>, T> INPUT_OBJECT = this.createInputPort();
	public final IInputPort<CountingFilter<T>, Long> CURRENT_COUNT = this.createInputPort();

	public final IOutputPort<CountingFilter<T>, T> RELAYED_OBJECT = this.createOutputPort();
	public final IOutputPort<CountingFilter<T>, Long> NEW_COUNT = this.createOutputPort();

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<CountingFilter<T>> context) {
		final T inputObject = context.tryTake(this.INPUT_OBJECT);
		if (inputObject == null) {
			return false;
		}
		final Long currentCount = context.tryTake(this.CURRENT_COUNT);
		if (currentCount == null) {
			return false;
		}

		context.put(this.RELAYED_OBJECT, inputObject);
		// FIXME using a MethodCallPipe leads to a stackoverflow since the value (currentCount+1) is put and taken infinitely
		context.put(this.NEW_COUNT, currentCount + 1); // BETTER support pipes with primitive values to improve performance by avoiding auto-boxing

		return true;
	}

}
