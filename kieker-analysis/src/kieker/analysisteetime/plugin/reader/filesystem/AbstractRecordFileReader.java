/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysisteetime.plugin.reader.filesystem.className.MappingFileParser;
import kieker.common.registry.reader.ReaderRegistry;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Christian Wulf
 *
 * @since 1.14
 *
 * TODO not used anywhere
 */
public abstract class AbstractRecordFileReader extends AbstractConsumerStage<File> {

	private final ClassNameRegistryRepository classNameRegistryRepository;
	private final MappingFileParser mappingFileParser;

	public AbstractRecordFileReader(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
		this.mappingFileParser = new MappingFileParser(this.logger);
	}

	@Override
	protected void execute(final File recordFile) {
		final ReaderRegistry<String> classNameRegistry = this.getClassNameRegistry(recordFile);
		this.reconstructRecords(classNameRegistry);
	}

	private ReaderRegistry<String> getClassNameRegistry(final File recordFile) {
		final File mapFile = this.mappingFileParser.findMappingFile(recordFile.getParentFile());

		ReaderRegistry<String> classNameRegistry = this.classNameRegistryRepository.get(mapFile);
		if (null == classNameRegistry) {
			try (final FileInputStream inputStream = new FileInputStream(mapFile)) {
				classNameRegistry = this.mappingFileParser.parseFromStream(inputStream);
				this.classNameRegistryRepository.put(mapFile, classNameRegistry);
			} catch (final IOException e) {
				this.logger.error("", e);
			}
		}

		return classNameRegistry;
	}

	protected abstract void reconstructRecords(ReaderRegistry<String> classNameRegistry);

}
