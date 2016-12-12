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

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.TraceFactory;
import kieker.analysisteetime.model.analysismodel.trace.TracePackage;
import kieker.analysisteetime.modeltooling.EStructuralFeatureChangedListener;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class EStructuralFeatureChangedListenerTest {

	private final TraceFactory traceFactory = TraceFactory.eINSTANCE;
	private final TracePackage tracePackage = TracePackage.eINSTANCE;
	private final DeploymentFactory deploymentFactory = DeploymentFactory.eINSTANCE;

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EStructuralFeatureChangedListener#notifyChanged(org.eclipse.emf.common.notify.Notification)}.
	 */
	@Test
	public void testNoChangedNotification() {

		final OperationCall operationCall = this.traceFactory.createOperationCall();
		final EAttribute startAttribute = this.tracePackage.getOperationCall_Start();

		final WrappedBoolean startValueChanged = new WrappedBoolean(false);

		final EStructuralFeatureChangedListener<OperationCall> listener = new EStructuralFeatureChangedListener<OperationCall>(Arrays.asList(startAttribute)) {
			@Override
			protected void notifyChanged(final OperationCall object) {
				startValueChanged.set(true);
			}
		};
		operationCall.eAdapters().add(listener);

		operationCall.setDuration(Duration.ofMillis(1));

		Assert.assertFalse(startValueChanged.get());
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EStructuralFeatureChangedListener#notifyChanged(org.eclipse.emf.common.notify.Notification)}.
	 */
	@Test
	public void testAttributeChangedNotification() {

		final OperationCall operationCall = this.traceFactory.createOperationCall();
		final EAttribute startAttribute = this.tracePackage.getOperationCall_Start();

		final WrappedBoolean startValueChanged = new WrappedBoolean(false);

		final EStructuralFeatureChangedListener<OperationCall> listener = new EStructuralFeatureChangedListener<OperationCall>(Arrays.asList(startAttribute)) {
			@Override
			protected void notifyChanged(final OperationCall object) {
				startValueChanged.set(true);
			}
		};
		operationCall.eAdapters().add(listener);

		operationCall.setStart(Instant.now());

		Assert.assertTrue(startValueChanged.get());
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EStructuralFeatureChangedListener#notifyChanged(org.eclipse.emf.common.notify.Notification)}.
	 */
	@Test
	public void testReferenceChangedNotification() {

		final OperationCall operationCall = this.traceFactory.createOperationCall();
		final EReference componentAttribute = this.tracePackage.getOperationCall_Component();

		final WrappedBoolean startValueChanged = new WrappedBoolean(false);

		final EStructuralFeatureChangedListener<OperationCall> listener = new EStructuralFeatureChangedListener<OperationCall>(Arrays.asList(componentAttribute)) {
			@Override
			protected void notifyChanged(final OperationCall object) {
				startValueChanged.set(true);
			}
		};
		operationCall.eAdapters().add(listener);

		final DeployedComponent component = this.deploymentFactory.createDeployedComponent();
		operationCall.setComponent(component);

		Assert.assertTrue(startValueChanged.get());
	}

	private static class WrappedBoolean {
		private boolean value;

		public WrappedBoolean(final boolean value) {
			this.value = value;
		}

		public boolean get() {
			return this.value;
		}

		public void set(final boolean value) {
			this.value = value;
		}

	}

}
