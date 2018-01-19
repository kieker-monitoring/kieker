/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;

/**
 * Binary wrapper which prepends an int (big-endian) to the data block
 * specifying its size in bytes.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public class BinaryChunkLengthWrapper extends AbstractBinaryDataWrapper {	

	private final ByteBuffer outputBuffer;
	
	public BinaryChunkLengthWrapper(final int sourceBufferSize) {
		// The wrapper adds an int, i.e. 4 bytes
		final int requiredOutputBufferSize = sourceBufferSize + 4;		
		this.outputBuffer = ByteBuffer.allocate(requiredOutputBufferSize);
	}
	
	@Override
	public ByteBuffer wrap(final ByteBuffer data) {
		final ByteBuffer targetBuffer = this.outputBuffer;
		final int dataSize = data.limit();
		
		// Rewind the target buffer and prepend the data length
		targetBuffer.rewind();
		targetBuffer.putInt(dataSize);
		targetBuffer.put(data);
		
		return targetBuffer;
	}

}
