/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.aspectj.database;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 * 
 * @since 1.14
 */
@Aspect
public class Fullinstrumentation extends AbstractAspect {

	// Relevant JDBC calls based on
	// http://docs.oracle.com/javase/7/docs/api/java/sql/Statement.html
	private static final String RELATED_CALLS = "call(java.sql.Statement java.sql.Connection.createStatement()) "
			+ "|| call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String)) "
			+ "|| call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String, String[])) "
			+ "|| call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String, int)) "
			+ "|| call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String, int[])) "
			+ "|| call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String, int, int)) "
			+ "|| call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String, int, int, int)) "
			+ "|| call(java.sql.CallableStatement java.sql.Connection.prepareCall(String)) "
			+ "|| call(java.sql.CallableStatement java.sql.Connection.prepareCall(String, int, int)) "
			+ "|| call(java.sql.CallableStatement java.sql.Connection.prepareCall(String, int, int, int)) "
			+ "|| call(void java.sql.Statement.addBatch(String)) "
			+ "|| call(java.sql.Statement java.sql.Statement.addBatch(int, int)) "
			+ "|| call(java.sql.Statement java.sql.Statement.addBatch(int, int, int)) "
			+ "|| call(int[] java.sql.Statement.executeBatch())"
			+ "|| call(boolean java.sql.Statement.execute(String)) "
			+ "|| call(boolean java.sql.Statement.execute(String,int)) "
			+ "|| call(boolean java.sql.Statement.execute(String,int[])) "
			+ "|| call(boolean java.sql.Statement.execute(String,String[])) "
			+ "|| call(java.sql.ResultSet java.sql.Statement.executeQuery(String)) "
			+ "|| call(int java.sql.Statement.executeUpdate(String)) "
			+ "|| call(int java.sql.Statement.executeUpdate(String, int)) "
			+ "|| call(int java.sql.Statement.executeUpdate(String, int[])) "
			+ "|| call(int java.sql.Statement.executeUpdate(String, String[])) "
			+ "|| call(void java.sql.PreparedStatement.setArray(..)) "
			// void java.sql.PreparedStatement.setAsciiStream(int, InputStream, int)
			+ "|| call(void java.sql.PreparedStatement.setAsciiStream(..)) "
			// void java.sql.PreparedStatement.setAsciiStream(int, InputStream, long)
			+ "|| call(void java.sql.PreparedStatement.setAsciiStream(..)) "
			// void java.sql.PreparedStatement.setBigDecimal(int, BigDecimal)
			+ "|| call(void java.sql.PreparedStatement.setBigDecimal(..)) "
			// void java.sql.PreparedStatement.setBinaryStream(int, InputStream))
			+ "|| call(void java.sql.PreparedStatement.setBinaryStream(..)) "
			// void java.sql.PreparedStatement.setBinaryStream(int, InputStream, int)
			// void java.sql.PreparedStatement.setBinaryStream(int, InputStream, long)
			+ "|| call(void java.sql.PreparedStatement.setBinaryStream(..)) "
			// void java.sql.PreparedStatement.setBlob(int, Blob)
			// void java.sql.PreparedStatement.setBlob(int, InputStream)
			// void java.sql.PreparedStatement.setBlob(int, InputStream, long)
			+ "|| call(void java.sql.PreparedStatement.setBlob(..)) "
			+ "|| call(void java.sql.PreparedStatement.setBoolean(int, boolean)) "
			+ "|| call(void java.sql.PreparedStatement.setByte(int, byte)) "
			+ "|| call(void java.sql.PreparedStatement.setBytes(int, byte[])) "
			// void java.sql.PreparedStatement.setCharacterStream(int, Reader)
			// void java.sql.PreparedStatement.setCharacterStream(int, Reader, int)
			// void java.sql.PreparedStatement.setCharacterStream(int, Reader, long)
			+ "|| call(void java.sql.PreparedStatement.setCharacterStream(..)) "
			// void java.sql.PreparedStatement.setClob(int, Clob)
			// void java.sql.PreparedStatement.setClob(int, Reader)
			// void java.sql.PreparedStatement.setClob(int, Reader, long)
			+ "|| call(void java.sql.PreparedStatement.setClob(..)) "
			// void java.sql.PreparedStatement.setDate(int, Date)
			// void java.sql.PreparedStatement.setDate(int, Date, Calendar)
			+ "|| call(void java.sql.PreparedStatement.setDate(..)) "
			+ "|| call(void java.sql.PreparedStatement.setDouble(int, double)) "
			+ "|| call(void java.sql.PreparedStatement.setFloat(int, float)) "
			+ "|| call(void java.sql.PreparedStatement.setInt(int, int)) "
			+ "|| call(void java.sql.PreparedStatement.setLong(int, long)) "
			// void java.sql.PreparedStatement.setNCharacterStream(int, Reader)
			// void java.sql.PreparedStatement.setNCharacterStream(int, Reader, long)
			+ "|| call(void java.sql.PreparedStatement.setNCharacterStream(..)) "
			// void java.sql.PreparedStatement.setNClob(int, NClob)
			// void java.sql.PreparedStatement.setNClob(int, Reader)
			// void java.sql.PreparedStatement.setNClob(int, Reader, long)
			+ "|| call(void java.sql.PreparedStatement.setNClob(..)) "
			+ "|| call(void java.sql.PreparedStatement.setNString(int, String)) "
			+ "|| call(void java.sql.PreparedStatement.setNull(int, int)) "
			+ "|| call(void java.sql.PreparedStatement.setNull(int, int, String)) "
			+ "|| call(void java.sql.PreparedStatement.setObject(int, Object)) "
			+ "|| call(void java.sql.PreparedStatement.setObject(int, Object, int)) "
			+ "|| call(void java.sql.PreparedStatement.setObject(int, Object, int, int)) "
			// void java.sql.PreparedStatement.setRef(int, Ref)
			+ "|| call(void java.sql.PreparedStatement.setRef(..)) "
			// void java.sql.PreparedStatement.setRowId(int, RowId)
			+ "|| call(void java.sql.PreparedStatement.setRowId(..)) "
			// void java.sql.PreparedStatement.setShort(int, short)
			+ "|| call(void java.sql.PreparedStatement.setShort(..)) "
			// void java.sql.PreparedStatement.setSQLML(int, SQLML)
			+ "|| call(void java.sql.PreparedStatement.setSQLML(..)) "
			+ "|| call(void java.sql.PreparedStatement.setString(int, String)) "
			// void java.sql.PreparedStatement.setTime(int, Time)
			// void java.sql.PreparedStatement.setTime(int, Time, Calendar)
			+ "|| call(void java.sql.PreparedStatement.setTime(..)) "
			// void java.sql.PreparedStatement.setTimestamp(int, Timestamp)
			// void java.sql.PreparedStatement.setTimestamp(int, Timestamp,
			// Calendar)
			+ "|| call(void java.sql.PreparedStatement.setTimestamp(..)) "
			// void java.sql.PreparedStatement.setUnicodeStream(int, InputStream, int)
			+ "|| call(void java.sql.PreparedStatement.setUnicodeStream(..)) "
			// void java.sql.PreparedStatement.setURL(int, URL)
			+ "|| call(void java.sql.PreparedStatement.setURL(..)) "
			+ "|| call(boolean java.sql.PreparedStatement.execute()) "
			+ "|| call(java.sql.ResultSet java.sql.PreparedStatement.executeQuery()) "
			+ "|| call(int java.sql.PreparedStatement.executeUpdate())";

	/**
	 * Default constructor.
	 */
	public Fullinstrumentation() {
		// empty default constructor
	}

	@Override
	@Pointcut(RELATED_CALLS)
	public void monitoredOperation() {
		// Aspect Declaration (MUST be empty)
	}
}
