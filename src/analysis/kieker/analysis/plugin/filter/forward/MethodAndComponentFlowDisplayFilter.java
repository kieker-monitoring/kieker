/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.plugin.filter.forward;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.TagCloud;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.ClassOperationSignaturePair;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
@Plugin(outputPorts =
		@OutputPort(
				name = MethodAndComponentFlowDisplayFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				eventTypes = { OperationExecutionRecord.class },
				description = "Provides each incoming object"))
public class MethodAndComponentFlowDisplayFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";

	private final TagCloud methodTagCloud = new TagCloud();
	private final TagCloud componentTagCloud = new TagCloud();

	public MethodAndComponentFlowDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = MethodAndComponentFlowDisplayFilter.INPUT_PORT_NAME_EVENTS, eventTypes = { OperationExecutionRecord.class })
	public void input(final OperationExecutionRecord record) {
		final String shortClassName = ClassOperationSignaturePair.splitOperationSignatureStr(record.getOperationSignature()).getSimpleClassname();
		final String methodName = shortClassName + '.' + this.extractMethodName(record.getOperationSignature());

		this.methodTagCloud.incrementCounter(methodName);
		this.componentTagCloud.incrementCounter(shortClassName);

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, record);
	}

	private String extractMethodName(final String operationSignature) {
		final String operationSignatureWithoutParameters = operationSignature.replaceAll("\\(.*\\)", "");
		final int lastPointPos = operationSignatureWithoutParameters.lastIndexOf('.');
		final String methodName = operationSignatureWithoutParameters.substring(lastPointPos + 1);

		return methodName;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@Display(name = "Method Tag Cloud Display")
	public TagCloud methodTagCloudDisplay() {
		return this.methodTagCloud;
	}

	@Display(name = "Component Tag Cloud Display")
	public TagCloud componentTagCloudDisplay() {
		return this.componentTagCloud;
	}
}
