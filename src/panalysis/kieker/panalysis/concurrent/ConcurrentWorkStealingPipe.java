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

package kieker.panalysis.concurrent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.chw.concurrent.CircularWorkStealingDeque;

import kieker.panalysis.base.AbstractPipe;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentWorkStealingPipe extends AbstractPipe<ConcurrentWorkStealingPipe> {

	private final CircularWorkStealingDeque circularWorkStealingDeque = new CircularWorkStealingDeque();
	/*
	 * BETTER use a prioritized list that considers
	 * <ul>
	 * <li>the size of each deque to minimize steals
	 * <li>the core's locality to improve cache access performance
	 * </ul>
	 */
	private List<ConcurrentWorkStealingPipe> allOtherPipes;

	public void copyAllOtherPipes(final List<ConcurrentWorkStealingPipe> samePipesFromAllThreads) {
		this.allOtherPipes = new ArrayList<ConcurrentWorkStealingPipe>(samePipesFromAllThreads);
		this.allOtherPipes.remove(this);
	}

	@Override
	protected void putInternal(final Object token) {
		this.circularWorkStealingDeque.pushBottom(token);
	}

	public void putMultiple(final List<?> items) {
		// TODO Auto-generated method stub
		// BETTER find a way to put multiple elements directly without a loop
	}

	public Object take() {
		throw new IllegalStateException("deprecated");
	}

	@Override
	protected Object tryTakeInternal() {
		final Object record = this.circularWorkStealingDeque.popBottom();
		if (record == CircularWorkStealingDeque.EMPTY) {
			for (final ConcurrentWorkStealingPipe pipe : this.allOtherPipes) {
				final Object stolenElement = pipe.steal();
				if ((stolenElement != CircularWorkStealingDeque.EMPTY) && (stolenElement != CircularWorkStealingDeque.ABORT)) {
					return stolenElement;
				}
			}
			// BETTER improve stealing efficiency by stealing multiple elements at once
			return null; // do not expose internal impl details (here: CircularWorkStealingDeque); instead return null
		}
		return record;
	}

	public List<?> tryTakeMultiple(final int maxItemsToTake) {
		// TODO Auto-generated method stub
		// BETTER find a way to take multiple elements directly without a loop
		return null;
	}

	public Object read() {
		final Object record = this.circularWorkStealingDeque.readBottom();
		if (record == CircularWorkStealingDeque.EMPTY) {
			return null;
		}
		return record;
	}

	public Object steal() {
		return this.circularWorkStealingDeque.steal();
	}

	public List<Object> stealMultiple(final int maxItemsToSteal) {
		int maxItemsToStealCoutner = maxItemsToSteal;
		final List<Object> stolenElements = new LinkedList<Object>();
		while (maxItemsToStealCoutner-- > 0) {
			final Object stolenElement = this.steal();
			if ((stolenElement == CircularWorkStealingDeque.EMPTY) && (stolenElement == CircularWorkStealingDeque.ABORT)) {
				break;
			}
			stolenElements.add(stolenElement);
		}
		// BETTER find a way to steal multiple elements directly without a loop
		return stolenElements;
	}

}
