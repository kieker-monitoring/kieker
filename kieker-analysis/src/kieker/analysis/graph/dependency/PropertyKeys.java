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

package kieker.analysis.graph.dependency;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class PropertyKeys {

	public static final String TYPE = "type";

	public static final String NAME = "name";

	public static final String PACKAGE_NAME = "package name";

	public static final String RETURN_TYPE = "return type";

	public static final String MODIFIERS = "modifiers";

	public static final String PARAMETER_TYPES = "parameter types";

	public static final String CALLS = "calls";

	public static final String MIN_REPSONSE_TIME = "min response time";

	public static final String MAX_REPSONSE_TIME = "max response time";

	public static final String MEAN_REPSONSE_TIME = "mean response time";

	public static final String MEDIAN_REPSONSE_TIME = "median response time";

	public static final String TOTAL_RESPONSE_TIME = "total response time";

	public static final String TIME_UNIT = "time unit";

	private PropertyKeys() {}

}
