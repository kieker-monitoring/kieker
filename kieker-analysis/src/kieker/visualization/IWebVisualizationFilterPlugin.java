/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
 * This is the interface for web visualization filter plugins within Kieker.
 *
 * @author Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.9
 */
public interface IWebVisualizationFilterPlugin {

	/**
	 * @return returns header string
	 *
	 * @since 1.9
	 */
	public String getHeader();

	/**
	 * @return returns the initial content
	 *
	 * @since 1.9
	 */
	public String getInitialContent();

	/**
	 * @return returns the updated content
	 *
	 * @since 1.9
	 */
	public String getUpdatedContent();

}
