/**
 * Copyright (C) 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime-framework.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kieker.analysisteetime.plugin.reader.filesystem;

import java.io.File;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.framework.AbstractStage;
import teetime.stage.FileExtensionSwitch;
import teetime.stage.basic.merger.Merger;
import teetime.stage.io.Directory2FilesFilter;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryCreationFilter;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysisteetime.plugin.reader.filesystem.format.binary.file.BinaryFile2RecordFilter;
import kieker.analysisteetime.plugin.reader.filesystem.format.text.file.DatFile2RecordFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.common.util.filesystem.FSUtil;

/**
 * @author Christian Wulf
 *
 * @since 1.0
 */
public final class Dir2RecordsFilter extends CompositeStage {

	private final ClassNameRegistryCreationFilter classNameRegistryCreationFilter;
	private final Merger<IMonitoringRecord> recordMerger;

	private ClassNameRegistryRepository classNameRegistryRepository;

	/**
	 * Default constructor using a new instance of {@link ClassNameRegistryRepository}
	 */
	public Dir2RecordsFilter() {
		this(new ClassNameRegistryRepository());
	}

	public Dir2RecordsFilter(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;

		// FIXME does not yet work with more than one thread due to classNameRegistryRepository: classNameRegistryRepository is set after the ctor
		// create stages
		final ClassNameRegistryCreationFilter classNameRegistryCreationFilter = new ClassNameRegistryCreationFilter(this.classNameRegistryRepository);
		final Directory2FilesFilter directory2FilesFilter = new Directory2FilesFilter();

		final FileExtensionSwitch fileExtensionSwitch = new FileExtensionSwitch();

		final DatFile2RecordFilter datFile2RecordFilter = new DatFile2RecordFilter(this.classNameRegistryRepository);
		final BinaryFile2RecordFilter binaryFile2RecordFilter = new BinaryFile2RecordFilter(this.classNameRegistryRepository);

		recordMerger = new Merger<IMonitoringRecord>();

		// store ports due to readability reasons
		final OutputPort<File> normalFileOutputPort = fileExtensionSwitch.addFileExtension(FSUtil.NORMAL_FILE_EXTENSION);
		final OutputPort<File> binFileOutputPort = fileExtensionSwitch.addFileExtension(BinaryCompressionMethod.NONE.getFileExtension());

		// connect ports by pipes
		connectPorts(classNameRegistryCreationFilter.getOutputPort(), directory2FilesFilter.getInputPort());
		connectPorts(directory2FilesFilter.getOutputPort(), fileExtensionSwitch.getInputPort());

		connectPorts(normalFileOutputPort, datFile2RecordFilter.getInputPort());
		connectPorts(binFileOutputPort, binaryFile2RecordFilter.getInputPort());

		connectPorts(datFile2RecordFilter.getOutputPort(), recordMerger.getNewInputPort());
		connectPorts(binaryFile2RecordFilter.getOutputPort(), recordMerger.getNewInputPort());

		// prepare pipeline
		this.classNameRegistryCreationFilter = classNameRegistryCreationFilter;
	}

	public AbstractStage getFirstStage() {
		return classNameRegistryCreationFilter;
	}

	public InputPort<File> getInputPort() {
		return classNameRegistryCreationFilter.getInputPort();
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return recordMerger.getOutputPort();
	}

	public ClassNameRegistryRepository getClassNameRegistryRepository() {
		return this.classNameRegistryRepository;
	}

	public void setClassNameRegistryRepository(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

}
