/**
 * Copyright 2018 Armin Moebius, Sven Ulrich (https://www.rbee.io)
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
 */

package kieker.monitoring.writer.cassandra;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * 
 * @author Armin Moebius, Sven Ulrich
 *
 */
public class CassandraDb
{
	/** Fields **/
	
	private Cluster					 cluster;
	private List<InetSocketAddress>	 contactPoints;
	private String					 defaultKeyspace;
	private String					 tablePrefix;
	private boolean					 dropTables;
	private boolean					 init = false;
	private Session					 session;
	private String					 cassandraDefaultHost = "127.0.0.1";
	private String					 cassandraDefaultPort = "9042";	
	private List<PreparedStatement>	 preparedStatements;
	
	private HashMap<Class<?>, String> databaseTypes;
	
	private static final Log LOG = LogFactory.getLog(CassandraDb.class);
	
	/** Constructor **/
	
	/**
	 * Creates a new instance of this class
	 */
	public CassandraDb() 
	{
		initDefault();
	}
	
	/**
	 * Creates a new instance of this class using the given parameter
	 * @param keyspace
	 * @param contactpoints
	 * @param tablePrefix
	 * @param dropTables
	 * @param defaultId
	 * @param idType
	 * @throws Exception
	 */
	public CassandraDb(String keyspace, String[] contactpoints, String tablePrefix, boolean dropTables) throws Exception
	{
		this();
		initDatabase(keyspace, contactpoints, tablePrefix, dropTables);
	}
	
	/** Setter **/
	
	public void setDefaultKeyspace(String defaultKeyspace)
	{
		this.defaultKeyspace = defaultKeyspace;
	}
	
	public void setTablePrefix(String prefix)
	{
		this.tablePrefix = prefix;
	}
	
	public void setDropTables(boolean drop)
	{
		this.dropTables = drop;
	}
	/** Getter **/
	
	public String getDefaultKeyspace()
	{
		return this.defaultKeyspace;
	}
	
	public String getTablePrefix()
	{
		return this.tablePrefix;
	}
	
	public boolean getDropTables()
	{
		return this.dropTables;
	}

	public boolean isInit()
	{
		return init;
	}

	/** private **/
	/**
	 * Sets the default values for all fields
	 */
	private void initDefault()
	{
		this.contactPoints	 = new ArrayList<InetSocketAddress>();
		InetSocketAddress iA = new InetSocketAddress("127.0.0.1", 9042);
		this.contactPoints.add(iA);
		setDefaultKeyspace("kieker");
		setTablePrefix("kieker");		
		setDatabasetypes();
	}
	
	/**
	 * Prepares the mapping from Java Types to Cassandra Types
	 */
	private void setDatabasetypes()
	{
		this.databaseTypes = new HashMap<Class<?>, String>();
		
		//String
		this.databaseTypes.put(String.class, "text");
		
		//int
		this.databaseTypes.put(int.class, "int");
		this.databaseTypes.put(Integer.class, "int");
		
		//long
		this.databaseTypes.put(long.class, "bigint");
		this.databaseTypes.put(Long.class, "bigint");
		
		//float
		this.databaseTypes.put(float.class, "float");
		this.databaseTypes.put(Float.class, "float");
		
		//double
		this.databaseTypes.put(double.class, "double");
		this.databaseTypes.put(Double.class, "double");
		
		//short
		this.databaseTypes.put(short.class, "int");
		this.databaseTypes.put(Short.class, "int");
		
		//boolean
		this.databaseTypes.put(boolean.class, "boolean");
		this.databaseTypes.put(Boolean.class, "boolean");
		
		//char
		this.databaseTypes.put(char.class, "varchar");
	}	
	
	/**
	 * Establishes a connection to the database
	 * @return
	 */
	private boolean connect()
	{
		boolean result = false;
		
		try
		{
			/**
			PoolingOptions poolingOpts = new PoolingOptions();
			poolingOpts.setCoreConnectionsPerHost(HostDistance.REMOTE, 2);
			poolingOpts.setMaxConnectionsPerHost(HostDistance.REMOTE, 200);
			**/
			closeSession();
			close();
			
			this.cluster = Cluster.builder().addContactPointsWithPorts(this.contactPoints)
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)	
					.withMaxSchemaAgreementWaitSeconds(60)
					.build();
			
			this.session = this.cluster.connect(getDefaultKeyspace());
			result = true;
		}
		catch(Exception exc)
		{
			result = false;
			LOG.error("Opening Connection to Database failed", exc);
		}
		
		return result;
	}
	
	/**
	 * Closes the cluster
	 */
	private void close()
	{
		if (this.cluster != null && !this.cluster.isClosed())
		{		
			this.cluster.close();		
		}
	}
	
	/**
	 * Closes the Session
	 */
	private void closeSession()
	{
		if (this.session != null && !this.session.isClosed())
		{
			this.session.close();	
		}
	}
	
	/**
	 * Executes the given statement. Returns a ResultSet if the call was successfull
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	private ResultSet execute(BoundStatement statement) throws Exception
	{
		return execute(statement, this.session);		
	}
	
	/**
	 * Executes the given statement asynchronous. Returns a ResultSetFuture if the call was succesfull
	 * @param statement
	 * @param ignoreFuture ignore Future if return is not important
	 * @return
	 * @throws Exception
	 */
	private ResultSetFuture executeAsync(BoundStatement statement, boolean ignoreFuture) throws Exception
	{
		return executeAsync(statement, this.session, ignoreFuture);
	}
	
	/**
	 * Executes the given statement asynchronous. Returns a ResultSetFuture if the call was succesfull
	 * @param statement
	 * @param session
	 * @param ignoreFuture ignore Future if return is not important
	 * @return
	 * @throws Exception
	 */
	private ResultSetFuture executeAsync(BoundStatement statement, Session session, boolean ignoreFuture) throws Exception
	{
		if (!this.init)
		{
			throw new Exception("Connection not established; please establish a Connection first");
		}
		
		ResultSetFuture rs = null;
		try
		{
			rs = session.executeAsync(statement);			
			if (ignoreFuture)
			{
				rs.cancel(true);
			}
		}
		catch(Exception exc)
		{
			LOG.error("Error executing Statement", exc);
		}
		
		return rs;
	}
	
	/**
	 * Executes the given statement. Returns a ResultSet if the call was successfull
	 * @param statement
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private ResultSet execute(BoundStatement statement, Session session) throws Exception
	{
		if (!this.init)
		{
			throw new Exception("Connection not established; please establish a Connection first");
		}
		
		ResultSet rs = null;
		try
		{
			rs = session.execute(statement);
		}
		catch (Exception exc)
		{
			LOG.error("Error executing Statement ", exc);
		}
		
		return rs;
	}

	/**
	 * Returns a BoundStatement from the given String. Uses the default Session to the keyspace.
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	private BoundStatement getBoundStatement(String statement) throws Exception
	{
		return getBoundStatement(statement, this.session);		
	}
	
	/**
	 * Returns a BoundStatement from the given String. Uses the given Session.
	 * @param statement
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private BoundStatement getBoundStatement(String statement, Session session) throws Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Statement: " + statement);
		}
		
		if (!this.init)
		{
			throw new Exception("Connection not established; please establish a Connection first");
		}
		
		if (this.preparedStatements == null)
		{
			this.preparedStatements = new ArrayList<>();
		}
		
		PreparedStatement ps = null;
		
		for (PreparedStatement cached : this.preparedStatements)
		{
			if (cached.getQueryString().equals(statement))
			{
				ps = cached;
			}
		}
		
		if (ps == null)
		{
			ps = session.prepare(statement);
			this.preparedStatements.add(ps);
		}
				
		return new BoundStatement(ps);		
	}
	
	/**
	 * Sets the param in the BoundStatement
	 * @param bs
	 * @param value
	 * @param index
	 */
	private void setParam(BoundStatement bs, Object value, int index)
	{
		if (value instanceof String)
		{
			bs.setString(index, (String) value);
		}
		else if (value instanceof Integer)
		{
			bs.setInt(index, (Integer) value);
		}
		else if (value instanceof Long)
		{
			bs.setLong(index, (Long) value);
		}
		else if (value instanceof Float)
		{
			bs.setFloat(index, (Float) value);
		}
		else if (value instanceof Double)
		{
			bs.setDouble(index, (Double) value);
		}
		else if (value instanceof Short)
		{
			bs.setShort(index, (Short) value);
		}
		else if (value instanceof Boolean)
		{
			bs.setBool(index, (Boolean) value);
		}
	}
	
	/** public **/
	
	/**
	 * initializes the database connection
	 * @param keyspace
	 * @param contactpoints
	 * @param tablePrefix
	 * @param dropTables
	 * @throws Exception
	 */
	public void initDatabase(String keyspace, String[] contactpoints, String tablePrefix, boolean dropTables) throws Exception
	{
		if (keyspace != null && !keyspace.isEmpty())
		{
			setDefaultKeyspace(keyspace);
		}
		
		if (contactpoints != null && contactpoints.length > 0)
		{		
			this.contactPoints.clear();
			
			for (int i = 0; i < contactpoints.length; i++)
			{
				String[] array = contactpoints[i].split(":");
				List<String> list = Arrays.asList(array);
				
				String host = this.cassandraDefaultHost;
				String port = this.cassandraDefaultPort;
				
				int index = 1;
				for(String s : list)
				{
					if (index == 1)
					{
						host = s;
					}
					else if (index == 2)
					{
						port = s;
					}
					index++;
				}
				
				InetSocketAddress iA = new InetSocketAddress(host, Integer.parseInt(port));
				this.contactPoints.add(iA);
			}		
		}
		
		if (tablePrefix != null && !tablePrefix.isEmpty())
		{			
			setTablePrefix(tablePrefix);			
		}
		
		setDropTables(dropTables);
		
		this.init = connect();
	}

	/**
	 * Creates the index Table
	 * @throws Exception
	 */
	public void createIndexTable() throws Exception
	{
		boolean tableExists = false;
		
		if (!this.init)
		{	
			throw new Exception("Connection not established; please establish a Connection first");
		}
		
		if (getDropTables())
		{
			String dropStatement = "DROP TABLE " + getTablePrefix();
			
			BoundStatement bs = getBoundStatement(dropStatement);
			if (execute(bs) == null)
			{
				LOG.warn("Dropping table " + getTablePrefix() + " failed; Maybe table does not exist");
			}
		}
		else
		{
			try
			{
				String selectStatement = "SELECT * FROM " + getTablePrefix();
				BoundStatement bs = getBoundStatement(selectStatement);
				ResultSet rs = execute(bs);
				if (rs != null)
				{
					tableExists = true;
				}
			}
			catch(Exception exc)
			{
				LOG.warn("Looking for table " + getTablePrefix() + " failed; Maybe table does not exist");
			}
			
		}		
		
		
		if (!tableExists)
		{			
			String createStatement = "CREATE TABLE " + getTablePrefix() + "  ( "+
				 						"tablename text, " +
				 						"classname text, " +
				 						"PRIMARY KEY (tablename) " +
				 						")";
			BoundStatement bs = getBoundStatement(createStatement);						
			if (execute(bs)  == null)
			{
				LOG.error("Creating index table " + getTablePrefix() + " failed!");
				throw new Exception("Creating index table " + getTablePrefix() + " failed!");
			}
		}
	}
	
	/**
	 * Closes all open connections to the database
	 */
	public void disconnect()
	{
		closeSession();
		close();
	}
	
	/**
	 * Creates a table in the keyspace with the given parameters
	 * @param className
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public String createTable(String className, Class<?>... columns) throws Exception
	{
		boolean tableExists	 = false;
		String tablename	 = getTablePrefix() + "_" + className;
		
		if (!this.init)
		{
			throw new Exception("Connection not established; please establish a Connection first");
		}
		
		if (getDropTables())
		{
			String dropStatement = "DROP TABLE " + tablename;
			
			BoundStatement bs = getBoundStatement(dropStatement);
			if (execute(bs) == null)
			{
				LOG.warn("Dropping table " + tablename + " failed; Maybe table does not exist");
			}
		}
		else
		{
			try
			{
				String selectStatement = "SELECT * FROM " + tablename;
				BoundStatement bs = getBoundStatement(selectStatement);
				ResultSet rs = execute(bs);
				if (rs != null)
				{
					tableExists = true;
				}
			}
			catch(Exception exc)
			{
				LOG.warn("Looking for table " + tablename + " failed; Maybe table does not exist");
			}
			
		}	
		
		if (!tableExists)
		{
			StringBuilder createTable = new StringBuilder();
			//currentString CREATE TABLE $classname (
			createTable.append("CREATE TABLE ").append(tablename).append(" (");
			//currentString CREATE TABLE $classname ( id bigint,
			createTable.append("benchmark_id ").append(this.databaseTypes.get(String.class));
			//currentString CREATE TABLE $classname ( id bigint, timestamp bigint
			createTable.append(", ").append("timestamp ").append(this.databaseTypes.get(long.class));
			
			int i = 1;
			for (Class<?> c : columns)
			{
				createTable.append(", c").append(i++).append(" ");
				String databaseType = this.databaseTypes.get(c);
				
				if (databaseType != null)
				{
					createTable.append(databaseType);
				}
				else
				{
					throw new Exception("Type '" + c.getSimpleName() + "' not supported");
				}
				
			}
			createTable.append(", PRIMARY KEY (benchmark_id, timestamp)) ");
			
			BoundStatement bs = getBoundStatement(createTable.toString());
			if(execute(bs) == null)
			{
				LOG.error("Creating table " + getTablePrefix() + " failed!");
				throw new Exception("Creating table " + getTablePrefix() + " failed!");
			}
			else
			{
				String addIndex = "INSERT INTO " + getTablePrefix() + " (tablename, classname) VALUES('" + tablename + "','" + className + "')";
				BoundStatement index = getBoundStatement(addIndex);
				if(execute(index) == null)
				{
					LOG.error("Adding table " + getTablePrefix() + " to index failed!");
					throw new Exception("Adding table " + getTablePrefix() + " to index failed!");
				}				
			}
		}
		
		return tablename;
	}
	
	/**
	 * Inserts the given statement into the database
	 * @param statement
	 * @param values
	 * @throws Exception
	 */
	public void insert(String statement, Collection<Object> values) throws Exception
	{
		BoundStatement bs = getBoundStatement(statement);
		
		int i = 0;
		for(Object value : values)
		{
			setParam(bs, value, i);
			i++;
		}
		
		if(execute(bs) == null)
		{			
			throw new Exception("Error inserting monitoring data");
		}
	}
	
	/**
	 * Inserts the given statement into the database asynchronous
	 * @param statement
	 * @param values
	 * @param ignoreFuture
	 * @throws Exception
	 */
	public void insertAsync(String statement, Collection<Object> values, boolean ignoreFuture) throws Exception
	{
		BoundStatement bs = getBoundStatement(statement);
		
		int i = 0;
		for(Object value : values)
		{
			setParam(bs, value, i);
			i++;
		}
		
		if (executeAsync(bs, ignoreFuture) == null && !ignoreFuture)
		{			
			throw new Exception("Error inserting monitoring data");			
		}
	}
	
	/**
	 * Returns the highest found RecordId. If no record or matching table is found; 0 is returned.
	 * @return
	 * @throws Exception
	 */
	public int getLastRecordId() throws Exception
	{
		if(!this.init)
		{
			throw new Exception("Connection not established; please establish a Connection first");
		}
		
		List<String> tables = new ArrayList<String>();
		int highestId = 0;
		
		// get all tables who belong to the current prefix 
		Session sysSession = this.cluster.connect("system_schema");
		String statement = "select table_name from tables where keyspace_name ='" + getDefaultKeyspace() + "'";
		String searchKey = getTablePrefix() + "_";
		BoundStatement bs = getBoundStatement(statement, sysSession);
		ResultSet rs = execute(bs, sysSession);
		if(rs != null)
		{
			for(Row row : rs)
			{
				String table = row.getString("table_name");
				
				if (table.contains(searchKey))
				{
					tables.add(table);
				}
			}
		}
		sysSession.close();
		
		if (!tables.isEmpty())
		{
			String stmt = "select max(id) as id from ";
			
			
			for(String s : tables)
			{
				String tableLookup = stmt + s;
				BoundStatement tableStmt = getBoundStatement(tableLookup);
				ResultSet result = execute(tableStmt);
				for (Row row : result)
				{
					int id = row.getInt("id");
					
					if (id > highestId)
					{
						highestId = id;
					}
				}
			}
		}
		
		return highestId;
	}


}
