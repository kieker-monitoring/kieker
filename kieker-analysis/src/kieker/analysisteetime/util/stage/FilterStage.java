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

package kieker.analysisteetime.util.stage;

import java.util.function.Predicate;

import teetime.stage.basic.AbstractFilter;

/**
 * This stage filters incoming objects and forwards only those which meet the given predicate.
 *
 * @author Nils Christian Ehmke, Sören Henning
 *
 * @param <T>
 *            The precise type of the incoming and outgoing object.
 */
public final class FilterStage<T> extends AbstractFilter<T> {

	private final Predicate<T> predicate;

	public FilterStage(final Predicate<T> predicate) {
		this.predicate = predicate;
	}

	@Override
	protected void execute(final T element) {
		if (this.predicate.test(element)) {
			super.getOutputPort().send(element);
		}
	}

}
