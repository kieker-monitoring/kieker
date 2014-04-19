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

package kieker.panalysis.stage.visualization;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;

/**
 * @author Nils Christian Ehmke, Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <I>
 *            The type of the input ports
 */
public class PlainTextWebVisualizationSink<T> extends AbstractFilter<PlainTextWebVisualizationSink<T>> implements IWebVisualizationSink {

	public final IInputPort<PlainTextWebVisualizationSink<T>, T> INPUT_OBJECT = this.createInputPort();

	private Object currentObject = "N/A";

	/**
	 * @since 1.10
	 */
	public String getHeader() {
		return "";
	}

	/**
	 * @since 1.10
	 */
	public String getInitialContent() {
		return this.currentObject.toString();
	}

	/**
	 * @since 1.10
	 */
	public String getUpdatedContent() {
		return this.currentObject.toString();
	}

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<PlainTextWebVisualizationSink<T>> context) {
		final T object = this.tryTake(this.INPUT_OBJECT);
		if (object == null) {
			return false;
		}

		this.currentObject = object;

		return true;
	}

}
