/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.adaptiveMonitoring.cxf;

import java.util.List;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestOutInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor;
import kieker.monitoring.probe.cxf.SOAPTraceRegistry;
import kieker.monitoring.probe.cxf.executions.bookstore.BookstoreImpl;
import kieker.monitoring.probe.cxf.executions.bookstore.IBookstore;
import kieker.monitoring.test.util.NamedListWriter;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 *
 * @author Bjoern Weissenfels
 *
 * @since 1.8
 */
@Ignore // https://kieker-monitoring.atlassian.net/browse/KIEKER-1826
public class TestCXFClientServerInterceptors extends AbstractKiekerTest {
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();

	/** This constant is used as the hostname of the server. */
	protected static final String SERVER_HOSTNAME = "srv";
	/** This constant is used as the hostname of the client. */
	protected static final String CLIENT_HOSTNAME = "client";

	private static final String SERVICE_ADDRESS = "http://localhost:9093/bookstore";
	private static final int TIMEOUT_IN_MS = 1000;

	private List<IMonitoringRecord> recordListFilledByListWriter;

	private MonitoringController clientMonitoringController;
	private MonitoringController serverMonitoringController;

	private Server server;
	private IBookstore client;

	/**
	 * Default constructor.
	 */
	public TestCXFClientServerInterceptors() {
		// empty default constructor
	}

	@Before
	public void prepare() throws Exception {
		final String listName = TestCXFClientServerInterceptors.class.getName();
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(listName);

		this.unsetKiekerThreadLocalData();
		this.clientMonitoringController = this.createMonitoringController(CLIENT_HOSTNAME, listName);
		this.serverMonitoringController = this.createMonitoringController(SERVER_HOSTNAME, listName);
		this.server = this.startServer();
		this.client = this.createClient();
	}

	private MonitoringController createMonitoringController(final String hostname, final String listName) {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.ADAPTIVE_MONITORING_ENABLED, "true");
		config.setProperty(ConfigurationConstants.META_DATA, "false");
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, listName);
		config.setProperty(ConfigurationConstants.HOST_NAME, hostname);
		return MonitoringController.createInstance(config);
	}

	private Server startServer() {
		final JaxWsServerFactoryBean srvFactory = new JaxWsServerFactoryBean();
		srvFactory.setServiceClass(IBookstore.class);
		srvFactory.setAddress(TestCXFClientServerInterceptors.SERVICE_ADDRESS);
		srvFactory.setServiceBean(new BookstoreImpl());

		// On the server-side, we only intercept incoming requests and outgoing responses.
		srvFactory.getInInterceptors().add(new OperationExecutionSOAPRequestInInterceptor(this.serverMonitoringController));
		srvFactory.getOutInterceptors().add(new OperationExecutionSOAPResponseOutInterceptor(this.serverMonitoringController));
		return srvFactory.create();
	}

	private IBookstore createClient() {
		final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		// On the client-side, we only intercept outgoing requests and incoming responses.
		factory.getOutInterceptors().add(new OperationExecutionSOAPRequestOutInterceptor(this.clientMonitoringController));
		factory.getInInterceptors().add(new OperationExecutionSOAPResponseInInterceptor(this.clientMonitoringController));

		factory.setServiceClass(IBookstore.class);
		factory.setAddress(TestCXFClientServerInterceptors.SERVICE_ADDRESS);
		return factory.create(IBookstore.class);
	}

	@Test
	public final void testIt() throws InterruptedException {
		final String retVal = this.client.searchBook("any"); // produces a client and a server side record
		Assert.assertEquals("Unexpected return value", "any", retVal);
		NamedListWriter.awaitListSize(this.recordListFilledByListWriter, 2, TIMEOUT_IN_MS);
		Assert.assertThat(this.recordListFilledByListWriter.get(0), CoreMatchers.is(CoreMatchers.instanceOf(OperationExecutionRecord.class)));
		Assert.assertThat(this.recordListFilledByListWriter.get(1), CoreMatchers.is(CoreMatchers.instanceOf(OperationExecutionRecord.class)));

		final String clientPattern = "public void "
				+ "kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor.handleMessage(org.apache.cxf.message.Message)";
		final String serverPattern = "public void "
				+ "kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor.handleMessage(org.apache.cxf.binding.soap.SoapMessage)";

		MonitoringController monCtrl;

		monCtrl = this.clientMonitoringController;
		monCtrl.deactivateProbe(clientPattern);
		this.client.searchBook("any"); // only the server side monitoring is active
		NamedListWriter.awaitListSize(this.recordListFilledByListWriter, 3, TIMEOUT_IN_MS);
		Assert.assertThat(this.recordListFilledByListWriter.get(2), CoreMatchers.is(CoreMatchers.instanceOf(OperationExecutionRecord.class)));

		monCtrl = this.serverMonitoringController;
		monCtrl.deactivateProbe(serverPattern);
		this.client.searchBook("any"); // nothing should be monitored
		NamedListWriter.awaitListSize(this.recordListFilledByListWriter, 3, TIMEOUT_IN_MS);

		monCtrl = this.serverMonitoringController;
		Assert.assertThat(monCtrl.activateProbe("*"), CoreMatchers.is(true));
		this.client.searchBook("any"); // only the server side monitoring is active
		NamedListWriter.awaitListSize(this.recordListFilledByListWriter, 4, TIMEOUT_IN_MS);
		Assert.assertThat(this.recordListFilledByListWriter.get(3), CoreMatchers.is(CoreMatchers.instanceOf(OperationExecutionRecord.class)));

		monCtrl = this.clientMonitoringController;
		Assert.assertThat(monCtrl.activateProbe(clientPattern), CoreMatchers.is(true));
		this.client.searchBook("any"); // both the server side and the client side monitoring is active
		NamedListWriter.awaitListSize(this.recordListFilledByListWriter, 6, TIMEOUT_IN_MS);
		Assert.assertThat(this.recordListFilledByListWriter.get(4), CoreMatchers.is(CoreMatchers.instanceOf(OperationExecutionRecord.class)));
		Assert.assertThat(this.recordListFilledByListWriter.get(5), CoreMatchers.is(CoreMatchers.instanceOf(OperationExecutionRecord.class)));
	}

	@After
	public void cleanup() {
		this.unsetKiekerThreadLocalData();
		this.server.destroy();
		this.clientMonitoringController.terminateMonitoring();
		this.serverMonitoringController.terminateMonitoring();
	}

	private void unsetKiekerThreadLocalData() {
		CF_REGISTRY.unsetThreadLocalTraceId();
		SESSION_REGISTRY.unsetThreadLocalSessionId();
		CF_REGISTRY.unsetThreadLocalEOI();
		CF_REGISTRY.unsetThreadLocalESS();
		SOAP_REGISTRY.unsetThreadLocalOutRequestIsEntryCall();
		SOAP_REGISTRY.unsetThreadLocalOutRequestTin();
	}
}
