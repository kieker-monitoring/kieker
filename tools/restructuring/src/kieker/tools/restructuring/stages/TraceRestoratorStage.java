/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring.stages;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.tools.restructuring.EMappingStrategy;
import kieker.tools.restructuring.mapper.BasicComponentMapper;
import kieker.tools.restructuring.mapper.ComponentsMapper;
import kieker.tools.restructuring.mapper.EmptyMapper;
import kieker.tools.restructuring.mapper.KuhnMatcherMapper;
import kieker.tools.restructuring.mapper.RandomMapper;
import kieker.tools.restructuring.util.TransformationFactory;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public class TraceRestoratorStage extends AbstractConsumerStage<ModelRepository> {

	protected final OutputPort<BasicComponentMapper> compMapperOutputPort = this.createOutputPort(); // original

	private final EMappingStrategy strat;

	private ModelRepository goal;
	private ModelRepository original;

	public TraceRestoratorStage(final EMappingStrategy strat) {
		this.strat = strat;
	}

	public OutputPort<BasicComponentMapper> getOutputPort() {
		return this.compMapperOutputPort;
	}

	@Override
	protected void execute(final ModelRepository repository) throws Exception {
		if (this.original == null) {
			this.original = repository;
		} else {
			this.goal = repository;
			this.logger.info("Processing {} -> {}", this.original.getName(), this.goal.getName());
			final AssemblyModel o = this.original.getModel(AssemblyPackage.eINSTANCE.getAssemblyModel());
			final AssemblyModel g = this.goal.getModel(AssemblyPackage.eINSTANCE.getAssemblyModel());
			final BasicComponentMapper mapper;
			switch (this.strat) {
			case EMPTY:
				mapper = new EmptyMapper(o, g, this.original.getName(), this.goal.getName());
				break;
			case RANDOM:
				mapper = new RandomMapper(o, g, this.original.getName(), this.goal.getName());
				break;
			case KUHN:
				mapper = new KuhnMatcherMapper(o, g, this.original.getName(), this.goal.getName());
				break;
			case NORMAL:
			default:
				mapper = new ComponentsMapper(o, g, this.original.getName(), this.goal.getName());
				break;
			}

			if (TransformationFactory.areSameModels(o, g)) {
				this.logger.error("Identical models. Nothing to do.");
			} else {
				this.compMapperOutputPort.send(mapper);
			}
		}
	}

}
