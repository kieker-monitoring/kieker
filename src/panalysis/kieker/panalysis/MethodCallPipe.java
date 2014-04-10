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

package kieker.panalysis;

import java.util.List;

import kieker.panalysis.base.AbstractPipe;
import kieker.panalysis.base.IInputPort;
import kieker.panalysis.base.IPipe;
import kieker.panalysis.base.ISink;
import kieker.panalysis.base.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class MethodCallPipe<T> extends AbstractPipe<T> {

	private IStage targetStage;
	private T storedToken;

	public MethodCallPipe(final T initialToken) {
		this.storedToken = initialToken;
	}

	public MethodCallPipe() {
		this.storedToken = null;
	}

	@Override
	protected void putInternal(final T token) {
		this.storedToken = token;
		this.targetStage.execute();
	}

	@Override
	protected T tryTakeInternal() {
		final T temp = this.storedToken;
		this.storedToken = null;
		return temp;
	}

	public T read() {
		return this.storedToken;
	}

	public void putMultiple(final List<T> items) {
		throw new IllegalStateException("Putting more than one element is not possible. You tried to put " + items.size() + " items.");
	}

	public List<?> tryTakeMultiple(final int numItemsToTake) {
		throw new IllegalStateException("Taking more than one element is not possible. You tried to take " + numItemsToTake + " items.");
	}

	@Override
	public <S extends ISink<S>> IPipe<T> target(final S targetStage, final IInputPort<S, T> targetPort) {
		this.targetStage = targetStage;
		return super.target(targetStage, targetPort);
	}

}
