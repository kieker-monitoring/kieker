package kieker.common.record.io;

import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;

public class DefaultValueDeserializer implements IValueDeserializer {

	private static final DefaultValueDeserializer INSTANCE = new DefaultValueDeserializer();
	
	public static DefaultValueDeserializer instance() {
		return INSTANCE;
	}
	
	protected DefaultValueDeserializer() {
		// Empty constructor
	}
	
	@Override
	public byte getByte(final ByteBuffer buffer) {
		return buffer.get();
	}
	
	@Override
	public int getInt(final ByteBuffer buffer) {
		return buffer.getInt();
	}

	@Override
	public long getLong(final ByteBuffer buffer) {
		return buffer.getLong();
	}
	
	@Override
	public double getDouble(final ByteBuffer buffer) {
		return buffer.getDouble();
	}

	@Override
	public String getString(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		int stringId = this.getInt(buffer);
		return stringRegistry.get(stringId);
	}

	@Override
	public byte[] getBytes(final ByteBuffer buffer, final byte[] target) {
		buffer.get(target);
		return target;
	}

}
