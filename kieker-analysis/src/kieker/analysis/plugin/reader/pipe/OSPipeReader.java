package kieker.analysis.plugin.reader.pipe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.newio.AbstractRawDataReader;
import kieker.analysis.plugin.reader.newio.IRawDataProcessor;
import kieker.analysis.plugin.reader.newio.IRawDataUnwrapper;
import kieker.analysis.plugin.reader.newio.Outcome;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * A reader that reads data from a OS pipe, such as {@code stdin} or a named pipe. 
 * 
 * @author Holger Knoche
 * @since 2.0
 */
public class OSPipeReader extends AbstractRawDataReader {

	/** The name of the configuration property for the input pipe name. */
	public static final String CONFIG_PROPERTY_PIPE_NAME = "pipeName";
		
	private static final String SYMBOLIC_NAME_STDIN = "-";
		
	private static final Log LOG = LogFactory.getLog(OSPipeReader.class);
	
	private final boolean inputStreamInitialized;	
		
	private final IRawDataUnwrapper dataUnwrapper;
	
	private volatile boolean terminated = false;
		
	/**
	 * Creates a new OS pipe reader with the given configuration, sending the data to the given processor.
	 * @param configuration
	 * @param unwrapperType
	 * @param processor
	 */
	public OSPipeReader(final Configuration configuration, Class<? extends IRawDataUnwrapper> unwrapperType, final IRawDataProcessor processor) throws AnalysisConfigurationException {
		super(processor);
		
		String pipeName = configuration.getStringProperty(CONFIG_PROPERTY_PIPE_NAME);
		
		// Assume stdin if no pipe name is given
		if (pipeName == null) {
			pipeName = SYMBOLIC_NAME_STDIN;
			LOG.info("No input pipe name given, stdin assumed.");
		}
		
		InputStream inputStream = this.openInputStream(pipeName);
		
		if (inputStream != null) {
			this.inputStreamInitialized = true;
			this.dataUnwrapper = this.instantiateUnwrapper(unwrapperType, inputStream);			
		} else {
			this.inputStreamInitialized = false;
			this.dataUnwrapper = null;			
		}
	}
		
	private InputStream openInputStream(String inputStreamName) throws AnalysisConfigurationException {		
		try {
			// Open stdin if desired, otherwise try to open the file with the given name
			if (SYMBOLIC_NAME_STDIN.equals(inputStreamName)) {
				return System.in;
			} else {
				return new FileInputStream(inputStreamName);
			}
		} catch (FileNotFoundException e) {
			throw new AnalysisConfigurationException("Unable to open input pipe.", e);
		}
	}
	
	private IRawDataUnwrapper instantiateUnwrapper(Class<? extends IRawDataUnwrapper> unwrapperType, InputStream inputStream) {
		if (unwrapperType == null) {
			LOG.error("No unwrapper type was supplied.");
			return null;
		}

		IRawDataUnwrapper unwrapper = null;
		
		try {
			// Try to instantiate the unwrapper
			unwrapper = unwrapperType.getConstructor(InputStream.class).newInstance(inputStream);
		} catch (NoSuchMethodException e) {
			LOG.error("Class " + unwrapperType.getName() + " must implement a (public) constructor that accepts an input stream.", e);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException e) {
			LOG.error("Unable to instantiate " + unwrapperType.getName() + ".", e);
		} 
		
		return unwrapper;
	}
	
	@Override
	public Outcome onInitialization() {
		// Check whether the input stream was successfully initialized during construction
		if (this.inputStreamInitialized) {
			return Outcome.SUCCESS;
		} else {
			return Outcome.FAILURE;
		}
		
	}

	@Override
	public Outcome read() {
		IRawDataUnwrapper unwrapper = this.dataUnwrapper;
		
		if (unwrapper == null) {
			return Outcome.FAILURE;
		}		
		
		try {
			Outcome outcome;
			
			if (unwrapper.supportsCharacterData()) {
				outcome = this.readCharacterData();
			} else {
				outcome = this.readBinaryData();
			}
			
			return outcome;
		} catch (IOException e) {
			LOG.error("Error reading data.", e);
			return Outcome.FAILURE;
		}
	}
	
	private Outcome readBinaryData() throws IOException {
		IRawDataUnwrapper unwrapper = this.dataUnwrapper;
		IRawDataProcessor processor = this.dataProcessor;
		
		while (!this.terminated) {
			ByteBuffer dataChunk = unwrapper.fetchBinaryData();
			
			// Null represents "end of data"
			if (dataChunk == null) {
				this.terminated = true;
				break;
			}
			
			processor.decodeBytesAndDeliverRecords(dataChunk, dataChunk.limit());
		}
		
		return Outcome.SUCCESS;
	}
	
	private Outcome readCharacterData() throws IOException {
		IRawDataUnwrapper unwrapper = this.dataUnwrapper;
		IRawDataProcessor processor = this.dataProcessor;
		
		while (!this.terminated) {
			CharBuffer dataChunk = unwrapper.fetchCharacterData();
			
			// Null represents "end of data"
			if (dataChunk == null) {
				this.terminated = true;
				break;
			}
			
			processor.decodeCharactersAndDeliverRecords(dataChunk, dataChunk.limit());
		}
		
		return Outcome.SUCCESS;
	}

	@Override
	public Outcome onTermination() {
		this.terminated = true;
		return Outcome.SUCCESS;
	}

}
