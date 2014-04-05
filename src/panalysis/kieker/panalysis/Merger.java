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

package kieker.panalysis;

import kieker.panalysis.base.AbstractFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Merger extends AbstractFilter<Merger.INPUT_PORT, Merger.OUTPUT_PORT> {

	public static enum INPUT_PORT { // NOCS
		INPUT0, INPUT1
	}

	public static enum OUTPUT_PORT { // NOCS
		OBJECT
	}

	private final INPUT_PORT[] inputPorts;
	private int index = 0;

	public Merger() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
		this.inputPorts = INPUT_PORT.values();
	}

	public void execute() {
		int size = this.inputPorts.length;
		// check each port at most once to avoid a potentially infinite loop
		while (size-- > 0) {
			final INPUT_PORT inputPort = this.getNextPortInRoundRobinOrder();
			final Object object = this.tryTake(inputPort);
			if (object != null) {
				this.put(OUTPUT_PORT.OBJECT, object);
				return;
			}
		}
	}

	private INPUT_PORT getNextPortInRoundRobinOrder() {
		final INPUT_PORT port = this.inputPorts[this.index];
		this.index = (this.index + 1) % this.inputPorts.length;
		return port;
	}

}
