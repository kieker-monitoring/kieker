/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.stage;

import java.io.File;
import java.util.HashMap;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class FileExtensionFilter extends AbstractFilter<FileExtensionFilter> {

	public final IInputPort<FileExtensionFilter, File> fileInputPort = this.createInputPort();

	public final IOutputPort<FileExtensionFilter, File> unknownFileExtensionPort = this.createOutputPort();

	private final HashMap<String, IOutputPort<FileExtensionFilter, File>> fileExtensionMap = new HashMap<String, IOutputPort<FileExtensionFilter, File>>();

	@Override
	protected boolean execute(final Context<FileExtensionFilter> context) {
		final File file = context.tryTake(this.fileInputPort);
		if (file == null) {
			return false;
		}

		final String[] filenameParts = file.getName().split("\\.");
		final String fileExtension = filenameParts[filenameParts.length - 1];
		final IOutputPort<FileExtensionFilter, File> outputPort = this.fileExtensionMap.get(fileExtension);
		if (outputPort != null) {
			context.put(outputPort, file);
		} else {
			context.put(this.unknownFileExtensionPort, file);
		}

		return true;
	}

	/**
	 * 
	 * @param fileExtension
	 *            the file extension with a leading dot
	 * @return
	 */
	public IOutputPort<FileExtensionFilter, File> createOutputPortForFileExtension(final String fileExtension) {
		if (!fileExtension.startsWith(".")) {
			throw new IllegalArgumentException("Please pass the file extension with a leading dot.");
		}
		IOutputPort<FileExtensionFilter, File> outputPort = this.fileExtensionMap.get(fileExtension);
		if (outputPort == null) {
			outputPort = super.createOutputPort();
			this.fileExtensionMap.put(fileExtension, outputPort);
		}
		return outputPort;
	}

	// public IOutputPort<FileExtensionFilter, File> getOutputPortForFileExtension(final String fileExtension) {
	// if (fileExtension.startsWith(".")) {
	// throw new IllegalArgumentException("");
	// }
	// return this.fileExtensionMap.get(fileExtension);
	// }
}
