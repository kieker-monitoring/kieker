/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.probe.cxf.executions;

import java.util.List;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
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
 * @author Andre van Hoorn, Marius Loewe
 *
 * @since 1.6
 */
public abstract class AbstractTestCXFClientServerInterceptors extends AbstractKiekerTest {
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();

	/** This constant is used as the hostname of the server. */
	protected static final String SERVER_HOSTNAME = "srv";
	/** This constant is used as the hostname of the client. */
	protected static final String CLIENT_HOSTNAME = "client";

	private static final String SERVICE_ADDRESS_TEMPLATE = "http://localhost:909X/bookstore";

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestCXFClientServerInterceptors.class);

	/**
	 * Each instance of this class increments the port number by 1.
	 */
	private String serviceAddress;

	private String listName;
	private List<IMonitoringRecord> recordListFilledByListWriter;

	private MonitoringController clientMonitoringController;
	private MonitoringController serverMonitoringController;

	private IBookstore client;
	private Server server;

	public AbstractTestCXFClientServerInterceptors() {
		// empty constructor
	}

	@Before
	public void prepare() throws Exception {
		final int curIdx = this.getPortDigit();
		this.serviceAddress = SERVICE_ADDRESS_TEMPLATE.replace("X", Integer.toString(curIdx));
		this.listName = AbstractTestCXFClientServerInterceptors.class.getName() + "-" + curIdx;
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(this.listName);

		this.unsetKiekerThreadLocalData();
		this.clientMonitoringController = this.createMonitoringController(CLIENT_HOSTNAME);
		this.serverMonitoringController = this.createMonitoringController(SERVER_HOSTNAME);
		this.server = this.startServer();
		this.client = this.createClient();
	}

	/**
	 * Workaround to have unique port numbers among the CXF tests. A mechanism
	 * having a static integer increment by each instance did work under Eclipse,
	 * but not when executed by ant.
	 *
	 * @return A port digit.
	 */
	protected abstract int getPortDigit();

	private MonitoringController createMonitoringController(final String hostname) {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationKeys.META_DATA, "false");
		config.setProperty(ConfigurationKeys.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.listName);
		config.setProperty(ConfigurationKeys.HOST_NAME, hostname);
		return MonitoringController.createInstance(config);
	}

	private Server startServer() {
		LOGGER.info("XX: {}", this.serviceAddress);

		final JaxWsServerFactoryBean srvFactory = new JaxWsServerFactoryBean();
		srvFactory.setServiceClass(IBookstore.class);
		srvFactory.setAddress(this.serviceAddress);
		srvFactory.setServiceBean(new BookstoreImpl());

		// On the server-side, we only intercept incoming requests and outgoing
		// responses.
		srvFactory.getInInterceptors()
				.add(new OperationExecutionSOAPRequestInInterceptor(this.serverMonitoringController));
		srvFactory.getOutInterceptors()
				.add(new OperationExecutionSOAPResponseOutInterceptor(this.serverMonitoringController));
		return srvFactory.create(); // create() also starts the server
	}

	private IBookstore createClient() {
		final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		// On the client-side, we only intercept outgoing requests and incoming
		// responses.
		factory.getOutInterceptors()
				.add(new OperationExecutionSOAPRequestOutInterceptor(this.clientMonitoringController));
		factory.getInInterceptors()
				.add(new OperationExecutionSOAPResponseInInterceptor(this.clientMonitoringController));

		factory.setServiceClass(IBookstore.class);
		factory.setAddress(this.serviceAddress);
		return (IBookstore) factory.create();
	}

	/**
	 * Gives implementing classes the possibility to modify the state before the
	 * request to {@link #client} is performed.
	 */
	protected abstract void beforeRequest();

	/**
	 * Gives implementing classes the possibility to modify the state after the
	 * request to {@link #client} is performed.
	 */
	protected abstract void afterRequest();

	/**
	 * Gives implementing classes the possibility to inspect the records written by
	 * the probes.
	 *
	 * @param records
	 *            The list of written records.
	 */
	protected abstract void checkRecordList(List<IMonitoringRecord> records);

	@Test
	public final void testIt() throws InterruptedException {
		Assert.assertTrue(this.server.isStarted());
		this.beforeRequest();
		final String retVal = this.client.searchBook("any"); // we could use the return value
		Assert.assertEquals("Unexpected return value", "any", retVal);
		this.afterRequest();

		this.clientMonitoringController.terminateMonitoring();
		this.clientMonitoringController.waitForTermination(5000);
		this.serverMonitoringController.terminateMonitoring();
		this.serverMonitoringController.waitForTermination(5000);

		this.checkRecordList(this.recordListFilledByListWriter);
	}

	@After
	public void cleanup() {
		this.unsetKiekerThreadLocalData();
		this.server.destroy();
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
