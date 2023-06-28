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
package kieker.analysis.generic.source.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import kieker.analysis.generic.depcompression.AbstractDecompressionFilter;
import kieker.analysis.generic.depcompression.Bzip2DecompressionFilter;
import kieker.analysis.generic.depcompression.DeflateDecompressionFilter;
import kieker.analysis.generic.depcompression.GZipDecompressionFilter;
import kieker.analysis.generic.depcompression.NoneDecompressionFilter;
import kieker.analysis.generic.depcompression.XZDecompressionFilter;
import kieker.analysis.generic.depcompression.ZipDecompressionFilter;
import kieker.analysis.util.FSReaderUtil;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.FSUtil;
import teetime.stage.basic.AbstractTransformation;

/**
 * Read a kieker log directory. The filter receives a directory as input and
 * outputs all events collected in the directory.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DirectoryReaderStage extends AbstractTransformation<File, IMonitoringRecord> {

	public static final FilenameFilter MAP_FILTER = new MapFileFilter();
	private final Integer dataBufferSize;
	private final boolean verbose;
	private final List<OneRegistryReader> registryReaders = new LinkedList<>();
	private final PriorityQueue<File> filesToRead = new PriorityQueue<>(new FileNameDateComparator());

	public DirectoryReaderStage(final boolean verbose, final int dataBufferSize) {
		this.verbose = verbose;
		this.dataBufferSize = dataBufferSize;
	}

	@Override
	protected void execute(final File directory) {
		logger.info("Reading folder: {}", directory);
		
		OneRegistryReader reader = new OneRegistryReader(directory, verbose, dataBufferSize, outputPort);
		registryReaders.add(reader);
		
		List<File> currentFolderDataFiles = Arrays.asList(directory.listFiles(MapFileFilter.NOT_FILTER));
		filesToRead.addAll(currentFolderDataFiles);
	}
	
	@Override
	protected void onTerminating() {
		for (File file : filesToRead) {
			System.out.println("Start: " + file.getAbsolutePath());
			if (!DirectoryReaderStage.MAP_FILTER.accept(file.getParentFile(), file.getName())) {
				for (OneRegistryReader reader : registryReaders) {
					reader.readLogFile(file);
				}
			}
		}
		super.onTerminating();
	}

	public static AbstractDecompressionFilter findDecompressionFilterByExtension(final String filename) {
		final String extension = FSReaderUtil.getExtension(filename);
		if (FSUtil.GZIP_FILE_EXTENSION.equals(extension)) {
			return new GZipDecompressionFilter();
		} else if (FSUtil.DEFLATE_FILE_EXTENSION.equals(extension)) {
			return new DeflateDecompressionFilter();
		} else if (FSUtil.XZ_FILE_EXTENSION.equals(extension)) {
			return new XZDecompressionFilter();
		} else if (FSUtil.ZIP_FILE_EXTENSION.equals(extension)) {
			return new ZipDecompressionFilter();
		} else if (FSUtil.BZIP2_FILE_EXTENSION.equals(extension)) {
			return new Bzip2DecompressionFilter();
		} else {
			return new NoneDecompressionFilter();
		}
	}
}
