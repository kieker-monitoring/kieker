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
public abstract class AbstractPipe<T, P extends IPipe<T, P>> implements IPipe<T, P> {

	private Runnable fireSignalClosing;

	@SuppressWarnings("unchecked")
	public <S extends ISource> P source(final IOutputPort<S, T> sourcePort) {
		sourcePort.setAssociatedPipe(this);
		return (P) this;
	}

	@SuppressWarnings("unchecked")
	public <S extends ISink<S>> P target(final S targetStage, final IInputPort<S, T> targetPort) {
		targetPort.setAssociatedPipe(this);
		this.fireSignalClosing = new Runnable() {
			// This Runnable avoids the declaration of targetStage and targetPort as attributes and the declaration of I as type parameter
			public void run() {
				targetStage.onSignalClosing(targetPort);
			}
		};
		return (P) this;
	}

	@SuppressWarnings("unchecked")
	public <S extends ISink<S>> P target(final IInputPort<S, T> targetPort) {
		targetPort.setAssociatedPipe(this);
		this.fireSignalClosing = new Runnable() {
			// This Runnable avoids the declaration of targetStage and targetPort as attributes and the declaration of I as type parameter
			public void run() {
				targetPort.getOwningStage().onSignalClosing(targetPort);
			}
		};
		return (P) this;
	}

	// TODO remove if it does not add any new functionality
	protected abstract void putInternal(T token);

	public void put(final T token) {
		this.putInternal(token);
	}

	// TODO remove if it does not add any new functionality
	protected abstract T tryTakeInternal();

	public final T tryTake() {
		return this.tryTakeInternal();
	}

	public void fireSignalClosing() {
		this.fireSignalClosing.run();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.fireSignalClosing == null) ? 0 : this.fireSignalClosing.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		final AbstractPipe other = (AbstractPipe) obj;
		if (this.fireSignalClosing == null) {
			if (other.fireSignalClosing != null) {
				return false;
			}
		} else if (!this.fireSignalClosing.equals(other.fireSignalClosing)) {
			return false;
		}
		return true;
	}

}
