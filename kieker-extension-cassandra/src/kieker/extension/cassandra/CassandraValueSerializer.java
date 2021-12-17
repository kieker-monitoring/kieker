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

import com.datastax.driver.core.BoundStatement;

import kieker.common.record.io.IValueSerializer;

/**
 * @author Reiner Jung
 * @since 1.16
 */
public class CassandraValueSerializer implements IValueSerializer { // NOPMD TooManyMethods
	
	private final BoundStatement boundStatement;
	private int column;

	public CassandraValueSerializer(final BoundStatement boundStatement) {
		this.boundStatement = boundStatement;
		this.column = 1; // start at one, as 0 is the column of the benchmarkId
	}

	@Override
	public void putBoolean(final boolean value) {
		this.boundStatement.setBool(column++, value);
	}

	@Override
	public void putByte(final byte value) {
		this.boundStatement.setByte(column++, value);
	}

	@Override
	public void putChar(final char value) {
		this.boundStatement.setString(column++, String.valueOf(value));
	}

	@Override
	public void putShort(final short value) {
		this.boundStatement.setShort(column++, value);
	}

	@Override
	public void putInt(final int value) {
		this.boundStatement.setInt(column++, value);
	}

	@Override
	public void putLong(final long value) {
		this.boundStatement.setLong(column++, value);
	}

	@Override
	public void putFloat(final float value) {
		this.boundStatement.setFloat(column++, value);
	}

	@Override
	public void putDouble(final double value) {
		this.boundStatement.setDouble(column++, value);
	}

	@Override
	public <T extends Enum<T>> void putEnumeration(final T value) {
		this.boundStatement.setInt(column++, value.ordinal());
	}

	@Override
	public void putBytes(final byte[] value) {
		throw new UnsupportedOperationException("Plain byte arrays cannot be stored in Cassandra DB.");
	}

	@Override
	public void putString(final String value) {
		this.boundStatement.setString(column++, value);
	}

}
