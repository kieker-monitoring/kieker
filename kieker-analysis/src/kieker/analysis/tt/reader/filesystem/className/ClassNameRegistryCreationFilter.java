/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.tt.reader.filesystem.className;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import kieker.common.registry.reader.ReaderRegistry;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 *
 * @deprecated since 1.15 remove 1.16
 */
@Deprecated
public class ClassNameRegistryCreationFilter extends AbstractConsumerStage<File> {

	private final OutputPort<File> outputPort = this.createOutputPort();

	private ClassNameRegistryRepository classNameRegistryRepository;

	private final MappingFileParser mappingFileParser;

	/**
	 * @since 1.10
	 */
	public ClassNameRegistryCreationFilter(final ClassNameRegistryRepository classNameRegistryRepository) {
		this();
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	/**
	 * @since 1.10
	 */
	public ClassNameRegistryCreationFilter() {
		super();
		this.mappingFileParser = new MappingFileParser(this.logger);
	}

	@Override
	// @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION") // Stream is closed by parseFromStream(..) method
	protected void execute(final File inputDir) {
		final File mappingFile = this.mappingFileParser.findMappingFile(inputDir);
		if (mappingFile == null) {
			this.logger.error("Directory '" + inputDir + "' does not exist or an I/O error occured.");
			return;
		}

		try {
			final ReaderRegistry<String> classNameRegistry = this.mappingFileParser.parseFromStream(new FileInputStream(mappingFile));
			this.classNameRegistryRepository.put(inputDir, classNameRegistry);
			this.outputPort.send(inputDir);

			// final String filePrefix = this.mappingFileParser.getFilePrefixFromMappingFile(mappingFile);
			// context.put(this.filePrefixOutputPort, filePrefix);
		} catch (final FileNotFoundException e) {
			this.logger.error("Mapping file not found.", e); // and skip this directory
		}
	}

	public ClassNameRegistryRepository getClassNameRegistryRepository() {
		return this.classNameRegistryRepository;
	}

	public void setClassNameRegistryRepository(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	public OutputPort<File> getOutputPort() {
		return this.outputPort;
	}

}
