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

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 *            The type of the pipeline
 */
public abstract class AbstractPipe<T> implements IPipe<T> {

	private Runnable fireSignalClosing;

	public <S extends ISource> IPipe<T> source(final IOutputPort<S, T> sourcePort) {
		sourcePort.setAssociatedPipe(this);
		return this;
	}

	public <S extends ISink<S>> IPipe<T> target(final S targetStage, final IInputPort<S, T> targetPort) {
		targetPort.setAssociatedPipe(this);
		this.fireSignalClosing = new Runnable() {
			// This Runnable avoids the declaration of targetStage and targetPort as attributes and the declaration of I as type parameter
			public void run() {
				targetStage.onSignalClosing(targetPort);
			}
		};
		return this;
	}

	protected abstract void putInternal(T token);

	public final void put(final T token) {
		this.putInternal(token);
	}

	protected abstract T tryTakeInternal();

	public final T tryTake() {
		final T token = this.tryTakeInternal();
		return token;
	}

	public void fireSignalClosing() {
		this.fireSignalClosing.run();
	}

}
