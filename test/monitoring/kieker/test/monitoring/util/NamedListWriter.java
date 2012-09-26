/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class NamedListWriter extends AbstractMonitoringWriter {
	private static final Log LOG = LogFactory.getLog(NamedListWriter.class);

	private static final Map<String, List<IMonitoringRecord>> NAMED_LISTS = new HashMap<String, List<IMonitoringRecord>>();

	public static final String CONFIG_PROPERTY_NAME_LIST_NAME = NamedListWriter.class.getName() + ".listName";

	/**
	 * Returns the list with the given name, which is newly created in case it doesn't exist, yet.
	 * 
	 * @param name
	 * @return
	 */
	public static final List<IMonitoringRecord> createNamedList(final String name) {
		synchronized (NAMED_LISTS) {
			List<IMonitoringRecord> list = NAMED_LISTS.get(name);
			if (list == null) {
				list = new ArrayList<IMonitoringRecord>();
				NAMED_LISTS.put(name, list);
			}
			return list;
		}
	}

	/**
	 * Returns the list with the given name or null in case no list with this name exists.
	 * 
	 * @param name
	 * @return
	 */
	public static final List<IMonitoringRecord> getNamedList(final String name) {
		synchronized (NAMED_LISTS) {
			return NAMED_LISTS.get(name);
		}
	}

	private final String name;
	private final List<IMonitoringRecord> myNamedList;

	public NamedListWriter(final Configuration configuration) {
		super(configuration);
		this.name = configuration.getStringProperty(CONFIG_PROPERTY_NAME_LIST_NAME);
		if (this.name == null) {
			LOG.error("No name given as property");
		}
		this.myNamedList = NamedListWriter.createNamedList(this.name);
	}

	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		synchronized (this.myNamedList) {
			return this.myNamedList.add(record);
		}
	}

	public void terminate() {}

	@Override
	protected void init() throws Exception {}
}
