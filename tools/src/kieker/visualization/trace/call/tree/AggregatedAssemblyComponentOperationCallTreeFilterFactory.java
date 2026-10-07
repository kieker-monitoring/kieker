/***************************************************************************
 * Copyright 2026 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace.call.tree;

import kieker.model.repository.SystemModelRepository;
import kieker.visualization.trace.call.tree.dot.AggregatedAssemblyComponentOperationCallTreeFilter;

/**
 * A factory-class to create
 * {@link AggregatedAssemblyComponentOperationCallTreeFilter}, either with graph
 * format {@code DOT} or {@code PLANTUML}.
 * 
 * @author Yorrick Josuttis
 */
public final class AggregatedAssemblyComponentOperationCallTreeFilterFactory {

    private AggregatedAssemblyComponentOperationCallTreeFilterFactory() {
        // factory class
    }

    public static AggregatedAssemblyComponentOperationCallTreeFilter createDotFilter(
            final SystemModelRepository repository, final boolean includeWeights, final boolean shortLabels,
            final String dotOutputFile) {
        return new AggregatedAssemblyComponentOperationCallTreeFilter(repository, includeWeights, shortLabels,
                dotOutputFile, GraphFormat.DOT);
    }

    public static AggregatedAssemblyComponentOperationCallTreeFilter createPlantUMLFilter(
            final SystemModelRepository repository, final boolean includeWeights, final boolean shortLabels,
            final String dotOutputFile) {
        return new AggregatedAssemblyComponentOperationCallTreeFilter(repository, includeWeights, shortLabels,
                dotOutputFile, GraphFormat.PLANTUML);
    }

}
