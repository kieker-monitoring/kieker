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

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 *            The type of the pipe
 * @param <P>
 *            the extending pipe
 */
public abstract class AbstractPipe<T> implements IPipe<T> {

	/**
	 * @author Christian Wulf
	 * 
	 * @since 1.10
	 */
	public enum PipeState {
		UNINITIALIZED, PIPELINE_STARTED, PIPELINE_STOPPED
	}

	private PipeState state = PipeState.UNINITIALIZED;

	private IOutputPort<?, ? extends T> sourcePort;
	private IInputPort<?, T> targetPort;

	public IOutputPort<?, ? extends T> getSourcePort() {
		return this.sourcePort;
	}

	public IInputPort<?, T> getTargetPort() {
		return this.targetPort;
	}

	public <S extends ISource, A extends T> void setSourcePort(final IOutputPort<S, A> sourcePort) {
		sourcePort.setAssociatedPipe(this);
		this.sourcePort = sourcePort;
	}

	public <S extends ISink<S>, A extends T> void setTargetPort(final IInputPort<S, T> targetPort) {
		targetPort.setAssociatedPipe(this);
		this.targetPort = targetPort;
	}

	// BETTER remove if it does not add any new functionality
	protected abstract void putInternal(T token);

	public void put(final T token) {
		this.putInternal(token);
	}

	// BETTER remove if it does not add any new functionality
	protected abstract T tryTakeInternal();

	public final T tryTake() {
		return this.tryTakeInternal();
	}

	public final void notifyPipelineStarts() throws Exception {
		if (this.state == PipeState.UNINITIALIZED) {
			this.state = PipeState.PIPELINE_STARTED;
			this.onPipelineStarts();
			this.getTargetPort().getOwningStage().notifyPipelineStarts();
		}
	}

	/**
	 * This method is called exactly once iff the pipeline is started.
	 * 
	 * @since 1.10
	 */
	public void onPipelineStarts() {
		// empty default implementation
	}

	public final void notifyPipelineStops() {
		if (this.state != PipeState.PIPELINE_STOPPED) {
			this.state = PipeState.PIPELINE_STOPPED;
			this.onPipelineStops();
			this.getTargetPort().getOwningStage().notifyPipelineStops();
		}
	}

	/**
	 * This method is called exactly once iff the pipeline is stopped.
	 * 
	 * @since 1.10
	 */
	public void onPipelineStops() {
		// empty default implementation
	}

	public void close() {
		this.targetPort.close();
	}

}
