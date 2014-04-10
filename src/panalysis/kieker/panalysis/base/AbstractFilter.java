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

import java.util.EnumMap;
import java.util.Map;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <I>
 *            The type of the input ports
 * @param <O>
 *            The type of the input ports
 */
public abstract class AbstractFilter<I extends Enum<I>, O extends Enum<O>> extends AbstractStage<I> implements ISink<I>, ISource<O> {

	protected boolean mayBeDisabled;

	private final Map<I, Port> inputPortPipes;
	private final Map<O, Port> outputPortPipes;

	private int numPushedElements = 0;
	private int numTakenElements = 0;

	// private TaskBundle taskBundle;
	// private final int numTasksThreshold = 100;

	public AbstractFilter(final Class<I> inputEnumType, final Class<O> outputEnumType) {
		this.inputPortPipes = new EnumMap<I, Port>(inputEnumType);
		this.outputPortPipes = new EnumMap<O, Port>(outputEnumType);
	}

	public void setPipeForInputPort(final I inputPort, final IPipe pipe) {
		this.inputPortPipes.put(inputPort, new Port(pipe));
	}

	public void setPipeForOutputPort(final O outputPort, final IPipe pipe) {
		this.outputPortPipes.put(outputPort, new Port(pipe));
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
		final Port portObj = this.outputPortPipes.get(port);
		if (portObj == null) {
			return; // ignore unconnected port
		}
		final IPipe pipe = portObj.getPipe();
		pipe.put(record);
		this.numPushedElements++;
	}

	protected Object take(final I inputPort) {
		final Port portObj = this.inputPortPipes.get(inputPort);
		final IPipe pipe = portObj.getPipe();
		return pipe.take();
	}

	protected Object tryTake(final I inputPort) {
		final Port portObj = this.inputPortPipes.get(inputPort);
		final IPipe pipe = portObj.getPipe();
		final Object token = pipe.tryTake();
		if (token != null) {
			this.numTakenElements++;

		}
		return token;
	}

	protected Object read(final I inputPort) {
		final Port portObj = this.inputPortPipes.get(inputPort);
		final IPipe pipe = portObj.getPipe();
		return pipe.read();
	}

	public void onSignalClosing(final I inputPort) {
		final Port portObj = this.inputPortPipes.get(inputPort);
		portObj.setState(Port.State.CLOSING);
		System.out.println("Closing " + inputPort + " of " + this.toString());

		for (final Port po : this.inputPortPipes.values()) {
			if (po.getState() != Port.State.CLOSING) {
				return;
			}
		}

		this.mayBeDisabled = true;
		System.out.println(this.toString() + " can now be disabled by the pipeline scheduler.");
	}

	public void fireSignalClosingToAllInputPorts() {
		for (final I port : this.inputPortPipes.keySet()) {
			this.onSignalClosing(port);
		}
	}

	public void fireSignalClosingToAllOutputPorts() {
		for (final Port portObj : this.outputPortPipes.values()) {
			portObj.getPipe().fireSignalClosing();
		}
	}

	public boolean mayBeDisabled() {
		return this.mayBeDisabled;
	}

	/**
	 * For debugging purposes
	 * 
	 * @param port
	 * @return
	 */
	public IPipe getOutputPipe(final O port) {
		final Port portObj = this.outputPortPipes.get(port);
		final IPipe pipe = portObj.getPipe();
		return pipe;
	}

	@Override
	public String toString() {
		return "{" + this.getClass().getSimpleName() + ": " + "numPushedElements=" + this.numPushedElements + ", " + "numTakenElements=" + this.numTakenElements
				+ "}";
	}
}
