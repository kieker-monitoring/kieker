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

package kieker.tools.traceAnalysis.filter;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.filter.visualization.graph.IOriginRetentionPolicy;

/**
 * Interface for graph-producing filters.
 * 
 * @author Holger Knoche
 * 
 * @param <G>
 *            The type of the produced graph
 * 
 * @since 1.6
 */
public interface IGraphProducingFilter<G extends AbstractGraph<?, ?, ?>> extends IGraphOutputtingFilter<G> {

	/**
	 * Requests that the given retention policy is used by this graph producer. Note that the producer may
	 * choose to use a more liberal, compatible retention policy to satisfy the needs of other graph consumers
	 * (see {@link kieker.tools.traceAnalysis.filter.visualization.graph.IOriginRetentionPolicy#isCompatibleWith(IOriginRetentionPolicy)}).
	 * 
	 * @param policy
	 *            The requested policy
	 * @throws AnalysisConfigurationException
	 *             If an error occurs during the request
	 * 
	 * @since 1.6
	 */
	public void requestOriginRetentionPolicy(IOriginRetentionPolicy policy) throws AnalysisConfigurationException;

}
