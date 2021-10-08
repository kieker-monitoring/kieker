/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.sink.display;

import kieker.analysis.display.TagCloud;
import kieker.analysis.display.annotation.Display;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.ClassOperationSignaturePair;

import teetime.framework.AbstractConsumerStage;

/**
 * This is a filter which accepts {@link OperationExecutionRecord} instances and provides different views to visualize them.
 *
 * @author Nils Christian Ehmke, Lars Bluemke, Sören Henning
 *
 * @since 1.8
 */
public class MethodAndComponentFlowDisplaySink extends AbstractConsumerStage<OperationExecutionRecord> {

	/** The name of the input port accepting incoming events. */
	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	private final TagCloud methodTagCloud = new TagCloud();
	private final TagCloud componentTagCloud = new TagCloud();

	/**
	 * Creates a new instance of this class.
	 */
	public MethodAndComponentFlowDisplaySink() {
		// empty default constructor
	}

	/**
	 * This method represents the input port receiving the incoming events.
	 *
	 * @param record
	 *            The record to display and relay.
	 */
	@Override
	protected void execute(final OperationExecutionRecord record) {
		final String shortClassName = ClassOperationSignaturePair.splitOperationSignatureStr(record.getOperationSignature()).getSimpleClassname();
		final String methodName = shortClassName + '.' + this.extractMethodName(record.getOperationSignature());

		this.methodTagCloud.incrementCounter(methodName);
		this.componentTagCloud.incrementCounter(shortClassName);
	}

	private String extractMethodName(final String operationSignature) {
		final String operationSignatureWithoutParameters = operationSignature.replaceAll("\\(.*\\)", "");
		final int lastPointPos = operationSignatureWithoutParameters.lastIndexOf('.');
		return operationSignatureWithoutParameters.substring(lastPointPos + 1);
	}

	/**
	 * This method represents a display for a method tag cloud.
	 *
	 * @return The display object for the method tag cloud.
	 */
	@Display(name = "Method Tag Cloud Display")
	public TagCloud methodTagCloudDisplay() {
		return this.methodTagCloud;
	}

	/**
	 * This method represents a display for a component tag cloud.
	 *
	 * @return The display object for the component tag cloud.
	 */
	@Display(name = "Component Tag Cloud Display")
	public TagCloud componentTagCloudDisplay() {
		return this.componentTagCloud;
	}

}
