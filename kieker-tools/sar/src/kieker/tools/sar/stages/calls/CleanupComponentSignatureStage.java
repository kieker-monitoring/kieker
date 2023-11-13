/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.sar.stages.calls;

import java.util.List;

import kieker.analysis.code.CodeUtils;
import kieker.analysis.code.data.CallerCalleeEntry;
import kieker.tools.sar.signature.processor.AbstractSignatureProcessor;

import teetime.framework.OutputPort;
import teetime.stage.basic.AbstractFilter;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CleanupComponentSignatureStage extends AbstractFilter<CallerCalleeEntry> {

	private final OutputPort<String> errorMessageOutputPort = this.createOutputPort(String.class);

	private final List<AbstractSignatureProcessor> processors;

	public CleanupComponentSignatureStage(final List<AbstractSignatureProcessor> processors) {
		this.processors = processors;
	}

	@Override
	protected void execute(final CallerCalleeEntry event) throws Exception {
		final FullyQualifiedOperation caller = this.executeOperation(event.getSourcePath(), event.getSourceModule(),
				event.getCaller());
		final FullyQualifiedOperation callee = this.executeOperation(event.getTargetPath(), event.getTargetModule(),
				event.getCallee());
		final CallerCalleeEntry newEvent = new CallerCalleeEntry(event.getSourcePath(), caller.component,
				caller.operation, event.getTargetPath(), callee.component, callee.operation);

		this.outputPort.send(newEvent);
	}

	private FullyQualifiedOperation executeOperation(final String path, final String componentSignature,
			final String operationSignature) {
		final FullyQualifiedOperation entry = new FullyQualifiedOperation();
		entry.component = CodeUtils.UNKNOWN_COMPONENT;
		entry.operation = CodeUtils.UNKNOWN_OPERATION;

		for (final AbstractSignatureProcessor processor : this.processors) {
			if (!processor.processSignatures(path, componentSignature, operationSignature)) {
				this.errorMessageOutputPort.send(processor.getErrorMessage());
			}
			if (CodeUtils.UNKNOWN_COMPONENT.equals(entry.component)) {
				entry.component = processor.getComponentSignature();
			}
			if (CodeUtils.UNKNOWN_OPERATION.equals(entry.operation)) {
				entry.operation = processor.getElementSignature();
			}
		}
		return entry;
	}

	public OutputPort<String> getErrorMessageOutputPort() {
		return this.errorMessageOutputPort;
	}

	private class FullyQualifiedOperation {
		private String component;
		private String operation;
	}

}
