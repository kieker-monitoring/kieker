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
package kieker.analysis.behavior.clustering;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.behavior.mtree.MTree;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * The stage which execute the optics algorithm.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class OpticsStage extends AbstractStage {
	private static final Logger LOGGER = LoggerFactory.getLogger(OpticsStage.class);

	private final OutputPort<List<OpticsData>> outputPort = this.createOutputPort();

	private final InputPort<MTree<OpticsData>> mTreeInputPort = this.createInputPort();
	private final InputPort<List<OpticsData>> modelsInputPort = this.createInputPort();

	private final Queue<MTree<OpticsData>> mTreeInputQueue = new LinkedList<>();
	private final Queue<List<OpticsData>> modelsInputQueue = new LinkedList<>();

	private final double epsilon;
	private final int minPTs;

	public OpticsStage(final double epsilon, final int minPTs) {
		this.minPTs = minPTs;
		this.epsilon = epsilon;
	}

	@Override
	protected void execute() throws Exception {
		final MTree<OpticsData> newMTree = this.mTreeInputPort.receive();
		final List<OpticsData> newModels = this.modelsInputPort.receive();

		if (newMTree != null) {
			this.mTreeInputQueue.add(newMTree);
		}
		if (newModels != null) {
			this.modelsInputQueue.add(newModels);
		}

		// We need the list of all objects and the MTree with all objects for the algorithm
		if (!this.mTreeInputQueue.isEmpty() && !this.modelsInputQueue.isEmpty()) {
			OpticsStage.LOGGER.info("received models and mtrees, begins to calculate optics result");
			final MTree<OpticsData> mtree = this.mTreeInputQueue.poll();
			final List<OpticsData> models = this.modelsInputQueue.poll();

			final OPTICS optics = new OPTICS(mtree, this.epsilon, this.minPTs, models);

			final List<OpticsData> result = optics.calculate();

			OpticsStage.LOGGER.info("send optics result");

			this.outputPort.send(result);
		}

	}

	public OutputPort<List<OpticsData>> getOutputPort() {
		return this.outputPort;
	}

	public InputPort<MTree<OpticsData>> getmTreeInputPort() {
		return this.mTreeInputPort;
	}

	public InputPort<List<OpticsData>> getModelsInputPort() {
		return this.modelsInputPort;
	}

}
