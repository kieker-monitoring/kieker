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
import kieker.panalysis.base.ISink;
import kieker.panalysis.base.ISource;
import kieker.panalysis.base.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class MethodCallPipe extends AbstractPipe {

	private IStage targetStage;
	private Object storedRecord;

	public MethodCallPipe(final Object initialRecord) {
		this.storedRecord = initialRecord;
	}

	public MethodCallPipe() {
		this.storedRecord = null;
	}

	public void put(final Object record) {
		this.storedRecord = record;
		this.targetStage.execute();
	}

	public Object take() {
		final Object temp = this.storedRecord;
		this.storedRecord = null;
		return temp;
	}

	public Object tryTake() {
		return this.take();
	}

	public boolean isEmpty() {
		return this.storedRecord == null;
	}

	@Override
	public <O extends Enum<O>, I extends Enum<I>> void connect(final ISource<O> sourceStage, final O sourcePort, final ISink<I> targetStage, final I targetPort) {
		this.targetStage = targetStage;
		super.connect(sourceStage, sourcePort, targetStage, targetPort);
	}

	public List<Object> tryTakeMultiple(final int numItemsToTake) {
		throw new IllegalStateException("Taking more than one element is not possible. You tried to take " + numItemsToTake + " items.");
	}

	public void putMultiple(final List<?> items) {
		throw new IllegalStateException("Putting more than one element is not possible. You tried to put " + items.size() + " items.");
	}

}
