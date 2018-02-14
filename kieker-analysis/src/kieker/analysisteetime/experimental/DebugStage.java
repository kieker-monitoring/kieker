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

package kieker.analysisteetime.experimental;

import teetime.stage.basic.AbstractFilter;

/**
 * A simple stage that can be used to place breakpoints to debugging.
 *
 * @param <T>
 *            Type of elements to process
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DebugStage<T> extends AbstractFilter<T> {

	public DebugStage() {
		super();
	}

	@Override
	protected void execute(final T element) {
		this.outputPort.send(element);
	}

}
