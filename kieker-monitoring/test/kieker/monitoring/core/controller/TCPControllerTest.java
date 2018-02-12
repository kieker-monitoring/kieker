/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.controller;

import java.io.IOException;

import org.jctools.queues.MpscArrayQueue;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.DeactivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.timer.SystemNanoTimer;
import kieker.monitoring.writer.dump.DumpWriter;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;

/**
 *
 * @author Marc Adolf
 * @since 1.14
 *
 */
public class TCPControllerTest {
	private static Configuration configuration = new Configuration();
	private static int port = 9753;
	private static int timeoutInMs = 100;
	private SingleSocketTcpWriter tcpWriter;

	public TCPControllerTest() {
		super();
	}

	@BeforeClass
	public static void init() throws IOException {

		// setup for other depending parts
		configuration.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_ENABLED, true);
		configuration.setProperty(ConfigurationKeys.WRITER_CLASSNAME, DumpWriter.class.getName());
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_FQN,
				MpscArrayQueue.class.getName());
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_SIZE, "1");
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");
		// setup for TCP test
		configuration.setProperty(ConfigurationKeys.ACTIVATE_TCP_DOMAIN, "kieker.tcp");
		configuration.setProperty(ConfigurationKeys.ACTIVATE_TCP_REMOTE_PORT, port);
		configuration.setProperty(ConfigurationKeys.ACTIVATE_TCP, true);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, port);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_BUFFERSIZE, 65535);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_FLUSH, true);
		configuration.setProperty(ConfigurationKeys.ACTIVATE_TCP, true);
		configuration.setProperty(ConfigurationKeys.TIMER_CLASSNAME, SystemNanoTimer.class.getName());
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS, timeoutInMs);
		@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
		final String address = "127.0.0.1";
		TCPControllerTest.configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, address);

	}

	@Test(timeout = 30000)
	public void testActivationAndDeactivation() throws IOException, InterruptedException {
		final MonitoringController controller = MonitoringController.createInstance(TCPControllerTest.configuration);
		final String pattern = "void test.pattern()";

		this.tcpWriter = new SingleSocketTcpWriter(configuration);
		this.tcpWriter.onStarting();

		// wait for the reader to start

		Assert.assertTrue(controller.activateProbe(pattern));

		controller.deactivateProbe(pattern);
		Assert.assertFalse(controller.isProbeActivated(pattern));

		this.sendTCPEvent(new ActivationEvent(pattern));
		while (!controller.isProbeActivated(pattern)) {
			Thread.yield();
		}
		Assert.assertTrue(controller.isProbeActivated(pattern));

		this.sendTCPEvent(new DeactivationEvent(pattern));
		while (controller.isProbeActivated(pattern)) {
			Thread.yield();
		}
		Assert.assertFalse(controller.isProbeActivated(pattern));

		controller.cleanup();
		this.tcpWriter.onTerminating();
	}

	private void sendTCPEvent(final IRemoteControlEvent event) {
		this.tcpWriter.writeMonitoringRecord(event);
	}
}
