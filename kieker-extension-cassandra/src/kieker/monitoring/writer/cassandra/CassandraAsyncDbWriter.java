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

import java.util.concurrent.ConcurrentHashMap;

import kieker.common.configuration.Configuration;
import kieker.monitoring.writer.AbstractAsyncWriter;

/**
 * 
 * @author Armin Moebius, Sven Ulrich
 *
 */
public class CassandraAsyncDbWriter extends AbstractAsyncWriter
{
	private static final String PREFIX				 = CassandraAsyncDbWriter.class.getName() + ".";
	public static final String CONFIG_KEYSPACE		 = PREFIX + "Keyspace";
	public static final String CONFIG_CONTACTPOINTS	 = PREFIX + "Contactpoints";
	public static final String CONFIG_TABLEPREFIX	 = PREFIX + "TablePrefix";
	public static final String CONFIG_OVERWRITE		 = PREFIX + "DropTables";
	public static final String CONFIG_CONNECTIONS	 = PREFIX + "Connections";	
	public static final String CONFIG_BENCHMARKID	 = PREFIX + "BenchmarkId";
	
	private final String	 keyspace;
	private final String[]	 contactPoints;
	private final String	 tablePrefix;
	private final boolean	 dropTables;
	private final int		 connections;	
	private final String	 benchmarkId;
	
	protected static final ConcurrentHashMap<String, String> classes = new ConcurrentHashMap<String, String>();
	
	/**
	 * Creates a new instance of this class using the given parameter
	 * @param configuration
	 */
	public CassandraAsyncDbWriter(Configuration configuration)
	{
		super(configuration);
		
		this.keyspace		 = configuration.getStringProperty(CONFIG_KEYSPACE);
		this.contactPoints	 = configuration.getStringArrayProperty(CONFIG_CONTACTPOINTS, ";");
		this.tablePrefix	 = configuration.getStringProperty(CONFIG_TABLEPREFIX);
		this.dropTables		 = configuration.getBooleanProperty(CONFIG_OVERWRITE);
		this.connections	 = configuration.getIntProperty(CONFIG_CONNECTIONS);
		this.benchmarkId	 = configuration.getStringProperty(CONFIG_BENCHMARKID);
	}

	@Override
	public void init() throws Exception
	{
		CassandraDb database = new CassandraDb(this.keyspace, this.contactPoints, this.tablePrefix, this.dropTables);
		database.createIndexTable();
		database.disconnect();
		
		for(int i = 0; i < this.connections; i++)
		{
			CassandraWriterThread cwt = new CassandraWriterThread(super.monitoringController,
																	super.blockingQueue,																	
																	this.keyspace,
																	this.contactPoints,
																	this.tablePrefix,
																	this.dropTables,
																	this.benchmarkId
																	);
			this.addWorker(cwt);
			
			CassandraWriterThread cwtp = new CassandraWriterThread(super.monitoringController,
																	super.prioritizedBlockingQueue,																	
																	this.keyspace,
																	this.contactPoints,
																	this.tablePrefix,
																	this.dropTables,
																	this.benchmarkId
																	);
			this.addWorker(cwtp);			
		}
		
	}

}
