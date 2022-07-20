/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.recovery;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.architecture.recovery.events.CallEvent;
import kieker.analysis.architecture.recovery.events.OperationCallDurationEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.type.TypeModel;

import teetime.framework.test.StageTester;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CallEvent2OperationCallStageTest {

	private TypeModel typeModel;
	private AssemblyModel assemblyModel;
	private DeploymentModel deploymentModel;
	private CallEvent2OperationCallStage stage;

	@Before
	public void setUp() {
		this.typeModel = RecoveryModelUtils.createTypeModel();
		this.assemblyModel = RecoveryModelUtils.createAssemblyModel(this.typeModel);
		this.deploymentModel = RecoveryModelUtils.createDeploymentModel(this.assemblyModel);
		this.stage = new CallEvent2OperationCallStage(this.deploymentModel);
	}

	@Test
	public void testResolvableOperationSignatures() {
		final CallEvent callEvent = this.createCallEvent(RecoveryModelUtils.OPERATION_SIGNATURE, RecoveryModelUtils.OPERATION_SIGNATURE);

		final List<OperationCallDurationEvent> outputs = new ArrayList<>();

		StageTester.test(this.stage).send(callEvent).to(this.stage.getInputPort()).receive(outputs).from(this.stage.getOutputPort()).start();

		Assert.assertEquals("Number of result events expected.", 1, outputs.size());
		final OperationCallDurationEvent output = outputs.get(0);
		Assert.assertEquals("Wrong duration", Duration.ZERO, output.getDuration());

		final DeployedOperation callerOperation = output.getOperationCall().getSecond();
		Assert.assertNotNull("Missing caller operation", callerOperation);
		Assert.assertEquals("Wrong caller", RecoveryModelUtils.OPERATION_SIGNATURE,
				callerOperation.getAssemblyOperation().getOperationType().getSignature());
		final DeployedOperation calleeOperation = output.getOperationCall().getSecond();
		Assert.assertNotNull("Missing callee operation", calleeOperation);
		Assert.assertEquals("Wrong callee", RecoveryModelUtils.OPERATION_SIGNATURE,
				calleeOperation.getAssemblyOperation().getOperationType().getSignature());
	}

	@Test
	public void testUnresolvableCallee() {
		final CallEvent callEvent = this.createCallEvent(RecoveryModelUtils.OPERATION_SIGNATURE, RecoveryModelUtils.MISSING_OPERATION_SIGNATURE);

		final List<OperationCallDurationEvent> outputs = new ArrayList<>();

		StageTester.test(this.stage).send(callEvent).to(this.stage.getInputPort()).receive(outputs).from(this.stage.getOutputPort()).start();

		Assert.assertEquals("Number of result events expected.", 1, outputs.size());
		final OperationCallDurationEvent output = outputs.get(0);
		Assert.assertEquals("Wrong duration", Duration.ZERO, output.getDuration());

		final DeployedOperation callerOperation = output.getOperationCall().getFirst();
		Assert.assertNotNull("Missing caller operation", callerOperation);
		Assert.assertEquals("Wrong caller", RecoveryModelUtils.OPERATION_SIGNATURE,
				callerOperation.getAssemblyOperation().getOperationType().getSignature());
		final DeployedOperation calleeOperation = output.getOperationCall().getSecond();
		Assert.assertNull("Found callee operation, but should be empty", calleeOperation);
	}

	@Test
	public void testUnresolvableComponent() {
		final CallEvent callEvent = this.createCallEvent(RecoveryModelUtils.HOST_NAME, RecoveryModelUtils.MISSING_COMPONENT_SIGNATURE,
				RecoveryModelUtils.OPERATION_SIGNATURE, RecoveryModelUtils.MISSING_OPERATION_SIGNATURE);

		final List<OperationCallDurationEvent> outputs = new ArrayList<>();

		StageTester.test(this.stage).send(callEvent).to(this.stage.getInputPort()).receive(outputs).from(this.stage.getOutputPort()).start();

		Assert.assertEquals("Number of result events expected.", 1, outputs.size());
		final OperationCallDurationEvent output = outputs.get(0);
		Assert.assertEquals("Wrong duration", Duration.ZERO, output.getDuration());

		final DeployedOperation callerOperation = output.getOperationCall().getFirst();
		Assert.assertNull("Found caller operation, but should be empty", callerOperation);

		final DeployedOperation calleeOperation = output.getOperationCall().getSecond();
		Assert.assertNull("Found callee operation, but should be empty", calleeOperation);
	}

	@Test
	public void testUnresolvableContext() {
		final CallEvent callEvent = this.createCallEvent(RecoveryModelUtils.MISSING_HOST_NAME, RecoveryModelUtils.COMPONENT_SIGNATURE,
				RecoveryModelUtils.OPERATION_SIGNATURE, RecoveryModelUtils.MISSING_OPERATION_SIGNATURE);

		final List<OperationCallDurationEvent> outputs = new ArrayList<>();

		StageTester.test(this.stage).send(callEvent).to(this.stage.getInputPort()).receive(outputs).from(this.stage.getOutputPort()).start();

		Assert.assertEquals("Number of result events expected.", 1, outputs.size());
		final OperationCallDurationEvent output = outputs.get(0);
		Assert.assertEquals("Wrong duration", Duration.ZERO, output.getDuration());

		final DeployedOperation callerOperation = output.getOperationCall().getFirst();
		Assert.assertNull("Found caller operation, but should be empty", callerOperation);

		final DeployedOperation calleeOperation = output.getOperationCall().getSecond();
		Assert.assertNull("Found callee operation, but should be empty", calleeOperation);
	}

	private CallEvent createCallEvent(final String callerSignature, final String calleeSignature) {
		return this.createCallEvent(RecoveryModelUtils.HOST_NAME, RecoveryModelUtils.COMPONENT_SIGNATURE, callerSignature, calleeSignature);
	}

	private CallEvent createCallEvent(final String hostname, final String componentSignature, final String callerSignature, final String calleeSignature) {
		final OperationEvent caller = new OperationEvent(hostname, componentSignature, callerSignature);
		final OperationEvent callee = new OperationEvent(hostname, componentSignature, calleeSignature);
		final Duration duration = Duration.ZERO;
		return new CallEvent(caller, callee, duration);
	}

}
