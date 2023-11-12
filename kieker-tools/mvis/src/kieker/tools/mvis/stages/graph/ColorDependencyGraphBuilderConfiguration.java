/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.tools.mvis.stages.graph;

import org.oceandsl.analysis.graph.IGraphElementSelector;

import kieker.tools.mvis.graph.IColorDependencyGraphBuilderConfiguration;

/**
 * @author Reiner Jung
 * @since 1.1
 *
 */
public class ColorDependencyGraphBuilderConfiguration implements IColorDependencyGraphBuilderConfiguration {

    private final IGraphElementSelector selector;

    public ColorDependencyGraphBuilderConfiguration(final IGraphElementSelector selector) {
        this.selector = selector;
    }

    @Override
    public IGraphElementSelector getSelector() {
        return this.selector;
    }

}
