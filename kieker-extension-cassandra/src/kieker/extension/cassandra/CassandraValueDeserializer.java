/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
 * @since 1.16
 */
public class CassandraValueDeserializer extends AbstractValueDeserializer implements IValueDeserializer {

	private final Row row;
	private int column;

	public CassandraValueDeserializer(final Row row) {
		this.row = row;
		this.column = 2; // the original implementation skips two? values
	}

	@Override
	public boolean getBoolean() {
		return this.row.getBool(this.column++);
	}

	@Override
	public byte getByte() throws NumberFormatException { // NOPMD
		return this.row.getByte(this.column++);
	}

	@Override
	public char getChar() {
		return this.row.getString(this.column++).charAt(0);
	}

	@Override
	public short getShort() throws NumberFormatException { // NOPMD
		return this.row.getShort(this.column++);
	}

	@Override
	public int getInt() throws NumberFormatException { // NOPMD
		return this.row.getInt(this.column++);
	}

	@Override
	public long getLong() throws NumberFormatException { // NOPMD
		return this.row.getLong(this.column++);
	}

	@Override
	public float getFloat() throws NumberFormatException { // NOPMD
		return this.row.getFloat(this.column++);
	}

	@Override
	public double getDouble() throws NumberFormatException { // NOPMD
		return this.row.getDouble(this.column++);
	}

	@Override
	public String getString() {
		return this.row.getString(this.column++);
	}

	@Override
	public <T extends Enum<T>> T getEnumeration(final Class<T> clazz) throws RecordInstantiationException {
		final int value = this.row.getInt(this.column++);
		return this.enumerationValueOf(clazz, value);
	}

}
