package kieker.monitoring.writer.pipe;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.writer.raw.IRawDataWriter;

public class OSPipeWriter implements IRawDataWriter {

	private static final String PREFIX = OSPipeWriter.class.getName() + ".";
	
	/** The name of the configuration property for the output pipe name. */
	public static final String CONFIG_PROPERTY_PIPE_NAME = PREFIX + "pipeName";
		
	private static final String SYMBOLIC_NAME_STDOUT = "-";
		
	private static final Log LOG = LogFactory.getLog(OSPipeWriter.class);
	
	private final OutputStream outputStream;
	
	public OSPipeWriter(final Configuration configuration) {
		String pipeName = configuration.getStringProperty(CONFIG_PROPERTY_PIPE_NAME);
		
		// Assume stdout if no pipe name is given
		if (pipeName == null) {
			pipeName = SYMBOLIC_NAME_STDOUT;
			LOG.warn("No output name given, stout assumed.");			
		}
		
		this.outputStream = this.openOutputStream(pipeName);
	}
	
	private OutputStream openOutputStream(final String outputStreamName) {
		try {
			// Open stdout if desired, otherwise try to open the file with the given name
			if (SYMBOLIC_NAME_STDOUT.equals(outputStreamName)) {
				return System.out;
			} else {
				return new FileOutputStream(outputStreamName);
			}
		} catch (final FileNotFoundException e) {
			LOG.error("Unable to open output pipe '" + outputStreamName + "'.", e);
			return null;
		}
	}
	
	@Override
	public void onInitialization() {
		// Do nothing
	}
	
	@Override
	public void writeData(final ByteBuffer buffer) {
		final byte[] rawData = new byte[buffer.limit()];
		buffer.get(rawData); 
		
		try {
			this.outputStream.write(rawData);
		} catch (final IOException e) {
			LOG.error("An exception occurred writing the data.", e);
		}
	}

	@Override
	public boolean requiresWrappedData() {
		return true;
	}
	
	@Override
	public void onTermination() {
		try {
		if (this.outputStream != null) {
			// Flush and close the output stream
			this.outputStream.flush();
			this.outputStream.close();
		}
		} catch (final IOException e) {
			LOG.error("Error closing output pipe.", e);
		}
	}

}
