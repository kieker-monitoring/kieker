/***************************************************************************
 * Copyright 2017 iObserve Project
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
package kieker.common.record.system;


import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public final class MemSwapUsageRecordFactory implements IRecordFactory<MemSwapUsageRecord> {
	
	
	@Override
	public MemSwapUsageRecord create(final IValueDeserializer deserializer) {
		return new MemSwapUsageRecord(deserializer);
	}
	
	@Override
	@Deprecated
	public MemSwapUsageRecord create(final Object[] values) {
		return new MemSwapUsageRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return MemSwapUsageRecord.SIZE;
	}
}
