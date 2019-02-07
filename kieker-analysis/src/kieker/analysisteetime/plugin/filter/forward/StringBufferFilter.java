/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.filter.forward;

import kieker.analysis.plugin.filter.forward.util.KiekerHashMap;

import teetime.stage.basic.AbstractFilter;

/**
 * This filter has exactly one input port and one output port.
 *
 * Every record received is cloned and each detected String is buffered in a shared area in order to save memory.
 *
 * @author Jan Waller, Lars Bluemke
 *
 *         TODO this filter does not work anymore without the array API, also it is unclear if it is useful, as all
 *         strings in incoming records originate from a string registry. Therefore, they are already unique.
 *
 * @since 1.6
 * @deprecated 1.15 remove in 1.16
 */
@Deprecated
public class StringBufferFilter extends AbstractFilter<Object> {

	private final KiekerHashMap kiekerHashMap = new KiekerHashMap();

	/**
	 * Default constructor.
	 */
	public StringBufferFilter() {
		// empty default constructor
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void execute(final Object object) {
		if (object instanceof String) {
			this.outputPort.send(this.kiekerHashMap.get((String) object));
		} else { // simply forward the object
			this.outputPort.send(object);
		}
	}

}
