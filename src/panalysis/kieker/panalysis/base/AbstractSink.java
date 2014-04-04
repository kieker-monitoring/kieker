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

package kieker.panalysis.base;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param I
 */
public abstract class AbstractSink<I extends Enum<I>> extends AbstractFilter<I, AbstractSink.OUTPUT_PORT> {

	static enum OUTPUT_PORT { // NOCS
		DUMMY // sink stages have not any output ports
	}

	public AbstractSink(final Class<I> inputEnumType) {
		super(inputEnumType, OUTPUT_PORT.class);
	}

}
