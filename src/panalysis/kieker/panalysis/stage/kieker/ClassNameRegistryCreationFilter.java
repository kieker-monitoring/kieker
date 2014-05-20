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
package kieker.panalysis.stage.kieker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import kieker.analysis.ClassNameRegistry;
import kieker.analysis.ClassNameRegistryRepository;
import kieker.analysis.MappingFileParser;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ClassNameRegistryCreationFilter extends AbstractFilter<ClassNameRegistryCreationFilter> {

	public final IInputPort<ClassNameRegistryCreationFilter, File> directoryInputPort = this.createInputPort();

	public final IOutputPort<ClassNameRegistryCreationFilter, File> relayDirectoryOutputPort = this.createOutputPort();
	public final IOutputPort<ClassNameRegistryCreationFilter, String> filePrefixOutputPort = this.createOutputPort();

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
	protected boolean execute(final Context<ClassNameRegistryCreationFilter> context) {
		final File inputDir = context.tryTake(this.directoryInputPort);
		if (inputDir == null) {
			return false;
		}

		final File mappingFile = this.mappingFileParser.findMappingFile(inputDir);
		if (mappingFile == null) {
			return true;
		}

		try {
			final ClassNameRegistry classNameRegistry = this.mappingFileParser.parseFromStream(new FileInputStream(mappingFile));
			this.classNameRegistryRepository.put(inputDir, classNameRegistry);
			context.put(this.relayDirectoryOutputPort, inputDir);

			final String filePrefix = this.mappingFileParser.getFilePrefixFromMappingFile(mappingFile);
			context.put(this.filePrefixOutputPort, filePrefix);
		} catch (final FileNotFoundException e) {
			this.logger.error("Mapping file not found.", e); // and skip this directory
		}

		return true;
	}

	public ClassNameRegistryRepository getClassNameRegistryRepository() {
		return this.classNameRegistryRepository;
	}

	public void setClassNameRegistryRepository(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

}
