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

package kieker.panalysis.framework.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <S>
 *            the extending stage
 * 
 */
public abstract class AbstractFilter<S extends IStage> extends AbstractStage implements ISink<S>, ISource {

	protected volatile boolean mayBeDisabled;

	private final List<IInputPort<S, ?>> inputPorts = new ArrayList<IInputPort<S, ?>>();
	private final List<IInputPort<S, ?>> readOnlyInputPorts = Collections.unmodifiableList(this.inputPorts);

	private final List<IOutputPort<S, ?>> outputPorts = new ArrayList<IOutputPort<S, ?>>();
	private final List<IOutputPort<S, ?>> readOnlyOutputPorts = Collections.unmodifiableList(this.outputPorts);

	private Context<S> context;

	// private final Configuration configuration;
	//
	// public AbstractFilter(final Configuration configuration) {
	// this.configuration = configuration;
	// }

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
	/**
	 * @since 1.10
	 */
	protected <T> void put(final IOutputPort<S, T> port, final T object) {
		this.context.put(port, object);
	}

	/**
	 * @since 1.10
	 */
	protected <T> T tryTake(final IInputPort<S, T> inputPort) {
		return this.context.tryTake(inputPort);
	}

	/**
	 * @since 1.10
	 */
	protected <T> T read(final IInputPort<S, T> inputPort) {
		return this.context.read(inputPort);
	}

	public final boolean execute() {
		final boolean success = this.execute(this.context);
		if (success) {
			this.context.clear();
		} else {
			this.context.rollback();
		}
		return success;
	}

	protected abstract boolean execute(Context<S> context);

	/**
	 * @since 1.10
	 */
	public void onPipelineStarts() {
		this.context = new Context<S>(this.readOnlyInputPorts);
	}

	/**
	 * @since 1.10
	 */
	public void onSignalClosing(final IInputPort<S, ?> inputPort) {
		inputPort.setState(IInputPort.State.CLOSING);
		System.out.println("Closed " + inputPort + " of " + this.toString());

		this.checkWhetherThisStageMayBeDisabled();
	}

	/**
	 * @since 1.10
	 */
	private void checkWhetherThisStageMayBeDisabled() {
		for (final IInputPort<S, ?> iport : this.inputPorts) {
			if (iport.getState() != IInputPort.State.CLOSING) {
				return;
			}
		}

		this.mayBeDisabled = true;
		System.out.println(this.toString() + " can now be disabled by the pipeline scheduler.");
	}

	/**
	 * @since 1.10
	 */
	public void fireSignalClosingToAllInputPorts() {
		for (final IInputPort<S, ?> port : this.inputPorts) {
			this.onSignalClosing(port);
		}
		if (this.inputPorts.isEmpty()) {
			this.checkWhetherThisStageMayBeDisabled();
		}
	}

	/**
	 * @since 1.10
	 */
	public void fireSignalClosingToAllOutputPorts() {
		this.logger.info("Fire closing signal to all output ports..." + "(" + this + ")");
		this.logger.info("outputPorts: " + this.outputPorts);
		for (final IOutputPort<S, ?> port : this.outputPorts) {
			final IPipe<?, ?> associatedPipe = port.getAssociatedPipe();
			if (associatedPipe != null) {
				associatedPipe.fireSignalClosing();
			} // else: ignore unconnected port
		}
	}

	/**
	 * @since 1.10
	 */
	public boolean mayBeDisabled() {
		return this.mayBeDisabled;
	}

	@Override
	public String toString() {
		final String s = super.toString();
		return "{" + s + ": " + "numPushedElements=" + this.context + "}";
	}

	/**
	 * @since 1.10
	 * @return a new input port that accepts elements of the particular type that is specified in the variable declaration.
	 */
	protected <T> IInputPort<S, T> createInputPort() {
		final IInputPort<S, T> inputPort = new InputPortImpl<S, T>();
		((AbstractPort<S, T>) inputPort).setOwningStage((S) this);
		this.inputPorts.add(inputPort);
		return inputPort;
	}

	/**
	 * @since 1.10
	 * @param stage
	 * @return
	 */
	protected <T> IOutputPort<S, T> createOutputPort() {
		final IOutputPort<S, T> outputPort = new OutputPortImpl<S, T>();
		((AbstractPort<S, T>) outputPort).setOwningStage((S) this);
		this.outputPorts.add(outputPort);
		return outputPort;
	}

	/**
	 * @since 1.10
	 * @return
	 * 
	 */
	// FIXME hide vis to protected
	public List<IInputPort<S, ?>> getInputPorts() {
		return this.readOnlyInputPorts;
	}

	/**
	 * @since 1.10
	 * @return
	 * 
	 */
	protected List<IOutputPort<S, ?>> getOutputPorts() {
		return this.readOnlyOutputPorts;
	}
}
