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
package kieker.extension.cassandra;

import com.datastax.driver.core.Row;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.io.AbstractValueDeserializer;
import kieker.common.record.io.IValueDeserializer;

/**
 * 
 * @author Reiner Jung
 *
 */
public class CassandraValueDeserializer extends AbstractValueDeserializer implements IValueDeserializer {
	
	private Row row;
	private int column;
	
	public CassandraValueDeserializer(final Row row) {
		this.row = row;
		this.column = 2; // the original implementation skips two? values
	}

	@Override
	public boolean getBoolean() {
		return this.row.getBool(column++);
	}

	@Override
	public byte getByte() throws NumberFormatException {
		return this.row.getByte(column++);
	}

	@Override
	public char getChar() {
		return this.row.getString(column++).charAt(0);
	}

	@Override
	public short getShort() throws NumberFormatException {
		return this.row.getShort(column++);
	}

	@Override
	public int getInt() throws NumberFormatException {
		return this.row.getInt(column++);
	}

	@Override
	public long getLong() throws NumberFormatException {
		return this.row.getLong(column++);
	}

	@Override
	public float getFloat() throws NumberFormatException {
		return this.row.getFloat(column++);
	}

	@Override
	public double getDouble() throws NumberFormatException {
		return this.row.getDouble(column++);
	}

	@Override
	public String getString() {
		return this.row.getString(column++);
	}

	@Override
	public <T extends Enum<T>> T getEnumeration(final Class<T> clazz) throws RecordInstantiationException {
		final int value = this.row.getInt(column++);
		return this.enumerationValueOf(clazz, value);
	}

}
