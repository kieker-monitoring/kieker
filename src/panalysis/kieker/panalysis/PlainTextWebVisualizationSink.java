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

import kieker.panalysis.base.AbstractWebVisualizationSink;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 * 
 * @param <I>
 *            The type of the input ports
 */
public class PlainTextWebVisualizationSink extends AbstractWebVisualizationSink<PlainTextWebVisualizationSink.INPUT_PORT> {

	public static enum INPUT_PORT { // NOCS
		OBJECT
	}

	private Object currentObject = "N/A";

	public PlainTextWebVisualizationSink(final Class<INPUT_PORT> inputEnumType) {
		super(inputEnumType);
	}

	public String getHeader() {
		return "";
	}

	public String getInitialContent() {
		return this.currentObject.toString();
	}

	public String getUpdatedContent() {
		return this.currentObject.toString();
	}

	public boolean execute() {
		final Object object = this.tryTake(INPUT_PORT.OBJECT);
		if (object == null) {
			return false;
		}

		this.currentObject = object;

		return true;
	}

}
