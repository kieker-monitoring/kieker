/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior.clustering;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.clustering.OpticsData;
import kieker.analysis.generic.graph.clustering.OpticsData.OPTICSDataGED;

import teetime.stage.basic.AbstractTransformation;

/**
 * Converts Behavior Models to Optics Data objects. This is necessary for the optics algorithm.
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class BehaviorModelToOpticsDataTransformation<N extends INode, E extends IEdge> extends AbstractTransformation<IGraph<N, E>, OpticsData<N, E>> {

	private final OPTICSDataGED<N, E> opticsGed;

	public BehaviorModelToOpticsDataTransformation(final OPTICSDataGED<N, E> opticsGed) {
		this.opticsGed = opticsGed;
	}

	@Override
	protected void execute(final IGraph<N, E> model) throws Exception {
		this.outputPort.send(new OpticsData<>(model.getGraph(), this.opticsGed));
		this.logger.debug("Converted BehaviorModel to OpticsData");
	}

}
