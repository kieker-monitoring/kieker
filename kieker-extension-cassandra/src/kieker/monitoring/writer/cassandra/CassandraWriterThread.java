package kieker.monitoring.writer.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;

/**
 * 
 * @author Sven Ulrich
 *
 */
public class CassandraWriterThread extends AbstractAsyncThread
{
	private CassandraDb	database;	
	private String benchmarkId;
	
	private static final Log LOG = LogFactory.getLog(CassandraWriterThread.class);
	

	/**
	 * Creates a new instance of this class using the given parameter
	 * @param monitoringController
	 * @param writeQueue
	 * @param recordId
	 * @param keyspace
	 * @param contactpoints
	 * @param tablePrefix
	 * @param dropTables 
	 * @throws Exception
	 */
	public CassandraWriterThread(IMonitoringController monitoringController,
			BlockingQueue<IMonitoringRecord> writeQueue,
			String		 keyspace,
			String[]	 contactpoints,
			String		 tablePrefix,
			boolean		 dropTables,
			String		 benchmarkId
			) throws Exception
	{
		super(monitoringController, writeQueue);
		this.benchmarkId = benchmarkId;
		this.database	 = new CassandraDb(keyspace, contactpoints, tablePrefix, dropTables);	
	}

	@Override
	protected void consume(IMonitoringRecord monitoringRecord) throws Exception
	{
		Class<? extends IMonitoringRecord> recordClass = monitoringRecord.getClass();
		String className = recordClass.getSimpleName();
		
		synchronized (this)
		{
			if(!CassandraAsyncDbWriter.classes.containsKey(className))
			{
				Class<?>[] typeArray = null;
				try
				{
					typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
				}
				catch(MonitoringRecordException exc)
				{
					throw new Exception("Failed to get types of record", exc);
				}
				
				String tableName = this.database.createTable(className, typeArray);
				StringBuilder values = new StringBuilder();
				
				values.append("'" + this.benchmarkId + "',?");
				StringBuilder fields = new StringBuilder("benchmark_id,timestamp");
				
				for(int i = 1; i <= typeArray.length; i++)
				{
					values.append(",?");
					fields.append(",c" + i);
				}
				
				String statement = "INSERT INTO " + tableName + " ( " + fields.toString() + " ) VALUES (" + values.toString() + ")";
				CassandraAsyncDbWriter.classes.put(className, statement);
			}
		}
				
		List<Object> values = new ArrayList<Object>();
		values.add(monitoringRecord.getLoggingTimestamp());
		List<Object> recordVals = Arrays.asList(monitoringRecord.toArray());
		values.addAll(recordVals);
		
		int 	count	 = 0;
		boolean inserted = false;
		do
		{
			try
			{
				//retry if other thread has created table, but the cassandra cluster did not acknowledged change on all hosts or write timeout
				this.database.insertAsync(CassandraAsyncDbWriter.classes.get(className), values, true);
				inserted = true;
			}
			catch(Exception exc)
			{
				count++;
				Thread.sleep(1000);
				if (count == 10)
				{
					throw exc;
				}		
				else
				{
					LOG.warn("Error inserting monitoring record ", exc);
				}
			}
		}
		while(!inserted);
	}

	@Override
	protected void cleanup()
	{	
		this.database.disconnect();
	}

}
