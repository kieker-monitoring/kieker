package kieker.common.record.io;

import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;

/**
 * Interface for value deserializers for use by monitoring records.
 * @author Holger Knoche
 * @since 1.13
 */
public interface IValueDeserializer {

	public byte getByte(ByteBuffer buffer);
	
	public int getInt(ByteBuffer buffer);
	
	public long getLong(ByteBuffer buffer);
	
	public double getDouble(ByteBuffer buffer);
	
	public String getString(ByteBuffer buffer, IRegistry<String> stringRegistry);	
	
	public byte[] getBytes(ByteBuffer buffer, byte[] target);
	
}
