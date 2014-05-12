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
package kieker.panalysis.framework.concurrent;

import java.util.Collection;

import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class StealIfEmptyStrategy<T> implements IStealStrategy<T> {

	public <S extends IStage> T steal(final IInputPort<S, T> inputPort, final Collection<ConcurrentWorkStealingPipe<T>> pipesToStealFrom) {
		for (final ConcurrentWorkStealingPipe<T> pipe : pipesToStealFrom) {
			final T stolenElement = pipe.steal();
			if (stolenElement != null) {
				return stolenElement;
			}
		}
		// BETTER improve stealing efficiency by stealing multiple elements at once
		return null; // do not expose internal impl details (here: CircularWorkStealingDeque); instead return null
	}

}
