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
 */
public abstract class AbstractSource<OUTPUT_PORT extends Enum<OUTPUT_PORT>> extends AbstractFilter<AbstractSource.INPUT_PORT, OUTPUT_PORT> implements
		ISource<OUTPUT_PORT> {

	static protected enum INPUT_PORT {
		DUMMY // source stages have not any input ports
	}

	public AbstractSource(final Class<OUTPUT_PORT> enumType) {
		super(INPUT_PORT.class, enumType);
	}

}
