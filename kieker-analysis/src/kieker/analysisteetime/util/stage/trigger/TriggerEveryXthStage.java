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

package kieker.analysisteetime.util.stage.trigger;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage sends a {@link Trigger} after every x-th incoming object.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TriggerEveryXthStage extends AbstractTransformation<Object, Trigger> {

	private int counter; // = 0
	private final int threshold;
	private final Trigger trigger = new Trigger();

	public TriggerEveryXthStage(final int threshold) {
		super();
		this.threshold = threshold;
	}

	@Override
	protected void execute(final Object object) {
		this.counter++;
		if ((this.counter % this.threshold) == 0) {
			this.counter = 0; // To avoid overflows in some point in time
			this.outputPort.send(this.trigger);
		}
	}

}
