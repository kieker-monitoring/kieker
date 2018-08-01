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
import java.io.IOException;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryCreationFilter;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysisteetime.plugin.reader.filesystem.format.binary.file.BinaryFile2RecordFilter;
import kieker.analysisteetime.plugin.reader.filesystem.format.text.file.DatFileToRecordStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.common.util.filesystem.FSUtil;

import teetime.framework.AbstractStage;
import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.FileExtensionSwitch;
import teetime.stage.basic.merger.Merger;
import teetime.stage.io.Directory2FilesFilter;

/** Note: This is a temporary measure, the real filter is available in teetime/kieker. */
/**
 * @author Christian Wulf
 * @author Reiner Jung -- minor fixes to support empty values in text files and compression
 *
 * @since 1.14
 */
public final class Dir2RecordsFilter extends CompositeStage {

	private static final Logger LOGGER = LoggerFactory.getLogger(Dir2RecordsFilter.class);

	private final ClassNameRegistryCreationFilter classNameRegistryCreationFilter;
	private final Merger<IMonitoringRecord> recordMerger;

	private ClassNameRegistryRepository classNameRegistryRepository;

	/**
	 * Default constructor using a new instance of {@link ClassNameRegistryRepository}.
	 */
	public Dir2RecordsFilter() {
		this(new ClassNameRegistryRepository());
	}

	/**
	 * Constructor for an external class name registry.
	 *
	 * @param classNameRegistryRepository
	 *			a class name registry
	 */
	public Dir2RecordsFilter(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;

		// FIXME does not yet work with more than one thread due to classNameRegistryRepository: classNameRegistryRepository is set after the ctor
		// create stages
		final ClassNameRegistryCreationFilter localClassNameRegistryCreationFilter = new ClassNameRegistryCreationFilter(this.classNameRegistryRepository);
		final Directory2FilesFilter directory2FilesFilter = new Directory2FilesFilter(new Comparator<File>() {

			@Override
			public int compare(final File o1, final File o2) {
				try {
					return o1.getCanonicalFile().compareTo(o2.getCanonicalFile());
				} catch (final IOException e) {
					Dir2RecordsFilter.LOGGER.error("Exception while getting canonical file name", e); // NOPMD
					return 0;
				}
			}

		});

		final FileExtensionSwitch fileExtensionSwitch = new FileExtensionSwitch();

		final DatFileToRecordStage datFile2RecordFilter = new DatFileToRecordStage(this.classNameRegistryRepository,
				"UTF-8");
		final BinaryFile2RecordFilter binaryFile2RecordFilter = new BinaryFile2RecordFilter(this.classNameRegistryRepository);
		// TODO this is a hack, as the fileExtensionSwitch does not work properly with composed extensions
		final BinaryFile2RecordFilter decompBinaryStream2RecordFilter = new BinaryFile2RecordFilter(this.classNameRegistryRepository);

		this.recordMerger = new Merger<>();

		// store ports due to readability reasons
		final OutputPort<File> datFileOutputPort = fileExtensionSwitch.addFileExtension(FSUtil.DAT_FILE_EXTENSION);
		final OutputPort<File> binFileOutputPort = fileExtensionSwitch.addFileExtension(BinaryCompressionMethod.NONE.getFileExtension());
		final OutputPort<File> xzFileOutputPort = fileExtensionSwitch.addFileExtension(".xz");

		// connect ports by pipes
		this.connectPorts(localClassNameRegistryCreationFilter.getOutputPort(), directory2FilesFilter.getInputPort());
		this.connectPorts(directory2FilesFilter.getOutputPort(), fileExtensionSwitch.getInputPort());

		this.connectPorts(datFileOutputPort, datFile2RecordFilter.getInputPort());
		this.connectPorts(binFileOutputPort, binaryFile2RecordFilter.getInputPort());

		this.connectPorts(xzFileOutputPort, decompBinaryStream2RecordFilter.getInputPort());

		this.connectPorts(datFile2RecordFilter.getOutputPort(), this.recordMerger.getNewInputPort());
		this.connectPorts(binaryFile2RecordFilter.getOutputPort(), this.recordMerger.getNewInputPort());
		this.connectPorts(decompBinaryStream2RecordFilter.getOutputPort(), this.recordMerger.getNewInputPort());

		// prepare pipeline
		this.classNameRegistryCreationFilter = localClassNameRegistryCreationFilter;
	}

	public AbstractStage getFirstStage() {
		return this.classNameRegistryCreationFilter;
	}

	public InputPort<File> getInputPort() {
		return this.classNameRegistryCreationFilter.getInputPort();
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.recordMerger.getOutputPort();
	}

	public ClassNameRegistryRepository getClassNameRegistryRepository() {
		return this.classNameRegistryRepository;
	}

	public void setClassNameRegistryRepository(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

}
