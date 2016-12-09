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

package kieker.test.analysisteetime.junit.modeltooling;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.analysisteetime.modeltooling.EReferenceChangedListener;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class EReferenceChangedListenerTest {

	private final ArchitectureFactory architectureFactory = ArchitectureFactory.eINSTANCE;
	private final ArchitecturePackage architecturePackage = ArchitecturePackage.eINSTANCE;

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceChangedListener#notifyElementAdded()}.
	 */
	@Test
	public void testNotificationOnOneAdded() {

		final ComponentType componentType = this.architectureFactory.createComponentType();
		final EReference providedOperationsReference = this.architecturePackage.getComponentType_ProvidedOperations();

		final WrappedNumber elementsAdded = new WrappedNumber(0);
		final WrappedNumber elementsRemoved = new WrappedNumber(0);

		final EReferenceChangedListener<OperationType> listener = new EReferenceChangedListener<OperationType>(providedOperationsReference) {

			@Override
			protected void notifyElementAdded(final OperationType element) {
				elementsAdded.set(elementsAdded.get().intValue() + 1);
			}

			@Override
			protected void notifyElementRemoved(final OperationType element) {
				elementsRemoved.set(elementsRemoved.get().intValue() + 1);
			}
		};
		componentType.eAdapters().add(listener);

		final OperationType operationType = this.architectureFactory.createOperationType();
		componentType.getProvidedOperations().add(operationType);

		Assert.assertEquals(1, elementsAdded.get());
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceChangedListener#notifyElementAdded()}.
	 */
	@Test
	public void testNotificationOnOneAddedReverse() {

		final ComponentType componentType = this.architectureFactory.createComponentType();
		final EReference providedOperationsReference = this.architecturePackage.getComponentType_ProvidedOperations();

		final WrappedNumber elementsAdded = new WrappedNumber(0);
		final WrappedNumber elementsRemoved = new WrappedNumber(0);

		final EReferenceChangedListener<OperationType> listener = new EReferenceChangedListener<OperationType>(providedOperationsReference) {

			@Override
			protected void notifyElementAdded(final OperationType element) {
				elementsAdded.set(elementsAdded.get().intValue() + 1);
			}

			@Override
			protected void notifyElementRemoved(final OperationType element) {
				elementsRemoved.set(elementsRemoved.get().intValue() + 1);
			}
		};
		componentType.eAdapters().add(listener);

		final OperationType operationType = this.architectureFactory.createOperationType();
		operationType.setComponentType(componentType);

		Assert.assertEquals(1, elementsAdded.get());
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceChangedListener#notifyElementAdded()}.
	 */
	@Test
	public void testNotificationOnMultipleAdded() {

		final ComponentType componentType = this.architectureFactory.createComponentType();
		final EReference providedOperationsReference = this.architecturePackage.getComponentType_ProvidedOperations();

		final WrappedNumber elementsAdded = new WrappedNumber(0);
		final WrappedNumber elementsRemoved = new WrappedNumber(0);

		final EReferenceChangedListener<OperationType> listener = new EReferenceChangedListener<OperationType>(providedOperationsReference) {

			@Override
			protected void notifyElementAdded(final OperationType element) {
				elementsAdded.set(elementsAdded.get().intValue() + 1);
			}

			@Override
			protected void notifyElementRemoved(final OperationType element) {
				elementsRemoved.set(elementsRemoved.get().intValue() + 1);
			}
		};
		componentType.eAdapters().add(listener);

		final OperationType operationType1 = this.architectureFactory.createOperationType();
		final OperationType operationType2 = this.architectureFactory.createOperationType();
		final OperationType operationType3 = this.architectureFactory.createOperationType();
		final List<OperationType> operationTypes = Arrays.asList(operationType1, operationType2, operationType3);
		componentType.getProvidedOperations().addAll(operationTypes);

		Assert.assertEquals(3, elementsAdded.get());
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceChangedListener#notifyElementAdded()}.
	 */
	@Test
	public void testNotificationOnOneRemoved() {

		final ComponentType componentType = this.architectureFactory.createComponentType();
		final EReference providedOperationsReference = this.architecturePackage.getComponentType_ProvidedOperations();

		final WrappedNumber elementsAdded = new WrappedNumber(0);
		final WrappedNumber elementsRemoved = new WrappedNumber(0);

		final EReferenceChangedListener<OperationType> listener = new EReferenceChangedListener<OperationType>(providedOperationsReference) {

			@Override
			protected void notifyElementAdded(final OperationType element) {
				elementsAdded.set(elementsAdded.get().intValue() + 1);
			}

			@Override
			protected void notifyElementRemoved(final OperationType element) {
				elementsRemoved.set(elementsRemoved.get().intValue() + 1);
			}
		};
		componentType.eAdapters().add(listener);

		final OperationType operationType1 = this.architectureFactory.createOperationType();
		final OperationType operationType2 = this.architectureFactory.createOperationType();
		final OperationType operationType3 = this.architectureFactory.createOperationType();
		final List<OperationType> operationTypes = Arrays.asList(operationType1, operationType2, operationType3);

		componentType.getProvidedOperations().addAll(operationTypes);

		componentType.getProvidedOperations().remove(operationType2);

		Assert.assertEquals(1, elementsRemoved.get());
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceChangedListener#notifyElementAdded()}.
	 */
	@Test
	public void testNotificationOnMultipleRemoved() {

		final ComponentType componentType = this.architectureFactory.createComponentType();
		final EReference providedOperationsReference = this.architecturePackage.getComponentType_ProvidedOperations();

		final WrappedNumber elementsAdded = new WrappedNumber(0);
		final WrappedNumber elementsRemoved = new WrappedNumber(0);

		final EReferenceChangedListener<OperationType> listener = new EReferenceChangedListener<OperationType>(providedOperationsReference) {

			@Override
			protected void notifyElementAdded(final OperationType element) {
				elementsAdded.set(elementsAdded.get().intValue() + 1);
			}

			@Override
			protected void notifyElementRemoved(final OperationType element) {
				elementsRemoved.set(elementsRemoved.get().intValue() + 1);
			}
		};
		componentType.eAdapters().add(listener);

		final OperationType operationType1 = this.architectureFactory.createOperationType();
		final OperationType operationType2 = this.architectureFactory.createOperationType();
		final OperationType operationType3 = this.architectureFactory.createOperationType();
		final List<OperationType> allOperationTypes = Arrays.asList(operationType1, operationType2, operationType3);

		componentType.getProvidedOperations().addAll(allOperationTypes);

		final List<OperationType> twoOperationTypes = Arrays.asList(operationType1, operationType2);
		componentType.getProvidedOperations().removeAll(twoOperationTypes);

		Assert.assertEquals(2, elementsRemoved.get());
	}

	private static class WrappedNumber {
		private Number value;

		public WrappedNumber(final Number value) {
			this.value = value;
		}

		public Number get() {
			return this.value;
		}

		public void set(final Number value) {
			this.value = value;
		}

	}

}
