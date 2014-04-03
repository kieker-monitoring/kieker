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
import java.util.concurrent.LinkedBlockingQueue;

import kieker.panalysis.base.IPipe;
import kieker.panalysis.base.ISink;
import kieker.panalysis.base.ISource;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class QueuePipe<T> extends LinkedBlockingQueue<T> implements IPipe<T> {

	private static final long serialVersionUID = 1L;

	public QueuePipe(final int capacity) {
		super(capacity);
	}

	@Override
	public void put(final T record) {
		try {
			super.put(record);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T take() {
		try {
			return super.take();
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public T tryTake() {
		return super.poll();
	}

	public static <OutputPort extends Enum<OutputPort>, InputPort extends Enum<InputPort>> void connect(final ISource<OutputPort> sourceStage,
			final OutputPort sourcePort, final ISink<InputPort> targetStage, final InputPort targetPort) {
		final IPipe<Object> pipe = new QueuePipe<Object>(Integer.MAX_VALUE);
		sourceStage.setPipeForOutputPort(sourcePort, pipe);
		targetStage.setPipeForInputPort(targetPort, pipe);
	}

	public List<T> tryTakeMultiple(final int numItemsToTake) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putMultiple(final List<T> items) {
		// TODO Auto-generated method stub

	}

}
