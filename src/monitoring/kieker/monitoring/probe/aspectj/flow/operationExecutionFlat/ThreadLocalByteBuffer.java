package kieker.monitoring.probe.aspectj.flow.operationExecutionFlat;

import java.nio.ByteBuffer;

public class ThreadLocalByteBuffer extends ThreadLocal<ByteBuffer> {
	private final int messageBufferSize;

	public ThreadLocalByteBuffer(final int messageBufferSize) {
		this.messageBufferSize = messageBufferSize;
	}

	@Override
	protected ByteBuffer initialValue() {
		return ByteBuffer.allocateDirect(messageBufferSize);
	}
}
