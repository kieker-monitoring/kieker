/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader.list;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractProducerStage;

/**
 * Helper class that reads records added using the methods {@link #addAllObjects(List)} or {@link #addObject(Object)}.
 *
 * Additions after this reader starts reading are ignored.
 *
 * @param <T>
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.6
 */
public class ListReader<T> extends AbstractProducerStage<T> {

	private final List<T> objects = new ArrayList<T>();

	/**
	 * Empty default constructor.
	 */
	public ListReader() {
		// empty default constructor
	}

	/**
	 * This method adds all given records to our list.
	 *
	 * @param records
	 *            The records to be added.
	 */
	public void addAllObjects(final List<T> records) {
		this.objects.addAll(records);
	}

	/**
	 * This method adds the given object to our list.
	 *
	 * @param object
	 *            The object to be added.
	 */
	public void addObject(final T object) {
		this.objects.add(object);
	}

	@Override
	protected void execute() {
		for (final T obj : this.objects) {
			this.outputPort.send(obj);
		}
	}

}
