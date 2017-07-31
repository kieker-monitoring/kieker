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
package kieker.common.record.system;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class NetworkUtilizationRecordFactory implements IRecordFactory<NetworkUtilizationRecord> {

	@Override
	public NetworkUtilizationRecord create(final IValueDeserializer deserializer) {
		return new NetworkUtilizationRecord(deserializer);
	}

	@Override
	public NetworkUtilizationRecord create(final Object[] values) {
		return new NetworkUtilizationRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return NetworkUtilizationRecord.SIZE;
	}
}
