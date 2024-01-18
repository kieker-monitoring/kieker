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
package kieker.analysis.architecture;

import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import kieker.analysis.architecture.repository.ModelDescriptor;
import kieker.analysis.architecture.repository.ModelRepository;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @param <T>
 *            trigger record type
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class TriggerToModelSnapshotStage<T> extends AbstractConsumerStage<T> {

	private final OutputPort<ModelRepository> outputPort = this.createOutputPort(ModelRepository.class);

	private final ModelRepository repository;

	public TriggerToModelSnapshotStage(final ModelRepository repository) {
		this.repository = repository;
	}

	@Override
	protected void execute(final T element) throws Exception {
		final ModelRepository duplicateRepository = new ModelRepository(this.repository.getName());
		for (final Entry<EClass, EObject> entry : this.repository.getModels().entrySet()) {
			final EObject duplicateModel = EcoreUtil.copy(entry.getValue());
			final ModelDescriptor descriptor = this.repository.getModelDescriptor(entry.getKey());
			duplicateRepository.register(descriptor, duplicateModel);
		}
		this.outputPort.send(duplicateRepository);
	}

	public OutputPort<ModelRepository> getOutputPort() {
		return this.outputPort;
	}
}
