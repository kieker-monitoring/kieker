/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.probe.spring.executions.jetty;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.spring.executions.jetty.UrlUtil;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore.Bookstore;
import kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore.Catalog;
import kieker.test.monitoring.util.NamedListWriter;

/**
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public class TestSpringMethodInterceptor extends AbstractKiekerTest {
	// private static final Log LOG = LogFactory.getLog(TestSpringMethodInterceptor.class);

	private static final String HOSTNAME = "SRV-W4W7E9pN";
	private static final String CTRLNAME = "MonitoringController-TestSpringMethodInterceptor";
	private static final URL BOOKSTORE_SEARCH_ANY_URL;

	private FileSystemXmlApplicationContext ctx;
	private List<IMonitoringRecord> recordListFilledByListWriter;

	static {
		try {
			BOOKSTORE_SEARCH_ANY_URL = new URL("http://localhost:9293/bookstore/search/any/");
		} catch (final MalformedURLException e) {
			throw new IllegalStateException("Should not happen because the URL is valid.", e);
		}
	}

	public TestSpringMethodInterceptor() {
		// empty default constructor
	}

	@Before
	public void startServer() throws IOException {
		final String listName = NamedListWriter.FALLBACK_LIST_NAME;
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(listName);
		// We must use System.setProperty (and not a new custom Configuration instance)
		// because the probe for the spring intercepter uses the singleton instance of the monitoring controller
		// which reads its properties by configuration file and system properties
		System.setProperty(ConfigurationKeys.META_DATA, "false");
		System.setProperty(ConfigurationKeys.HOST_NAME, HOSTNAME);
		System.setProperty(ConfigurationKeys.CONTROLLER_NAME, CTRLNAME);
		System.setProperty(ConfigurationKeys.WRITER_CLASSNAME, NamedListWriter.class.getName());
		// Doesn't work because the property does not start with kieker.monitoring:
		// System.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, listName);

		// this.monitoringController = MonitoringController.getInstance();

		// start the server
		final URL configURL = TestSpringMethodInterceptor.class
				.getResource("/kieker/test/monitoring/junit/probe/spring/executions/jetty/jetty.xml");
		this.ctx = new FileSystemXmlApplicationContext(configURL.toExternalForm());

		// Note that the Spring interceptor is configured in
		// test/monitoring/kieker/test/monitoring/junit/probe/spring/executions/jetty/webapp/WEB-INF/spring/servlet-context.xml
		// to only instrument
		// Bookstore.searchBook and Catalog.getBook
	}

	@Test
	public void testDummy() { // to avoid PMD issues
		Assert.assertNotNull(BOOKSTORE_SEARCH_ANY_URL);
	}

	// @Test
	// server returns a 503 on access
	public void ignoretestIt() throws IOException {
		// Assert.assertNotNull(this.ctx);
		Assert.assertThat(this.ctx.isRunning(), CoreMatchers.is(true));

		final IMonitoringController monitoringController = MonitoringController.getInstance();
		Assume.assumeThat(monitoringController.getName(), CoreMatchers.is(CTRLNAME));

		for (int i = 0; i < 5; i++) {
			UrlUtil.ping(BOOKSTORE_SEARCH_ANY_URL);
		}

		this.checkRecordList(this.recordListFilledByListWriter);
	}

	/**
	 * Performs some basic tests on the received records.
	 *
	 * @param records
	 */
	private void checkRecordList(final List<IMonitoringRecord> records) {
		Assert.assertFalse("No records in List", records.isEmpty());

		// Note that the Spring interceptor is configured in
		// test/monitoring/kieker/test/monitoring/junit/probe/spring/executions/jetty/webapp/WEB-INF/spring/servlet-context.xml
		// to only instrument
		// Bookstore.searchBook and Catalog.getBook
		for (final IMonitoringRecord record : records) {
			final OperationExecutionRecord opRec = (OperationExecutionRecord) record;

			Assert.assertEquals("Unexpected hostname", HOSTNAME, opRec.getHostname());
			switch (opRec.getEoi()) {
			case 0:
				this.assertSignatureIncludesString(opRec.getOperationSignature(), Bookstore.class.getName());
				Assert.assertEquals("Unexpected ess", 0, opRec.getEss());
				break;
			case 1: // fall through to case 2
			case 2:
				this.assertSignatureIncludesString(opRec.getOperationSignature(), Catalog.class.getName());
				Assert.assertEquals("Unexpected ess", 1, opRec.getEss());
				break;
			default:
				Assert.fail("Record with unexpected eoi" + opRec);
				break;
			}
		}
	}

	private void assertSignatureIncludesString(final String signatureString, final String stringIncluded) {
		final boolean included = signatureString.contains(stringIncluded);
		Assert.assertTrue(
				"Expected string '" + stringIncluded + "' not included in signature '" + signatureString + "'",
				included);
	}

	@After
	public void cleanup() {
		this.ctx.destroy();
		System.clearProperty(ConfigurationKeys.META_DATA);
		System.clearProperty(ConfigurationKeys.CONTROLLER_NAME);
		System.clearProperty(ConfigurationKeys.WRITER_CLASSNAME);
		System.clearProperty(ConfigurationKeys.HOST_NAME);
	}

}
