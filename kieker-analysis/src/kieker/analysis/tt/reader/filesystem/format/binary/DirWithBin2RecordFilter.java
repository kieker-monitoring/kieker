/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.tt.reader.filesystem.format.binary;

import java.io.File;

import kieker.analysis.tt.reader.filesystem.className.ClassNameRegistryCreationFilter;
import kieker.analysis.tt.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysis.tt.reader.filesystem.format.binary.file.BinaryFile2RecordFilter;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractStage;
import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * @author Christian Wulf
 *
 * @since 1.14
 *
 * @deprecated since 1.15 removed 1.16
 */
@Deprecated
public class DirWithBin2RecordFilter extends CompositeStage {

	private final ClassNameRegistryCreationFilter classNameRegistryCreationFilter;
	private final BinaryFile2RecordFilter binaryFile2RecordFilter;

	private ClassNameRegistryRepository classNameRegistryRepository;

	public DirWithBin2RecordFilter() {
		this(null);
	}

	public DirWithBin2RecordFilter(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;

		this.classNameRegistryCreationFilter = new ClassNameRegistryCreationFilter(classNameRegistryRepository);
		this.binaryFile2RecordFilter = new BinaryFile2RecordFilter(classNameRegistryRepository);
	}

	public ClassNameRegistryRepository getClassNameRegistryRepository() {
		return this.classNameRegistryRepository;
	}

	public void setClassNameRegistryRepository(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	protected AbstractStage getFirstStage() {
		return this.classNameRegistryCreationFilter;
	}

	public InputPort<File> getInputPort() {
		return this.classNameRegistryCreationFilter.getInputPort();
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.binaryFile2RecordFilter.getOutputPort();
	}

}
