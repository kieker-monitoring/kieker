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
package kieker.analysis.plugin.reader.newio;

/**
 * Enumeration for outcomes of event handlers and other operations.
 *
 * @author Holger Knoche
 * @since 1.13
 * @deprecated since 1.15.1 old plugin api
 */
public enum Outcome {
	/**
	 * Denotes that the operation has completed successfully.
	 */
	SUCCESS,
	/**
	 * Denotes that the operation has failed.
	 */
	FAILURE;
}