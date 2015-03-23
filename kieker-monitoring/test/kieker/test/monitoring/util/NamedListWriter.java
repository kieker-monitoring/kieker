/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public class NamedListWriter extends AbstractMonitoringWriter {

	/** The name of the configuration determining the name of the list used by this writer. */
	public static final String CONFIG_PROPERTY_NAME_LIST_NAME = NamedListWriter.class.getName() + ".listName";
	/** The default used list name if no name has been specified. */
	public static final String FALLBACK_LIST_NAME = "VbDt0E7Aqv";

	private static final Log LOG = LogFactory.getLog(NamedListWriter.class);

	private static final Map<String, List<IMonitoringRecord>> NAMED_LISTS = new HashMap<String, List<IMonitoringRecord>>(); // NOPMD (synchronized)

	private final List<IMonitoringRecord> myNamedList;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration used to configure this component.
	 */
	public NamedListWriter(final Configuration configuration) {
		super(configuration);
		String name = configuration.getStringProperty(CONFIG_PROPERTY_NAME_LIST_NAME);
		if (name.length() == 0) {
			name = FALLBACK_LIST_NAME;
			LOG.warn("No list name given as property. Falling back to the list with name '" + name + "'");
		}
		this.myNamedList = NamedListWriter.createNamedList(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		synchronized (this.myNamedList) {
			return this.myNamedList.add(record);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate() {
		// no need to do anything
	}

	@Override
	protected void init() throws Exception {
		// no need to do anything
	}

	/**
	 * Returns the list with the given name, which is newly created in case it doesn't exist, yet.
	 * 
	 * @param name
	 *            The name to search for.
	 * 
	 * @return Either the corresponding list if the name exists or a new list otherwise.
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
	 *            The name to search for.
	 * 
	 * @return Either the corresponding list if the name exists or null otherwise.
	 */
	public static final List<IMonitoringRecord> getNamedList(final String name) {
		synchronized (NAMED_LISTS) {
			return NAMED_LISTS.get(name);
		}
	}
}
