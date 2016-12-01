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

package kieker.analysisteetime.modeltests.adjusted;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EReference;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ComponentTypeAdjustedImpl extends ComponentTypeImpl implements ComponentTypeAdjusted {

	private final Map<String, OperationType> operationTypeRepository = new HashMap<>();

	public ComponentTypeAdjustedImpl() {

		final EReference providedOperationsFeature = ArchitecturePackage.eINSTANCE.getComponentType_ProvidedOperations();

		final Adapter adapter = new ReferenceChangedListener<OperationType>(providedOperationsFeature) {

			// TODO react also on changes of name of operation

			@Override
			protected void notifyOperationTypeAdded(final OperationType operationType) {
				ComponentTypeAdjustedImpl.this.operationTypeRepository.put(operationType.getSignature(), operationType);
			}

			@Override
			protected void notifyOperationTypeRemoved(final OperationType operationType) {
				ComponentTypeAdjustedImpl.this.operationTypeRepository.remove(operationType.getSignature());
			}
		};

		this.eAdapters().add(adapter);
	}

	@Override
	public OperationType getOperationTypeByName(final String name) {
		return this.operationTypeRepository.get(name);
	}

	@Override
	public boolean containsOperationTypeByName(final String name) {
		return this.operationTypeRepository.containsKey(name);
	}

	@Override
	public void addOperationTypeByName(final String name) {
		// Currently not supported, not necessarily required
		throw new UnsupportedOperationException();
	}

}
