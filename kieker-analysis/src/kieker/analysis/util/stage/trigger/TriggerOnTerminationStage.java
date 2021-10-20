/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.util.stage.trigger;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage sends a {@link Trigger} after the terminating signal was received.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TriggerOnTerminationStage<T> extends AbstractTransformation<Object, T> {

	private final T trigger;

	public TriggerOnTerminationStage(final T trigger) {
		super();
		this.trigger = trigger;
	}

	@Override
	protected void execute(final Object object) {
		// Do nothing
	}

	@Override
	protected void onTerminating() {
		this.outputPort.send(this.trigger);
		super.onTerminating();
	}

}
