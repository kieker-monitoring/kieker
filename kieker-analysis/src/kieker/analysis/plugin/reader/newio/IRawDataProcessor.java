package kieker.analysis.plugin.reader.newio;

import java.nio.ByteBuffer;

/**
 * Interface for raw data processors, i.e. classes which decode raw data to
 * records and feed them into a processing pipeline.
 * 
 * @author Holger Knoche
 *
 * @since 1.13
 */
public interface IRawDataProcessor {
	
	/**
	 * Decodes the given raw data and delivers the decoded records.
	 * 
	 * @param rawData
	 *            The raw data to decode
	 */
	public void decodeAndDeliverRecords(byte[] rawData);
	
	/**
	 * Decodes the given raw data and delivers the decoded records.
	 * 
	 * @param rawData
	 *            The raw data to decode
	 * @param dataSize
	 *            The size of the contained data
	 */
	public void decodeAndDeliverRecords(final ByteBuffer rawData, final int dataSize);

}
