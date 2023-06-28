package kieker.analysis.generic.source.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.generic.depcompression.AbstractDecompressionFilter;
import kieker.analysis.generic.depcompression.NoneDecompressionFilter;
import kieker.analysis.util.FSReaderUtil;
import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.reader.ReaderRegistry;
import teetime.framework.OutputPort;

class OneRegistryReader {
	
	protected final Logger logger;
	private final boolean verbose;
	private final int dataBufferSize;
	private final OutputPort<IMonitoringRecord> outputPort;
	private final File directory;

	private final ReaderRegistry<String> registry = new ReaderRegistry<>();
	
	public OneRegistryReader(File directory, boolean verbose, final int dataBufferSize, OutputPort<IMonitoringRecord> outputPort) {
		this.logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		this.directory = directory;
		this.verbose = verbose;
		this.dataBufferSize = dataBufferSize;
		this.outputPort = outputPort;
		
		final File[] mapFiles = directory.listFiles(DirectoryReaderStage.MAP_FILTER);
		readMapFiles(registry, mapFiles);
	}
	
	private void readMapFiles(final ReaderRegistry<String> registry, final File[] mapFiles) {
		for (final File mapFile : mapFiles) {
			final String mapFileName = mapFile.getName();
			try (InputStream inputStream = Files.newInputStream(mapFile.toPath(), StandardOpenOption.READ)) {
				this.readMapFile(inputStream, mapFileName, registry);
			} catch (final IOException e) {
				this.logger.error("Cannot find map file {}.", mapFileName);
			}
		}
	}
	
	/**
	 * Read a map file stream and initialize the registry.
	 *
	 * @param inputStream
	 *            the input stream
	 * @param logFileName
	 *            the name of the log file used for user feedback
	 * @param registry
	 *            string registry
	 */
	private void readMapFile(final InputStream inputStream, final String mapFileName, final ReaderRegistry<String> registry) {
		final AbstractDecompressionFilter decompressionFilter = DirectoryReaderStage.findDecompressionFilterByExtension(mapFileName);
		this.logger.debug("Reading map file {}", mapFileName);

		/** detecting correct map file deserializer. */
		final Class<? extends AbstractMapDeserializer> deserializerClass;
		if (decompressionFilter instanceof NoneDecompressionFilter) {
			deserializerClass = FSReaderUtil.findMapDeserializer(mapFileName);
		} else {
			final String baseName = mapFileName.substring(0, mapFileName.lastIndexOf('.') - 1);
			deserializerClass = FSReaderUtil.findMapDeserializer(baseName);
		}

		try (InputStream chainedInputStream = decompressionFilter.chainInputStream(inputStream)) {
			try {
				final AbstractMapDeserializer deserializer = deserializerClass.getConstructor().newInstance();
				deserializer.processDataStream(decompressionFilter.chainInputStream(inputStream), registry, mapFileName);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				this.logger.error("Cannot instantiate filter {} for decompression.", deserializerClass.getName());
			}
		} catch (final IOException ex) {
			this.logger.error("Reading map file {} failed.", mapFileName);
		}
	}

	public void readLogFile(File logFile) {
		if (logFile.getAbsolutePath().startsWith(directory.getAbsolutePath())) {
			final String logFileName = logFile.getName();
			try (InputStream inputStream = Files.newInputStream(logFile.toPath(), StandardOpenOption.READ)) {
				if (this.verbose) {
					this.logger.info("Reading log file {}", logFile);
				} else {
					this.logger.debug("Reading log file {}", logFile);
				}
				this.readLogFile(inputStream, logFileName, registry);
			} catch (final IOException e) {
				this.logger.error("Cannot find log file {}.", logFileName);
			}
		}
	}
	
	/**
	 * Read a log file stream and produce Kieker events.
	 *
	 * @param inputStream
	 *            the input stream
	 * @param logFileName
	 *            the name of the log file used for user feedback
	 * @param registry
	 *            string registry
	 */
	private void readLogFile(final InputStream inputStream, final String logFileName, final ReaderRegistry<String> registry) {
		final AbstractDecompressionFilter decompressionFilter = DirectoryReaderStage.findDecompressionFilterByExtension(logFileName);

		/** detecting correct log file deserializer. */
		final Class<? extends AbstractEventDeserializer> deserializerClass;
		if (decompressionFilter instanceof NoneDecompressionFilter) {
			deserializerClass = FSReaderUtil.findEventDeserializer(logFileName);
		} else {
			final String baseName = logFileName.substring(0, logFileName.lastIndexOf('.'));
			deserializerClass = FSReaderUtil.findEventDeserializer(baseName);
		}

		if (deserializerClass != null) {
			try (InputStream chainedInputStream = decompressionFilter.chainInputStream(inputStream)) {
				try {
					final AbstractEventDeserializer deserializer = deserializerClass.getConstructor(Integer.class, ReaderRegistry.class)
							.newInstance(this.dataBufferSize, registry);

					deserializer.processDataStream(chainedInputStream, this.outputPort);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					this.logger.error("Cannot instantiate filter {} for decompression.", deserializerClass.getName());
				}
			} catch (final IOException e) {
				this.logger.error("Reading log file {} failed.", logFileName);
			}
		} else {
			this.logger.debug("Skipping file {}, as the extension indicates that it is not a log file.", logFileName);
		}
	}
		
}