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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * 
 * @author Armin Moebius, Sven Ulrich
 *
 */
public class CassandraSyncDbWriter extends AbstractMonitoringWriter
{	
	private static final String PREFIX				 = CassandraSyncDbWriter.class.getName() + ".";
	public static final String CONFIG_KEYSPACE		 = PREFIX + "Keyspace";
	public static final String CONFIG_CONTACTPOINTS	 = PREFIX + "Contactpoints";
	public static final String CONFIG_TABLEPREFIX	 = PREFIX + "TablePrefix";
	public static final String CONFIG_OVERWRITE		 = PREFIX + "DropTables";
	public static final String CONFIG_BENCHMARKID	 = PREFIX + "BenchmarkId";
	
	private static final Log LOG = LogFactory.getLog(CassandraSyncDbWriter.class);
	
	private ConcurrentHashMap<String,String> classes = new ConcurrentHashMap<String,String>();
	
	private CassandraDb	 database;
	private String		 benchmarkId;
	
	/**
	 * Creates a new instance of this class using the given parameter
	 * @param configuration
	 * @throws Exception
	 */
	public CassandraSyncDbWriter(Configuration configuration) throws Exception
	{
		super(configuration);
		String keyspace			 = configuration.getStringProperty(CONFIG_KEYSPACE);
		String[] contactpoints	 = configuration.getStringArrayProperty(CONFIG_CONTACTPOINTS, ";");
		String tableprefix		 = configuration.getStringProperty(CONFIG_TABLEPREFIX);
		boolean dropTables		 = configuration.getBooleanProperty(CONFIG_OVERWRITE);
		benchmarkId				 = configuration.getStringProperty(CONFIG_BENCHMARKID);
		
		this.database = new CassandraDb(keyspace, contactpoints, tableprefix, dropTables);		
		this.database.createIndexTable();		
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord record)
	{						
		Class<? extends IMonitoringRecord> recordClass = record.getClass();
		String className = recordClass.getSimpleName();
		
		if(!classes.containsKey(className))
		{		
			Class<?>[] typeArray = null;		
			try
			{
				typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
			}
			catch (MonitoringRecordException exc)
			{		
				LOG.error("Failed to get types of record", exc);				
				return false;
			}
			
			try
			{
				String tableName = this.database.createTable(className, typeArray);
				StringBuilder values = new StringBuilder();
					values.append("'" + benchmarkId + "',?");
				
				StringBuilder fields = new StringBuilder("benchmark_id,timestamp");
				
				for(int i = 1; i <= typeArray.length; i++)
				{
					values.append(",?");
					fields.append(",c" + i);
				}
				
				String statement = "INSERT INTO " + tableName + " ( " + fields.toString()  + " )  VALUES ("+ values.toString() + ")";
				this.classes.put(className, statement);
			}
			catch (Exception exc)
			{
				LOG.error("Error creating Table", exc);
				return false;
			}
		}
		
		List<Object> values = new ArrayList<Object>();
		values.add(record.getLoggingTimestamp());
		List<Object> recordVals = Arrays.asList(record.toArray());
		values.addAll(recordVals);
		
		try
		{			
			this.database.insert(this.classes.get(className), values);
		}
		catch (Exception exc)
		{
			LOG.error("Error inserting Monitoring Record", exc);
			return false;			
		}
		
		return true;
	}

	@Override
	public void terminate()
	{		
		this.database.disconnect();
	}

	@Override
	protected void init() throws Exception{/** **/}

}
