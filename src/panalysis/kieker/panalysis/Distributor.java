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
public class Distributor extends AbstractFilter<Distributor.INPUT_PORT, Distributor.OUTPUT_PORT> {

	public static enum INPUT_PORT { // NOCS
		OBJECT
	}

	public static enum OUTPUT_PORT { // NOCS
		OUTPUT0, OUTPUT1
	}

	private final OUTPUT_PORT[] outputPorts;
	private int index = 0;

	public Distributor() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
		this.outputPorts = OUTPUT_PORT.values();
	}

	public void execute() {
		final Object object = this.take(INPUT_PORT.OBJECT);
		System.out.println("distributor takes " + object);
		final OUTPUT_PORT port = this.getNextPortInRoundRobinOrder();
		this.put(port, object);
	}

	private OUTPUT_PORT getNextPortInRoundRobinOrder() {
		final OUTPUT_PORT port = this.outputPorts[this.index];
		this.index = (this.index + 1) % this.outputPorts.length;
		return port;
	}
}
