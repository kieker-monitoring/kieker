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
package kieker.panalysis.stage;

import java.util.LinkedList;
import java.util.List;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Cache<T> extends AbstractFilter<Cache<T>> {

	private static enum OperationMode {
		CACHING, SENDING
	}

	private OperationMode operationMode = OperationMode.CACHING;

	public final IInputPort<Cache<T>, T> objectInputPort = this.createInputPort();
	public final IInputPort<Cache<T>, Boolean> sendInputPort = this.createInputPort();

	public final IOutputPort<Cache<T>, T> objectOutputPort = this.createOutputPort();

	private final List<T> cachedObjects = new LinkedList<T>();

	@Override
	protected boolean execute(final Context<Cache<T>> context) {
		if (this.operationMode == OperationMode.CACHING) {
			final T object = context.tryTake(this.objectInputPort);
			if (object == null) {
				final Boolean shouldSend = context.tryTake(this.sendInputPort);
				if (shouldSend == null) {
					return false;
				} else {
					this.operationMode = OperationMode.SENDING;
				}
			} else {
				this.cache(object);
			}
		} else {
			final boolean sent = this.send(context);
			if (!sent) {
				this.operationMode = OperationMode.CACHING;
			}
		}

		return true;
	}

	/**
	 * @since 1.10
	 */
	private boolean send(final Context<Cache<T>> context) {
		if (this.cachedObjects.size() > 0) {
			final T cachedObject = this.cachedObjects.remove(0);
			context.put(this.objectOutputPort, cachedObject);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @since 1.10
	 */
	private void cache(final T object) {
		this.cachedObjects.add(object);
	}

}
