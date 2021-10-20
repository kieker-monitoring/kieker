/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.database;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 * 
 * @since 1.14
 */
public final class BeforeDatabaseEventFactory implements IRecordFactory<BeforeDatabaseEvent> {

	@Override
	public BeforeDatabaseEvent create(final IValueDeserializer deserializer) throws RecordInstantiationException {
		return new BeforeDatabaseEvent(deserializer);
	}

	@Override
	public String[] getValueNames() {
		return BeforeDatabaseEvent.VALUE_NAMES; // NOPMD
	}

	@Override
	public Class<?>[] getValueTypes() {
		return BeforeDatabaseEvent.TYPES; // NOPMD
	}

	public int getRecordSizeInBytes() {
		return BeforeDatabaseEvent.SIZE;
	}
}
