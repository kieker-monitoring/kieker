/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.view.util;


/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 * 
 * @param <T>
 *            The type of the content of this model instance.
 */
public class Model<T> {

	private T model;
	private String name;

	public Model(final T model, final String name) {
		this.model = model;
		this.name = name;
	}

	public T getModel() {
		return this.model;
	}

	public void setModel(final T model) {
		this.model = model;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
