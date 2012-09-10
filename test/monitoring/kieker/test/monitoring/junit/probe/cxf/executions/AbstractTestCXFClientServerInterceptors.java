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

package kieker.test.monitoring.junit.probe.cxf.executions;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;
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

import kieker.test.monitoring.junit.probe.cxf.executions.bookstore.BookstoreImpl;
import kieker.test.monitoring.junit.probe.cxf.executions.bookstore.IBookstore;
import kieker.test.monitoring.util.NamedListWriter;

/**
 * 
 * @author Andre van Hoorn, Marius Loewe
 * 
 */
public abstract class AbstractTestCXFClientServerInterceptors {
	private static final AtomicInteger NEXT_IDX = new AtomicInteger(0);

	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();

	protected static final String SERVER_HOSTNAME = "srv";
	protected static final String CLIENT_HOSTNAME = "client";

	private static final String SERVICE_ADDRESS_TEMPLATE = "http://localhost:909X/bookstore";

	private final int curIdx = NEXT_IDX.getAndIncrement();

	/**
	 * Each instance of this class increments the port number by 1
	 */
	private final String serviceAddress = SERVICE_ADDRESS_TEMPLATE.replace("X", Integer.toString(this.curIdx));

	private final String LIST_NAME = AbstractTestCXFClientServerInterceptors.class.getName() + "-" + this.curIdx;
	private final List<IMonitoringRecord> recordListFilledByListWriter = NamedListWriter.createNamedList(this.LIST_NAME);

	private final JaxWsServerFactoryBean srvFactory = new JaxWsServerFactoryBean();

	private volatile IMonitoringController clientMonitoringController;
	private volatile IMonitoringController serverMonitoringController;

	private volatile IBookstore client;

	@Before
	public void setup() throws Exception {
		this.unsetKiekerThreadLocalData();
		this.clientMonitoringController = this.createMonitoringController(CLIENT_HOSTNAME);
		this.serverMonitoringController = this.createMonitoringController(SERVER_HOSTNAME);
		this.startServer();
		this.createClient();
	}

	private IMonitoringController createMonitoringController(final String hostname) {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, NamedListWriter.class.getName());
		config.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.LIST_NAME);
		config.setProperty(ConfigurationFactory.HOST_NAME, hostname);
		return MonitoringController.createInstance(config);
	}

	private void startServer() {
		final BookstoreImpl implementor = new BookstoreImpl();
		this.srvFactory.setServiceClass(IBookstore.class);
		this.srvFactory.setAddress(this.serviceAddress);
		this.srvFactory.setServiceBean(implementor);

		/*
		 * On the server-side, we only intercept incoming requests and outgoing responses.
		 */
		this.srvFactory.getInInterceptors().add(new OperationExecutionSOAPRequestInInterceptor(this.serverMonitoringController));
		this.srvFactory.getOutInterceptors().add(new OperationExecutionSOAPResponseOutInterceptor(this.serverMonitoringController));
		this.srvFactory.create();
	}

	private void createClient() {
		final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		/*
		 * On the client-side, we only intercept outgoing requests and incoming responses.
		 */
		factory.getOutInterceptors().add(new OperationExecutionSOAPRequestOutInterceptor(this.clientMonitoringController));
		factory.getInInterceptors().add(new OperationExecutionSOAPResponseInInterceptor(this.clientMonitoringController));

		factory.setServiceClass(IBookstore.class);
		factory.setAddress(this.serviceAddress);
		this.client = (IBookstore) factory.create();
	}

	/**
	 * Gives implementing classes the possibility to modify the state before the request to {@link #client} is performed.
	 */
	protected abstract void beforeRequest();

	/**
	 * Gives implementing classes the possibility to modify the state after the request to {@link #client} is performed.
	 */
	protected abstract void afterRequest();

	/**
	 * Gives implementing classes the possibility to inspect the records written by the probes.
	 */
	protected abstract void checkRecordList(List<IMonitoringRecord> records);

	@Test
	public final void testIt() {
		this.beforeRequest();
		final String reply = this.client.searchBook("any");
		System.out.println("Server found: " + reply);
		this.afterRequest();

		this.checkRecordList(this.recordListFilledByListWriter);
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
