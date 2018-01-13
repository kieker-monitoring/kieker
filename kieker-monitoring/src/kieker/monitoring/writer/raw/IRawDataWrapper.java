package kieker.monitoring.writer.raw;

import java.nio.Buffer;

/**
 * Raw data wrappers wrap chunks of raw data that are sent over a low-level
 * medium that does not provide it own means to reconstruct the original data
 * chunk in its entirety. For instance, a chunk sent to a (named) pipe may be
 * sliced into smaller chunks by the operating system. For such media,
 * additional wrapping is required to recover the original chunk. Higher-level
 * media like TCP connections or messaging provide the necessary facilities and
 * do not require wrapping.
 * 
 * @author Holger Knoche
 * @since 2.0
 *
 */
public interface IRawDataWrapper<T extends Buffer> {

	/**
	 * Denotes whether this wrapper supports binary data.
	 * @return see above
	 */
	public boolean supportsBinaryData();

	/**
	 * Denotes whether this wrapper supports character data.
	 * @return see above
	 */
	public boolean supportsCharacterData();
	
	/**
	 * Wraps the given chunk of data.
	 * @param data A buffer containing the data to wrap. The buffer is expected to be "flipped".
	 * @return The wrapped data chunk
	 */
	public T wrap(T data);
			
}
