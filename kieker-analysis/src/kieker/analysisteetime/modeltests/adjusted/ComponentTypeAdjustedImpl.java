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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
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

		final Adapter adapter = new AdapterImpl() {

			// TODO add this to constructor
			private final EReference providedOperationsFeature = ArchitecturePackage.eINSTANCE.getComponentType_ProvidedOperations();

			@Override
			public void notifyChanged(final Notification notification) {

				final Object requiredFeature = this.providedOperationsFeature;

				if (notification.getFeature() == requiredFeature) {
					switch (notification.getEventType()) {
					case Notification.ADD:
						this.notifyOperationTypeAdded((OperationType) notification.getNewValue());
						break;
					case Notification.ADD_MANY:
						// TODO Check casting
						final List<OperationType> addedOperationTypes = (List<OperationType>) notification.getNewValue();
						addedOperationTypes.forEach(o -> this.notifyOperationTypeAdded(o));
						break;
					case Notification.REMOVE:
						this.notifyOperationTypeRemoved((OperationType) notification.getOldValue());
						break;
					case Notification.REMOVE_MANY:
						// TODO Check casting
						final List<OperationType> removedOperationTypes = (List<OperationType>) notification.getOldValue();
						removedOperationTypes.forEach(o -> this.notifyOperationTypeRemoved(o));
						break;
					default:
						break;
					}
				}

				super.notifyChanged(notification);
			}

			// TODO make abstract
			protected void notifyOperationTypeRemoved(final OperationType operationType) {
				// TODO do something
			}

			// TODO make abstract
			protected void notifyOperationTypeAdded(final OperationType operationType) {
				// TODO do something
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
