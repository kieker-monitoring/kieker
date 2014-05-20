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
import java.util.regex.Pattern;

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

	public final IOutputPort<FileExtensionFilter, File> unknownFileExtensionOutputPort = this.createOutputPort();

	private final HashMap<String, IOutputPort<FileExtensionFilter, File>> fileExtensionMap = new HashMap<String, IOutputPort<FileExtensionFilter, File>>();

	private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

	@Override
	protected boolean execute(final Context<FileExtensionFilter> context) {
		final File file = context.tryTake(this.fileInputPort);
		if (file == null) {
			return false;
		}

		final String[] filenameParts = DOT_PATTERN.split(file.getName());
		final String fileExtension = filenameParts[filenameParts.length - 1];
		final IOutputPort<FileExtensionFilter, File> outputPort = this.fileExtensionMap.get(fileExtension);
		if (outputPort != null) {
			context.put(outputPort, file);
		} else {
			context.put(this.unknownFileExtensionOutputPort, file);
		}

		return true;
	}

	/**
	 * 
	 * @param fileExtension
	 *            the file extension (<i>a leading dot is removed automatically)</i>
	 * @return
	 */
	public IOutputPort<FileExtensionFilter, File> createOutputPortForFileExtension(String fileExtension) {
		if (fileExtension.startsWith(".")) {
			fileExtension = fileExtension.substring(1);
		}
		IOutputPort<FileExtensionFilter, File> outputPort = this.fileExtensionMap.get(fileExtension);
		if (outputPort == null) {
			outputPort = super.createOutputPort();
			this.fileExtensionMap.put(fileExtension, outputPort);
		}
		return outputPort;
	}
}
