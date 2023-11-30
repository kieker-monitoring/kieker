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
package kieker.tools.sar.stages.dataflow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kieker.analysis.code.CodeUtils;
import kieker.analysis.code.data.GlobalDataEntry;
import kieker.tools.sar.Storage;
import kieker.tools.sar.signature.processor.AbstractSignatureProcessor;

import teetime.framework.OutputPort;
import teetime.stage.basic.AbstractTransformation;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CleanupStorageComponentSignatureStage extends AbstractTransformation<GlobalDataEntry, Storage> {

	private final OutputPort<String> errorMessageOutputPort = this.createOutputPort(String.class);

	private final List<AbstractSignatureProcessor> processors;

	public CleanupStorageComponentSignatureStage(final List<AbstractSignatureProcessor> processors) {
		this.processors = processors;
	}

	@Override
	protected void execute(final GlobalDataEntry event) throws Exception {
		this.outputPort.send(
				this.executeEntry(this.makeList(event.getFiles()), this.makeList(event.getModules()), event.getName()));
	}

	private List<String> makeList(final String input) {
		return Arrays.asList(input.split(","));
	}

	private Storage executeEntry(final List<String> paths, final List<String> componentSignatures,
			final String storageSignature) {
		final Entry entry = new Entry();
		final Storage storage = new Storage(storageSignature);
		final Set<String> modules = new HashSet<>();

		for (int i = 0; i < paths.size(); i++) {
			entry.component = CodeUtils.UNKNOWN_COMPONENT;
			entry.element = CodeUtils.UNKNOWN_OPERATION;

			for (final AbstractSignatureProcessor processor : this.processors) {
				if (!processor.processSignatures(paths.get(i), componentSignatures.get(i), storageSignature)) {
					this.errorMessageOutputPort.send(processor.getErrorMessage());
				}

				if (CodeUtils.UNKNOWN_COMPONENT.equals(entry.component)) {
					entry.component = processor.getComponentSignature();
				}
				if (CodeUtils.UNKNOWN_OPERATION.equals(entry.element)) {
					entry.element = processor.getElementSignature();
				}
			}
			modules.add(entry.component);
		}
		storage.getFiles().addAll(paths);
		storage.getModules().addAll(modules);

		return storage;
	}

	public OutputPort<String> getErrorMessageOutputPort() {
		return this.errorMessageOutputPort;
	}

	private class Entry {
		private String component;
		private String element;
	}

}
