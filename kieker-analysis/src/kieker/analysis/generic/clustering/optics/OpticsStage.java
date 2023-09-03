/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.generic.clustering.optics;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kieker.analysis.generic.clustering.mtree.MTree;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * The stage which execute the optics algorithm.
 *
 * @param <T>
 *            optics data typr
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class OpticsStage<T> extends AbstractStage {

	private final OutputPort<List<OpticsData<T>>> outputPort = this.createOutputPort();

	private final InputPort<MTree<OpticsData<T>>> mTreeInputPort = this.createInputPort();
	private final InputPort<List<OpticsData<T>>> modelsInputPort = this.createInputPort();

	private final Queue<MTree<OpticsData<T>>> mTreeInputQueue = new LinkedList<>();
	private final Queue<List<OpticsData<T>>> modelsInputQueue = new LinkedList<>();

	private final double epsilon;
	private final int minPTs;

	public OpticsStage(final double epsilon, final int minPTs) {
		this.minPTs = minPTs;
		this.epsilon = epsilon;
	}

	@Override
	protected void execute() throws Exception {
		final MTree<OpticsData<T>> newMTree = this.mTreeInputPort.receive();
		final List<OpticsData<T>> newModels = this.modelsInputPort.receive();

		if (newMTree != null) {
			this.mTreeInputQueue.add(newMTree);
		}
		if (newModels != null) {
			this.modelsInputQueue.add(newModels);
		}

		// We need the list of all objects and the MTree with all objects for the algorithm
		if (!this.mTreeInputQueue.isEmpty() && !this.modelsInputQueue.isEmpty()) {
			this.logger.debug("received models and mtrees, begins to calculate optics result");
			final MTree<OpticsData<T>> mtree = this.mTreeInputQueue.poll();
			final List<OpticsData<T>> models = this.modelsInputQueue.poll();

			final OPTICS<T> optics = new OPTICS<>(mtree, this.epsilon, this.minPTs, models);

			final List<OpticsData<T>> result = optics.calculate();

			this.logger.debug("Send optics results {}", result.size());

			this.outputPort.send(result);
		}

	}

	public OutputPort<List<OpticsData<T>>> getOutputPort() {
		return this.outputPort;
	}

	public InputPort<MTree<OpticsData<T>>> getMTreeInputPort() {
		return this.mTreeInputPort;
	}

	public InputPort<List<OpticsData<T>>> getModelsInputPort() {
		return this.modelsInputPort;
	}

}
