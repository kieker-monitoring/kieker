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

package kieker.test.monitoring.junit.probe.spring.executions.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;

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

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private volatile FileSystemXmlApplicationContext ctx;
	private volatile List<IMonitoringRecord> recordListFilledByListWriter;

	public TestSpringMethodInterceptor() {
		// empty default constructor
	}

	@Before
	public void startServer() throws IOException, URISyntaxException {
		this.tmpFolder.create();
		final String listName = NamedListWriter.FALLBACK_LIST_NAME;
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(listName);
		System.setProperty(ConfigurationFactory.METADATA, "false");
		System.setProperty(ConfigurationFactory.CONTROLLER_NAME, CTRLNAME);
		System.setProperty(ConfigurationFactory.WRITER_CLASSNAME, NamedListWriter.class.getName());
		// Doesn't work because property not starting with kieker.monitoring: System.setProperty(NamedListWriter.CONFIG_PROPERTY_NAME_LIST_NAME, this.listName);
		System.setProperty(ConfigurationFactory.HOST_NAME, HOSTNAME);

		// start the server
		final URL configURL = TestSpringMethodInterceptor.class.getResource("/kieker/test/monitoring/junit/probe/spring/executions/jetty/jetty.xml");
		this.ctx = new FileSystemXmlApplicationContext(configURL.toExternalForm());

		// Note that the Spring interceptor is configure in
		// test/monitoring/kieker/test/monitoring/junit/probe/spring/executions/jetty/webapp/WEB-INF/spring/servlet-context.xml to only instrument
		// Bookstore.searchBook and Catalog.getBook
	}

	@Test
	public void testIt() throws IOException {
		Assume.assumeTrue(CTRLNAME.equals(MonitoringController.getInstance().getName()));
		Assert.assertNotNull(this.ctx);
		for (int i = 0; i < 5; i++) {
			final URL url = new URL("http://localhost:9293/bookstore/search/any/");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
				// final String result = in.readLine(); // the result is currently an empty string.
			} finally {
				if (null != in) {
					in.close();
				}
			}
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
		// test/monitoring/kieker/test/monitoring/junit/probe/spring/executions/jetty/webapp/WEB-INF/spring/servlet-context.xml to only instrument
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
			}
		}
	}

	private void assertSignatureIncludesString(final String signatureString, final String stringIncluded) {
		final boolean included = signatureString.contains(stringIncluded);
		Assert.assertTrue("Expected string '" + stringIncluded + "' not included in signature '" + signatureString + "'", included);
	}

	@After
	public void cleanup() {
		if (this.ctx != null) {
			this.ctx.destroy();
		}
		this.tmpFolder.delete();
		System.clearProperty(ConfigurationFactory.METADATA);
		System.clearProperty(ConfigurationFactory.CONTROLLER_NAME);
		System.clearProperty(ConfigurationFactory.WRITER_CLASSNAME);
		System.clearProperty(ConfigurationFactory.HOST_NAME);
	}
}
