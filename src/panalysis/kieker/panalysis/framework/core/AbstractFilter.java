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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.chw.concurrent.DequePopException;
import de.chw.util.StopWatch;

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
public abstract class AbstractFilter<S extends IStage> extends AbstractStage implements ISink<S>, ISource, IPortListener<S> {

	protected volatile boolean mayBeDisabled; // BETTER write only non-concurrent code in a stage

	/**
	 * @author Christian Wulf
	 * 
	 * @since 1.10
	 */
	public enum StageState {
		UNINITIALIZED, PIPELINE_STARTED, PIPELINE_STOPPED
	}

	/**
	 * Indicates whether this stage has (already) been initialized.<br>
	 * <i>This attribute prevents this stage to be initialized more than once.</i>
	 */
	private StageState state = StageState.UNINITIALIZED;

	private int depth = IStage.DEPTH_NOT_SET;
	private int schedulingIndex;

	private final List<IInputPort<S, ?>> inputPorts = new ArrayList<IInputPort<S, ?>>();
	private final List<IInputPort<S, ?>> readOnlyInputPorts = Collections.unmodifiableList(this.inputPorts);

	private final List<IOutputPort<S, ?>> outputPorts = new ArrayList<IOutputPort<S, ?>>();
	private final List<IOutputPort<S, ?>> readOnlyOutputPorts = Collections.unmodifiableList(this.outputPorts);

	private Context<S> context;

	private final IPipeCommand closeCommand = new IPipeCommand() {
		public void execute(final IPipe<?> pipe) throws Exception {
			pipe.close();
		}
	};

	private final IPipeCommand pipelineStartsCommand = new IPipeCommand() {
		public void execute(final IPipe<?> pipe) throws Exception {
			pipe.notifyPipelineStarts();
		}
	};

	private int enabledInputPorts = 0;
	/**
	 * 0=in-memory, x>0=disk0, disk1, display0, display1, socket0, socket1 etc.
	 */
	private int accessesDeviceId = 0;

	private final StopWatch stopWatch = new StopWatch();
	private long overallDurationInNs = 0;

	private long lastDuration;

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
		} catch (final Exception e) {
			this.logger.error("Error in stage execution", e);
		}
		return success;
	}

	private boolean executeLogged(final Context<S> context) {
		this.stopWatch.start();
		try {
			final boolean success = this.execute(context);
			return success;
		} finally {
			this.stopWatch.end();
			this.lastDuration = this.stopWatch.getDuration();
			this.overallDurationInNs += this.lastDuration;
		}
	}

	protected abstract boolean execute(Context<S> context);

	public final void notifyPipelineStarts() throws Exception {
		if (this.state == StageState.UNINITIALIZED) {
			this.state = StageState.PIPELINE_STARTED;
			this.onPipelineStarts();
			this.notifyOutputPipes(this.pipelineStartsCommand);
		}
	}

	/**
	 * This method is called exactly once iff the pipeline is started.
	 * 
	 * @throws Exception
	 * @since 1.10
	 */
	public void onPipelineStarts() throws Exception {
		this.context = new Context<S>(this, this.readOnlyInputPorts);
	}

	/**
	 * @since 1.10
	 */
	public void notifyOutputPipes(final IPipeCommand pipeCommand) throws Exception {
		for (final IOutputPort<S, ?> outputPort : this.readOnlyOutputPorts) {
			final IPipe<?> associatedPipe = outputPort.getAssociatedPipe();
			if (associatedPipe != null) {
				pipeCommand.execute(associatedPipe);
			} // else: ignore unconnected port
		}
	}

	public final void notifyPipelineStops() {
		if (this.state != StageState.PIPELINE_STOPPED) {
			this.state = StageState.PIPELINE_STOPPED;
			this.onPipelineStops();
		}
	}

	/**
	 * This method is called exactly once iff the pipeline is stopped.
	 * 
	 * @since 1.10
	 */
	public void onPipelineStops() {
		// default empty implementation
	}

	/**
	 * @since 1.10
	 */
	public void onPortIsClosed(final IInputPort<S, ?> inputPort) {
		// inputPort.setState(IInputPort.State.CLOSING);
		this.enabledInputPorts--;
		// this.logger.info("Closed " + "(" + this.enabledInputPorts + " remaining) " + inputPort + " of " + this);

		if (this.enabledInputPorts < 0) {
			this.logger.error("Closed port more than once: portIndex=" + inputPort.getIndex() + " for stage " + this);
		}

		this.checkWhetherThisStageMayBeDisabled();
	}

	/**
	 * @since 1.10
	 */
	private void checkWhetherThisStageMayBeDisabled() {
		if (this.enabledInputPorts == 0) {
			this.mayBeDisabled = true;
			// this.logger.info(this.toString() + " can now be disabled by the pipeline scheduler.");
		}
	}

	/**
	 * @since 1.10
	 */
	public void fireSignalClosingToAllInputPorts() {
		// this.logger.info("Fire closing signal to all input ports of: " + this);

		if (!this.inputPorts.isEmpty()) {
			for (final IInputPort<S, ?> port : this.inputPorts) {
				port.close();
			}
		} else {
			this.checkWhetherThisStageMayBeDisabled();
		}
	}

	/**
	 * @since 1.10
	 */
	public void fireSignalClosingToAllOutputPorts() {
		try {
			this.notifyOutputPipes(this.closeCommand);
		} catch (final Exception e) {
			throw new IllegalStateException("may not happen");
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
		// return s;
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
		inputPort.setPortListener(this);
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
	 */
	@SuppressWarnings("unchecked")
	public List<IInputPort<S, ?>> getInputPorts() {
		return this.readOnlyInputPorts;
	}

	/**
	 * @since 1.10
	 */
	@SuppressWarnings("unchecked")
	public List<IOutputPort<S, ?>> getOutputPorts() {
		return this.readOnlyOutputPorts;
	}

	@Override
	public Collection<? extends IStage> getAllOutputStages() {
		final Collection<IStage> outputStages = new LinkedList<IStage>();
		for (final IOutputPort<S, ?> outputPort : this.readOnlyOutputPorts) {
			final IPipe<?> associatedPipe = outputPort.getAssociatedPipe();
			if (associatedPipe != null) {
				outputStages.add(associatedPipe.getTargetPort().getOwningStage());
			}
		}
		return outputStages;
	}

	public IInputPort<?, ?> getInputPortByIndex(final int index) {
		return this.readOnlyInputPorts.get(index);
	}

	public IOutputPort<?, ?> getOutputPortByIndex(final int index) {
		return this.readOnlyOutputPorts.get(index);
	}

	public long getOverallDurationInNs() {
		return this.overallDurationInNs;
	}

	public long getLastDuration() {
		return this.lastDuration;
	}

	public int getDepth() {
		return this.depth;
	}

	public void setDepth(final int depth) {
		this.depth = depth;
	}

	public int getSchedulingIndex() {
		return this.schedulingIndex;
	}

	public void setSchedulingIndex(final int schedulingIndex) {
		this.schedulingIndex = schedulingIndex;
	}
}
