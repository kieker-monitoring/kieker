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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <S>
 *            the extending stage
 * @param <I>
 *            The type of the input ports
 * @param <O>
 *            The type of the input ports
 * 
 */
public abstract class AbstractFilter<S extends IStage, I, O> extends AbstractStage implements ISink<S>, ISource {

	protected boolean mayBeDisabled;

	private int numPushedElements = 0;
	private int numTakenElements = 0;

	private final List<IInputPort<S, ? extends I>> inputPorts = new ArrayList<IInputPort<S, ? extends I>>();
	private final List<IOutputPort<S, ? extends O>> outputPorts = new ArrayList<IOutputPort<S, ? extends O>>();

	// private TaskBundle taskBundle;
	// private final int numTasksThreshold = 100;

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

	protected <T extends O> void put(final IOutputPort<S, T> port, final T object) {
		final IPipe<T, ?> associatedPipe = port.getAssociatedPipe();
		if (associatedPipe == null) {
			return; // ignore unconnected port
			// BETTER return a NullObject rather than checking for null
		}
		associatedPipe.put(object);
		this.numPushedElements++;
	}

	protected <T extends I> T tryTake(final IInputPort<S, T> inputPort) {
		final IPipe<? extends T, ?> associatedPipe = inputPort.getAssociatedPipe();
		final T token = associatedPipe.tryTake();
		if (token != null) {
			this.numTakenElements++;
		}
		return token;
	}

	protected <T extends I> T read(final IInputPort<S, T> inputPort) {
		final IPipe<? extends T, ?> associatedPipe = inputPort.getAssociatedPipe();
		return associatedPipe.read();
	}

	public void onSignalClosing(final IInputPort<S, ?> inputPort) {
		inputPort.setState(IInputPort.State.CLOSING);
		System.out.println("Closing " + inputPort + " of " + this.toString());

		for (final IInputPort<S, ?> iport : this.inputPorts) {
			if (iport.getState() != IInputPort.State.CLOSING) {
				return;
			}
		}

		this.mayBeDisabled = true;
		System.out.println(this.toString() + " can now be disabled by the pipeline scheduler.");
	}

	public void fireSignalClosingToAllInputPorts() {
		for (final IInputPort<S, ? extends I> port : this.inputPorts) {
			this.onSignalClosing(port);
		}
	}

	public void fireSignalClosingToAllOutputPorts() {
		this.logger.info("Fire closing signal to all output ports..." + "(" + this + ")");
		this.logger.info("outputPorts: " + this.outputPorts);
		for (final IOutputPort<S, ? extends O> port : this.outputPorts) {
			final IPipe<? extends O, ?> associatedPipe = port.getAssociatedPipe();
			if (associatedPipe != null) {
				associatedPipe.fireSignalClosing();
			} // else: ignore unconnected port
		}
	}

	public boolean mayBeDisabled() {
		return this.mayBeDisabled;
	}

	@Override
	public String toString() {
		final String s = super.toString();
		return "{" + s + ": " + "numPushedElements=" + this.numPushedElements + ", " + "numTakenElements=" + this.numTakenElements
				+ "}";
	}

	/**
	 * @since 1.10
	 * @return a new input port that accepts elements of the particular type that is specified in the variable declaration.
	 */
	// <T extends I> is necessary since I is usually the (generic) type Object
	public <T extends I> IInputPort<S, T> createInputPort() {
		final IInputPort<S, T> inputPort = new InputPortImpl<S, T>();
		this.inputPorts.add(inputPort);
		return inputPort;
	}

	/**
	 * @since 1.10
	 * @param stage
	 * @return
	 */
	// <T extends O> is necessary since O is usually the (generic) type Object
	public <T extends O> IOutputPort<S, T> createOutputPort() {
		final IOutputPort<S, T> outputPort = new OutputPortImpl<S, T>();
		this.outputPorts.add(outputPort);
		return outputPort;
	}

	/**
	 * @since 1.10
	 * @return
	 * 
	 */
	protected List<IInputPort<S, ? extends I>> getInputPorts() {
		return this.inputPorts;
	}

	/**
	 * @since 1.10
	 * @return
	 * 
	 */
	protected List<IOutputPort<S, ? extends O>> getOutputPorts() {
		return this.outputPorts;
	}
}
