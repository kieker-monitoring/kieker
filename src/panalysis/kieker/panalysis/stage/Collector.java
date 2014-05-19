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

import java.util.Collection;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Collector<T> extends AbstractFilter<Collector<T>> {

	public final IInputPort<Collector<T>, T> objectInputPort = this.createInputPort();
	private Collection<T> objects;

	public Collector(final Collection<T> collection) {
		this.objects = collection;
	}

	public Collector() {
		super();
	}

	@Override
	protected boolean execute(final Context<Collector<T>> context) {
		final T object = context.tryTake(this.objectInputPort);
		if (object == null) {
			return false;
		}

		this.objects.add(object);

		return true;
	}

	public Collection<T> getObjects() {
		return this.objects;
	}

	public void setObjects(final Collection<T> objects) {
		this.objects = objects;
	}

}
