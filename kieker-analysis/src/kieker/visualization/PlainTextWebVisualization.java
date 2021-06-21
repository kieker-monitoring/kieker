/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.visualization;

/**
 * @author Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.9
 */
public final class PlainTextWebVisualization extends AbstractWebVisualizationFilterPlugin {

	/**
	 * The name of the input port receiving new events.
	 */
	public static final String INPUT_PORT_NAME_EVENTS = "events";

	private volatile Object currentObject;

	public PlainTextWebVisualization() {
		super();
	}

	@Override
	public String getHeader() {
		return "";
	}

	@Override
	public String getInitialContent() {
		return "N/A";
	}

	@Override
	public String getUpdatedContent() {
		if (this.currentObject != null) {
			return this.currentObject.toString();
		} else {
			return "N/A";
		}
	}

	/**
	 * This method represents the input port for the incoming objects.
	 *
	 * @param event
	 *            The new incoming object.
	 */
	@Override
	protected void execute(final Object event) {
		this.currentObject = event;
	}

}
