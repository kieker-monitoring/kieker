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
package kieker.panalysis.stage.basic;

import java.util.concurrent.Callable;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ObjectProducer<T> extends AbstractFilter<ObjectProducer<T>> {

	public final IOutputPort<ObjectProducer<T>, T> outputPort = this.createOutputPort();

	private Callable<T> objectCreator;
	private long numObjectsToCreate;

	/**
	 * @since 1.10
	 */
	public ObjectProducer(final long numObjectsToCreate, final Callable<T> objectCreator) {
		this.numObjectsToCreate = numObjectsToCreate;
		this.objectCreator = objectCreator;
	}

	/**
	 * @since 1.10
	 */
	public ObjectProducer() {
		super();
	}

	@Override
	protected boolean execute(final Context<ObjectProducer<T>> context) {
		if (this.numObjectsToCreate == 0) {
			return false;
		}

		try {
			final T newObject = this.objectCreator.call();
			context.put(this.outputPort, newObject);
		} catch (final Exception e) {
			throw new IllegalStateException();
		}

		this.numObjectsToCreate--;

		return true;
	}

	public Callable<T> getObjectCreator() {
		return this.objectCreator;
	}

	public void setObjectCreator(final Callable<T> objectCreator) {
		this.objectCreator = objectCreator;
	}

	public long getNumObjectsToCreate() {
		return this.numObjectsToCreate;
	}

	public void setNumObjectsToCreate(final long numObjectsToCreate) {
		this.numObjectsToCreate = numObjectsToCreate;
	}

}
