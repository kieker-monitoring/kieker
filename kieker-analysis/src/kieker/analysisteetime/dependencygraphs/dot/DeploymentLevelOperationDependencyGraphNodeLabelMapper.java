/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.dependencygraphs.dot;

import java.util.function.Function;

import kieker.analysisteetime.dependencygraphs.PropertyKeys;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DeploymentLevelOperationDependencyGraphNodeLabelMapper implements Function<Vertex, String> {

	@Override
	public String apply(final Vertex vertex) {
		final StringBuilder builder = new StringBuilder();

		final String type = vertex.getProperty(PropertyKeys.TYPE);
		final String name = vertex.getProperty(PropertyKeys.NAME);

		final String timeUnit = vertex.getProperty(PropertyKeys.TIME_UNIT);
		final String minResponseTime = vertex.getProperty(PropertyKeys.MIN_REPSONSE_TIME);
		final String maxResponseTime = vertex.getProperty(PropertyKeys.MAX_REPSONSE_TIME);
		final String totalResponseTime = vertex.getProperty(PropertyKeys.TOTAL_REPSONSE_TIME);
		final String meanResponseTime = vertex.getProperty(PropertyKeys.MEAN_REPSONSE_TIME);
		final String medianResponseTime = vertex.getProperty(PropertyKeys.MEDIAN_REPSONSE_TIME);

		return builder.toString();
	}

}
