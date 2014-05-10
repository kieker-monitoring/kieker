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

import de.chw.concurrent.DequePopException;

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

	protected volatile boolean mayBeDisabled; // BETTER write only non-concurrent code in a stage

	private final List<IInputPort<S, ?>> inputPorts = new ArrayList<IInputPort<S, ?>>();
	private final List<IInputPort<S, ?>> readOnlyInputPorts = Collections.unmodifiableList(this.inputPorts);

	private final List<IOutputPort<S, ?>> outputPorts = new ArrayList<IOutputPort<S, ?>>();
	private final List<IOutputPort<S, ?>> readOnlyOutputPorts = Collections.unmodifiableList(this.outputPorts);

	private Context<S> context;

	private int enabledInputPorts = 0;
	private int accessesDeviceId = 0;
	private long overallDuration = 0; // 0=in-memory, x>0=disk0, disk1, socket0, socket1 etc.

	public long getOverallDuration() {
		return this.overallDuration;
	}

	public int getAccessesDeviceId() {
		return this.accessesDeviceId;
	}

	public void setAccessesDeviceId(final int accessesDeviceId) {
		this.accessesDeviceId = accessesDeviceId;
	}

	// BETTER return a limited context that allows "read" only
	public Context<S> getContext() {
		return this.context;
	}

	/**
	 * @since 1.10
	 */
	public final boolean execute() {
		boolean success = false;
		try {
			success = this.executeLogged(this.context);
			if (success) { // deprecated boolean return value
				this.context.clear();
			} else {
				this.context.rollback();
			}
		} catch (final DequePopException e) {
			this.context.rollback();
		}
		return success;
	}

	private boolean executeLogged(final Context<S> context) {
		final long start = System.currentTimeMillis();

		final boolean success = this.execute(context);

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;

		return success;
	}

	protected abstract boolean execute(Context<S> context);

	/**
	 * @throws Exception
	 * @since 1.10
	 */
	public void onPipelineStarts() throws Exception {
		this.context = new Context<S>(this.readOnlyInputPorts);
	}

	/**
	 * @since 1.10
	 */
	public void onPipelineStops() { // NOPMD (empty non-abstract method is intended)
		// empty default implementation
	}

	/**
	 * @since 1.10
	 */
	public <T> void onSignalClosing(final IInputPort<S, T> inputPort) {
		// inputPort.setState(IInputPort.State.CLOSING);
		this.enabledInputPorts--;
		this.logger.info("Closed " + "(" + this.enabledInputPorts + " remaining) " + inputPort + " of " + this);

		this.checkWhetherThisStageMayBeDisabled();
	}

	/**
	 * @since 1.10
	 */
	private void checkWhetherThisStageMayBeDisabled() {
		if (this.enabledInputPorts == 0) {
			this.mayBeDisabled = true;
			this.logger.info(this.toString() + " can now be disabled by the pipeline scheduler.");
		}
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
		this.logger.info("Fire closing signal to all output ports of: " + this);
		for (final IOutputPort<S, ?> port : this.outputPorts) {
			final IPipe<?> associatedPipe = port.getAssociatedPipe();
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
		@SuppressWarnings("unchecked")
		final InputPortImpl<S, T> inputPort = new InputPortImpl<S, T>((S) this);
		inputPort.setIndex(this.inputPorts.size());
		this.inputPorts.add(inputPort);
		this.enabledInputPorts++;
		return inputPort;
	}

	/**
	 * @since 1.10
	 * @return a new output port that accepts elements of the particular type that is specified in the variable declaration.
	 */
	protected <T> IOutputPort<S, T> createOutputPort() {
		@SuppressWarnings("unchecked")
		final OutputPortImpl<S, T> outputPort = new OutputPortImpl<S, T>((S) this);
		outputPort.setIndex(this.outputPorts.size());
		this.outputPorts.add(outputPort);
		return outputPort;
	}

	/**
	 * @since 1.10
	 * @return
	 * 
	 */
	protected List<IInputPort<S, ?>> getInputPorts() {
		return this.readOnlyInputPorts;
	}

	/**
	 * @since 1.10
	 * @return
	 * 
	 */
	public List<IOutputPort<S, ?>> getOutputPorts() {
		return this.readOnlyOutputPorts;
	}

	/**
	 * @since 1.10
	 */
	public void copyAttributes(final IStage stage) {
		// default empty implementation
	}

	public IInputPort<?, ?> getInputPortByIndex(final int index) {
		return this.inputPorts.get(index);
	}

	public IOutputPort<?, ?> getOutputPortByIndex(final int index) {
		return this.outputPorts.get(index);
	}

}
