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
package kieker.panalysis.stage.basic;

import com.google.common.base.Predicate;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Nils Christian Ehmke
 * @param <T>
 * 
 * @since 1.10
 */
public class PredicateFilter<T> extends AbstractFilter<PredicateFilter<T>> {

	public final IInputPort<PredicateFilter<T>, T> inputPort = this.createInputPort();
	public final IOutputPort<PredicateFilter<T>, T> outputMatchingPort = this.createOutputPort();
	public final IOutputPort<PredicateFilter<T>, T> outputMismatchingPort = this.createOutputPort();

	private Predicate<T> predicate;

	public PredicateFilter(final Predicate<T> predicate) {
		this.setPredicate(predicate);
	}

	public PredicateFilter() {
		super();
	}

	public Predicate<T> getPredicate() {
		return this.predicate;
	}

	public void setPredicate(final Predicate<T> predicate) {
		this.predicate = predicate;
	}

	@Override
	protected boolean execute(final Context<PredicateFilter<T>> context) {
		final T inputObject = context.tryTake(this.inputPort);
		if (inputObject == null) {
			return false;
		}

		if (this.predicate.apply(inputObject)) {
			context.put(this.outputMatchingPort, inputObject);
		} else {
			context.put(this.outputMismatchingPort, inputObject);
		}

		return true;
	}

}
