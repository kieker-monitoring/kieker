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
import java.io.FileFilter;
import java.util.Comparator;
import java.util.Map;

import kieker.analysis.ClassNameRegistry;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.common.util.filesystem.FSUtil;
import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipe;
import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipeFactory;
import kieker.panalysis.framework.core.CompositeFilter;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.predicate.IsDirectoryPredicate;
import kieker.panalysis.stage.Directory2FilesFilter;
import kieker.panalysis.stage.basic.PredicateFilter;
import kieker.panalysis.stage.basic.merger.Merger;
import kieker.panalysis.stage.io.FileExtensionFilter;
import kieker.panalysis.stage.kieker.fileToRecord.BinaryFile2RecordFilter;
import kieker.panalysis.stage.kieker.fileToRecord.DatFile2RecordFilter;
import kieker.panalysis.stage.kieker.fileToRecord.ZipFile2RecordFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class File2RecordFilter extends CompositeFilter {

	public final IInputPort<PredicateFilter<File>, File> fileInputPort;

	public final IOutputPort<Merger<IMonitoringRecord>, IMonitoringRecord> recordOutputPort;

	private final FileFilter fileFilter = new FileFilter() {
		public boolean accept(final File pathname) {
			final String name = pathname.getName();
			return pathname.isFile()
					&& name.startsWith(FSUtil.FILE_PREFIX)
					&& (name.endsWith(FSUtil.NORMAL_FILE_EXTENSION) || BinaryCompressionMethod.hasValidFileExtension(name));
		}
	};

	private final Comparator<File> fileComparator = new Comparator<File>() {
		public final int compare(final File f1, final File f2) {
			return f1.compareTo(f2); // simplified (we expect no dirs!)
		}
	};

	// FIXME filters must have a default ctor
	// FIXME classNameRegistryRepository must be a property to be able to be cloned
	public File2RecordFilter(final Map<String, ClassNameRegistry> classNameRegistryRepository) {
		// create stages
		final PredicateFilter<File> isDirectoryFilter = new PredicateFilter<File>(new IsDirectoryPredicate());
		final ClassNameRegistryCreationFilter classNameRegistryCreationFilter = new ClassNameRegistryCreationFilter(classNameRegistryRepository);
		final Directory2FilesFilter directory2FilesFilter = new Directory2FilesFilter(this.fileFilter, this.fileComparator);
		final Merger<File> fileMerger = new Merger<File>();
		final FileExtensionFilter fileExtensionFilter = new FileExtensionFilter();

		final DatFile2RecordFilter datFile2RecordFilter = new DatFile2RecordFilter(classNameRegistryRepository);
		final BinaryFile2RecordFilter binaryFile2RecordFilter = new BinaryFile2RecordFilter();
		final ZipFile2RecordFilter zipFile2RecordFilter = new ZipFile2RecordFilter();

		final Merger<IMonitoringRecord> recordMerger = new Merger<IMonitoringRecord>();

		// store ports due to readability reasons
		final IOutputPort<FileExtensionFilter, File> normalFileInputPort = fileExtensionFilter.createOutputPortForFileExtension(FSUtil.NORMAL_FILE_EXTENSION);
		final IOutputPort<FileExtensionFilter, File> binFileInputPort = fileExtensionFilter.createOutputPortForFileExtension(BinaryCompressionMethod.NONE
				.getFileExtension());
		final IOutputPort<FileExtensionFilter, File> zipFileInputPort = fileExtensionFilter.createOutputPortForFileExtension(FSUtil.ZIP_FILE_EXTENSION);

		// connect ports by pipes
		MethodCallPipe.connect(isDirectoryFilter.outputMatchingPort, classNameRegistryCreationFilter.directoryInputPort);
		MethodCallPipe.connect(isDirectoryFilter.outputMismatchingPort, fileMerger.getNewInputPort()); // BETTER restructure pipeline
		MethodCallPipe.connect(classNameRegistryCreationFilter.relayDirectoryOutputPort, directory2FilesFilter.directoryInputPort);
		MethodCallPipe.connect(directory2FilesFilter.fileOutputPort, fileMerger.getNewInputPort());
		MethodCallPipe.connect(fileMerger.outputPort, fileExtensionFilter.fileInputPort);

		final ConcurrentWorkStealingPipeFactory<File> concurrentWorkStealingPipeFactory0 = new ConcurrentWorkStealingPipeFactory<File>();
		final ConcurrentWorkStealingPipe<File> concurrentWorkStealingPipe0 = concurrentWorkStealingPipeFactory0.create();
		concurrentWorkStealingPipe0.setSourcePort(normalFileInputPort);
		concurrentWorkStealingPipe0.setTargetPort(datFile2RecordFilter.fileInputPort);

		final ConcurrentWorkStealingPipeFactory<File> concurrentWorkStealingPipeFactory1 = new ConcurrentWorkStealingPipeFactory<File>();
		final ConcurrentWorkStealingPipe<File> concurrentWorkStealingPipe1 = concurrentWorkStealingPipeFactory1.create();
		concurrentWorkStealingPipe1.setSourcePort(binFileInputPort);
		concurrentWorkStealingPipe1.setTargetPort(binaryFile2RecordFilter.fileInputPort);

		final ConcurrentWorkStealingPipeFactory<File> concurrentWorkStealingPipeFactory2 = new ConcurrentWorkStealingPipeFactory<File>();
		final ConcurrentWorkStealingPipe<File> concurrentWorkStealingPipe2 = concurrentWorkStealingPipeFactory2.create();
		concurrentWorkStealingPipe2.setSourcePort(zipFileInputPort);
		concurrentWorkStealingPipe2.setTargetPort(zipFile2RecordFilter.fileInputPort);

		final ConcurrentWorkStealingPipeFactory<IMonitoringRecord> concurrentWorkStealingPipeFactoriesNormal = new ConcurrentWorkStealingPipeFactory<IMonitoringRecord>();
		final ConcurrentWorkStealingPipe<IMonitoringRecord> datPipe = concurrentWorkStealingPipeFactoriesNormal.create();
		datPipe.connect(datFile2RecordFilter.recordOutputPort, recordMerger.getNewInputPort());

		final ConcurrentWorkStealingPipeFactory<IMonitoringRecord> concurrentWorkStealingPipeFactoriesBinary = new ConcurrentWorkStealingPipeFactory<IMonitoringRecord>();
		final ConcurrentWorkStealingPipe<IMonitoringRecord> binaryPipe = concurrentWorkStealingPipeFactoriesBinary.create();
		binaryPipe.connect(binaryFile2RecordFilter.recordOutputPort, recordMerger.getNewInputPort());

		final ConcurrentWorkStealingPipeFactory<IMonitoringRecord> concurrentWorkStealingPipeFactoriesZip = new ConcurrentWorkStealingPipeFactory<IMonitoringRecord>();
		final ConcurrentWorkStealingPipe<IMonitoringRecord> zipPipe = concurrentWorkStealingPipeFactoriesZip.create();
		zipPipe.connect(zipFile2RecordFilter.recordOutputPort, recordMerger.getNewInputPort());

		this.fileInputPort = isDirectoryFilter.inputPort;
		this.recordOutputPort = recordMerger.outputPort;

		this.schedulableStages.add(isDirectoryFilter);

		// this.schedulableStages.add(classNameRegistryCreationFilter);
		// this.schedulableStages.add(directory2FilesFilter);
		// this.schedulableStages.add(fileMerger);
		// this.schedulableStages.add(fileExtensionFilter);

		this.schedulableStages.add(datFile2RecordFilter);
		this.schedulableStages.add(binaryFile2RecordFilter);
		this.schedulableStages.add(zipFile2RecordFilter);
		this.schedulableStages.add(recordMerger);
	}

}
