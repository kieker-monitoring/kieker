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

import java.io.PrintStream;

import kieker.panalysis.examples.countWords.AbstractDefaultFilter;

/**
 * @author Matthias Rohr, Jan Waller, Nils Christian Ehmke, Christian Wulf
 * 
 * @since 1.10
 */
public final class TeeFilter extends AbstractDefaultFilter<TeeFilter> {

	public static enum INPUT_PORT { // NOCS
		INPUT_OBJECT
	}

	public static enum OUTPUT_PORT { // NOCS
		RELAYED_OBJECT
	}

	private final PrintStream printStream;

	public TeeFilter() {
		this.printStream = System.out;
	}

	public boolean execute() {
		final Object inputObject = super.tryTake(INPUT_PORT.INPUT_OBJECT);

		final StringBuilder sb = new StringBuilder(128);
		sb.append(super.getId()).append('(').append(inputObject.getClass().getSimpleName()).append(") ").append(inputObject.toString());
		this.printStream.println(sb.toString());

		super.put(OUTPUT_PORT.RELAYED_OBJECT, inputObject);
		return true;
	}

}
