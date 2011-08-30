/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import javax.management.ObjectName;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public final class JMXWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(JMXWriter.class);

	private final static String PREFIX = JMXWriter.class.getName() + ".";
	public final static String CONFIG__DOMAIN = JMXWriter.PREFIX + "domain";
	public final static String CONFIG__LOGNAME = JMXWriter.PREFIX + "logname";

	private KiekerJMXMonitoringLog kiekerJMXMonitoringLog;
	private ObjectName monitoringLogName;

	public JMXWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected void init() throws Exception {
		try {
			String domain = this.configuration.getStringProperty(CONFIG__DOMAIN);
			if (domain.equals("")) {
				domain = monitoringController.getJMXDomain();
			}
			this.monitoringLogName = new ObjectName(domain, "type", this.configuration.getStringProperty(CONFIG__LOGNAME));
		} catch (final Exception ex) {
			JMXWriter.log.error("The generated ObjectName is not correct! Check the following configuration values '" + CONFIG__DOMAIN + "="
					+ this.configuration.getStringProperty(CONFIG__DOMAIN) + "' and '" + CONFIG__LOGNAME + "="
					+ this.configuration.getStringProperty(CONFIG__LOGNAME) + "'");
			throw new IllegalArgumentException("The generated ObjectName is not correct!");
		}
		this.kiekerJMXMonitoringLog = new KiekerJMXMonitoringLog(this.monitoringLogName);
		try {
			ManagementFactory.getPlatformMBeanServer().registerMBean(this.kiekerJMXMonitoringLog, this.monitoringLogName);
		} catch (final Exception ex) {
			JMXWriter.log.error("Failed to inititialize JMXWriter.");
			throw ex;
		}
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.kiekerJMXMonitoringLog.newMonitoringRecord(record);
	}

	@Override
	public void terminate() {
		try {
			ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.monitoringLogName);
		} catch (final Exception ex) {
			log.error("Failed to terminate writer", ex);
		}
	}
}
