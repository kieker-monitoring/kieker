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

package kieker.test.monitoring.junit.probe.adaptiveMonitoring.cxf;

import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestOutInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor;
import kieker.monitoring.probe.cxf.SOAPTraceRegistry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.junit.probe.cxf.executions.bookstore.BookstoreImpl;
import kieker.test.monitoring.junit.probe.cxf.executions.bookstore.IBookstore;
import kieker.test.monitoring.util.NamedListWriter;

/**
 * 
 * @author Bjoern Weissenfels
 * 
 * @since 1.8
 */
public class TestCXFClientServerInterceptors extends AbstractKiekerTest {
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();

	/** This constant is used as the hostname of the server. */
	protected static final String SERVER_HOSTNAME = "srv";
	/** This constant is used as the hostname of the client. */
	protected static final String CLIENT_HOSTNAME = "client";

	private static final Log LOG = LogFactory.getLog(TestCXFClientServerInterceptors.class);

	private volatile String serviceAddress = "http://localhost:9093/bookstore";

	private volatile String listName;
	private volatile List<IMonitoringRecord> recordListFilledByListWriter;

	private final JaxWsServerFactoryBean srvFactory = new JaxWsServerFactoryBean();

	private volatile IMonitoringController clientMonitoringController;
	private volatile IMonitoringController serverMonitoringController;

	private volatile IBookstore client;

	/**
	 * Default constructor.
	 */
	public TestCXFClientServerInterceptors() {
		// empty default constructor
	}

	@Before
	public void prepare() throws Exception {
		this.listName = TestCXFClientServerInterceptors.class.getName();
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(this.listName);

		this.unsetKiekerThreadLocalData();
		this.clientMonitoringController = this.createMonitoringController(CLIENT_HOSTNAME);
		this.serverMonitoringController = this.createMonitoringController(SERVER_HOSTNAME);
		this.startServer();
		this.createClient();
	}

	private IMonitoringController createMonitoringController(final String hostname) {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		config.setProperty(ConfigurationFactory.METADATA, "false");
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.listName);
		config.setProperty(ConfigurationFactory.HOST_NAME, hostname);
		return MonitoringController.createInstance(config);
	}

	private void startServer() {
		LOG.info("XX: " + this.serviceAddress);

		final BookstoreImpl implementor = new BookstoreImpl();
		this.srvFactory.setServiceClass(IBookstore.class);
		this.srvFactory.setAddress(this.serviceAddress);
		this.srvFactory.setServiceBean(implementor);

		// On the server-side, we only intercept incoming requests and outgoing responses.
		this.srvFactory.getInInterceptors().add(new OperationExecutionSOAPRequestInInterceptor(this.serverMonitoringController));
		this.srvFactory.getOutInterceptors().add(new OperationExecutionSOAPResponseOutInterceptor(this.serverMonitoringController));
		this.srvFactory.create();
	}

	private void createClient() {
		final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		// On the client-side, we only intercept outgoing requests and incoming responses.
		factory.getOutInterceptors().add(new OperationExecutionSOAPRequestOutInterceptor(this.clientMonitoringController));
		factory.getInInterceptors().add(new OperationExecutionSOAPResponseInInterceptor(this.clientMonitoringController));

		factory.setServiceClass(IBookstore.class);
		factory.setAddress(this.serviceAddress);
		this.client = (IBookstore) factory.create();
	}

	@Test
	public final void testIt() throws InterruptedException {
		final String retVal = this.client.searchBook("any"); // produces a client and a server side record
		Assert.assertEquals("Unexpected return value", "any", retVal);
		Assert.assertEquals("Unexpected return value", 2, this.recordListFilledByListWriter.size());

		final String clientPattern = "public void "
				+ "kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor.handleMessage(org.apache.cxf.message.Message)";
		final String serverPattern = "public void "
				+ "kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor.handleMessage(org.apache.cxf.binding.soap.SoapMessage)";

		this.clientMonitoringController.deactivateProbe(clientPattern);
		this.client.searchBook("any"); // only the server side record should be monitored
		Assert.assertEquals("Unexpected return value", 3, this.recordListFilledByListWriter.size());

		this.serverMonitoringController.deactivateProbe(serverPattern);
		this.client.searchBook("any"); // nothing should be monitored
		Assert.assertEquals("Unexpected return value", 3, this.recordListFilledByListWriter.size());

		this.serverMonitoringController.activateProbe("*"); // this should also activate monitoring
		this.client.searchBook("any"); // only the server side record should be monitored
		Assert.assertEquals("Unexpected return value", 4, this.recordListFilledByListWriter.size());

		this.clientMonitoringController.activateProbe(clientPattern);
		this.client.searchBook("any");
		Assert.assertEquals("Unexpected return value", 6, this.recordListFilledByListWriter.size());
	}

	@After
	public void cleanup() {
		this.unsetKiekerThreadLocalData();
		this.srvFactory.destroy();
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
