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
public abstract class AbstractPipe<T extends AbstractPipe<T>> implements IPipe {

	private Runnable fireSignalClosing;

	public <O extends Enum<O>, I extends Enum<I>> void connect(final ISource<O> sourceStage, final O sourcePort, final ISink<I> targetStage,
			final I targetPort) {
		this.source(sourceStage, sourcePort);
		this.target(targetStage, targetPort);
		System.out.println("Connected " + sourceStage.getClass().getSimpleName() + " with " + targetStage.getClass().getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public <O extends Enum<O>> T source(final ISource<O> sourceStage, final O outputPort) {
		sourceStage.setPipeForOutputPort(outputPort, this);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <I extends Enum<I>> T target(final ISink<I> targetStage, final I targetPort) {
		targetStage.setPipeForInputPort(targetPort, this);
		this.fireSignalClosing = new Runnable() {
			// This Runnable avoids the declaration of targetStage and targetPort as attributes and the declaration of I as type parameter
			public void run() {
				targetStage.onSignalClosing(targetPort);
			}
		};
		return (T) this;
	}

	protected abstract void putInternal(Object token);

	public final void put(final Object token) {
		this.putInternal(token);
	}

	protected abstract Object tryTakeInternal();

	public final Object tryTake() {
		final Object token = this.tryTakeInternal();
		return token;
	}

	public void fireSignalClosing() {
		this.fireSignalClosing.run();
	}

}
