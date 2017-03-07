package kieker.common.record.io;

import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;

/**
 * Interface for value serializers for use by monitoring records.
 * @author Holger Knoche
 * @since 1.13
 */
public interface IValueSerializer {
	
	public void putByte(byte value, ByteBuffer buffer);
	
	public void putInt(int value, ByteBuffer buffer);
	
	public void putLong(long value, ByteBuffer buffer);
	
	public void putDouble(double value, ByteBuffer buffer);
	
	public void putBytes(byte[] value, ByteBuffer buffer);
	
	public void putString(String value, ByteBuffer buffer, IRegistry<String> stringRegistry);

}
