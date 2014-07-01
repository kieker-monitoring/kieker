/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.4
 */
public final class JMXWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = JMXWriter.class.getName() + ".";
	public static final String CONFIG_DOMAIN = PREFIX + "domain"; // NOCS (afterPREFIX)
	public static final String CONFIG_LOGNAME = PREFIX + "logname"; // NOCS (afterPREFIX)

	private static final Log LOG = LogFactory.getLog(JMXWriter.class);

	private final String configDomain;
	private final String configLogname;

	private KiekerJMXMonitoringLog kiekerJMXMonitoringLog;
	private ObjectName monitoringLogName;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public JMXWriter(final Configuration configuration) {
		super(configuration);
		this.configDomain = configuration.getStringProperty(CONFIG_DOMAIN);
		this.configLogname = configuration.getStringProperty(CONFIG_LOGNAME);
	}

	@Override
	protected void init() throws Exception {
		try {
			String domain = this.configDomain;
			if ("".equals(domain)) {
				domain = this.monitoringController.getJMXDomain();
			}
			this.monitoringLogName = new ObjectName(domain, "type", this.configLogname);
		} catch (final MalformedObjectNameException ex) {
			throw new IllegalArgumentException("The generated ObjectName is not correct! Check the following configuration values '" + CONFIG_DOMAIN
					+ "=" + this.configDomain + "' and '" + CONFIG_LOGNAME + "=" + this.configLogname + "'", ex);
		}
		this.kiekerJMXMonitoringLog = new KiekerJMXMonitoringLog(this.monitoringLogName);
		try {
			ManagementFactory.getPlatformMBeanServer().registerMBean(this.kiekerJMXMonitoringLog, this.monitoringLogName);
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("Failed to inititialize JMXWriter.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.kiekerJMXMonitoringLog.newMonitoringRecord(record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate() {
		try {
			ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.monitoringLogName);
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("Failed to terminate writer", ex);
		}
	}
}
