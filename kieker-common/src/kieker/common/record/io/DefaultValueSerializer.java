package kieker.common.record.io;

import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;

public class DefaultValueSerializer implements IValueSerializer {

	private static final DefaultValueSerializer INSTANCE = new DefaultValueSerializer();
	
	public static DefaultValueSerializer instance() {
		return INSTANCE;
	}
	
	protected DefaultValueSerializer() {
		// Empty constructor
	}
	
	@Override
	public void putByte(final byte value, final ByteBuffer buffer) {
		buffer.put(value);
	}
	
	@Override
	public void putInt(final int value, final ByteBuffer buffer) {
		buffer.putInt(value);
	}

	@Override
	public void putLong(final long value, final ByteBuffer buffer) {
		buffer.putLong(value);
	}

	@Override
	public void putDouble(final double value, final ByteBuffer buffer) {
		buffer.putDouble(value);
	}
	
	@Override
	public void putBytes(final byte[] value, final ByteBuffer buffer) {
		buffer.put(value);
	}

	@Override
	public void putString(final String value, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		int stringId = stringRegistry.get(value);
		this.putInt(stringId, buffer);
	}

}
