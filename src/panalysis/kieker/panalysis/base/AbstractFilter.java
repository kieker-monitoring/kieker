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

package kieker.panalysis.base;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param I
 */
public abstract class AbstractFilter<I extends Enum<I>, O extends Enum<O>> extends AbstractStage<I> implements ISink<I>, ISource<O> {

	private final Map<I, IPipe> inputPortPipes;
	private final Map<O, IPipe> outputPortPipes;
	private final Map<I, IPipe> readOnlyInputPortPipes;

	// private TaskBundle taskBundle;
	// private final int numTasksThreshold = 100;

	public AbstractFilter(final Class<I> inputEnumType, final Class<O> outputEnumType) {
		this.inputPortPipes = new EnumMap<I, IPipe>(inputEnumType);
		this.outputPortPipes = new EnumMap<O, IPipe>(outputEnumType);
		this.readOnlyInputPortPipes = Collections.unmodifiableMap(this.inputPortPipes);
	}

	public void setPipeForInputPort(final I inputPort, final IPipe pipe) {
		this.inputPortPipes.put(inputPort, pipe);
	}

	public void setPipeForOutputPort(final O outputPort, final IPipe pipe) {
		this.outputPortPipes.put(outputPort, pipe);
	}

	// protected void put(final OutputPort port, final Object record) {
	// if (this.taskBundle == null) {
	// this.taskBundle = new TaskBundle(this, new LinkedList<Object>());
	// }
	//
	// this.taskBundle.getTasks().add(record);
	//
	// if (this.taskBundle.getTasks().size() == this.numTasksThreshold) {
	// this.put(port, this.taskBundle);
	// this.taskBundle = null;
	// }
	// }

	protected void put(final O port, final Object record) {
		final IPipe pipe = this.outputPortPipes.get(port);
		if (pipe == null) {
			return; // ignore unconnected port
		}
		pipe.put(record);
	}

	protected Object take(final I inputPort) {
		final IPipe pipe = this.inputPortPipes.get(inputPort);
		return pipe.take();
	}

	protected Object tryTake(final I inputPort) {
		final IPipe pipe = this.inputPortPipes.get(inputPort);
		return pipe.tryTake();
	}

	public Map<I, IPipe> getInputPortPipes() {
		return this.readOnlyInputPortPipes;
	}
}
