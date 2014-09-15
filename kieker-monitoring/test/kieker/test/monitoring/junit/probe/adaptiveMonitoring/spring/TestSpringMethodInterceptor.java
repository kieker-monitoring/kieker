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

package kieker.test.monitoring.junit.probe.adaptiveMonitoring.spring;

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
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedListWriter;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.8
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

	/**
	 * Default constructor.
	 */
	public TestSpringMethodInterceptor() {
		// empty default constructor
	}

	@Before
	public void startServer() throws IOException, URISyntaxException {
		this.tmpFolder.create();
		final String listName = NamedListWriter.FALLBACK_LIST_NAME;
		this.recordListFilledByListWriter = NamedListWriter.createNamedList(listName);
		System.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
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
		final IMonitoringController monitoringController = MonitoringController.getInstance();
		Assume.assumeTrue(CTRLNAME.equals(monitoringController.getName()));
		Assert.assertNotNull(this.ctx);

		final String getBookPattern = "public kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore.Book "
				+ "kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore.Catalog.getBook(boolean)";
		final String searchBookPattern = "public kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore.Book "
				+ "kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore.Bookstore.searchBook(java.lang.String)";

		Assert.assertEquals("Unexpected size of records. Should be 0, found " + this.recordListFilledByListWriter.size(), 0,
				this.recordListFilledByListWriter.size());
		this.search();
		Assert.assertEquals("Unexpected size of records. Should be 3, found " + this.recordListFilledByListWriter.size(), 3,
				this.recordListFilledByListWriter.size());

		monitoringController.deactivateProbe(getBookPattern);
		this.search();
		Assert.assertEquals("Unexpected size of records. Should be 4, found " + this.recordListFilledByListWriter.size(), 4,
				this.recordListFilledByListWriter.size());

		monitoringController.deactivateProbe(searchBookPattern);
		this.search();
		Assert.assertEquals("Unexpected size of records. Should be 4, found " + this.recordListFilledByListWriter.size(), 4,
				this.recordListFilledByListWriter.size());

		monitoringController.activateProbe(getBookPattern);
		this.search();
		Assert.assertEquals("Unexpected size of records. Should be 6, found " + this.recordListFilledByListWriter.size(), 6,
				this.recordListFilledByListWriter.size());

		monitoringController.activateProbe(searchBookPattern);
		this.search();
		Assert.assertEquals("Unexpected size of records. Should be 9, found " + this.recordListFilledByListWriter.size(), 9,
				this.recordListFilledByListWriter.size());
	}

	private void search() throws IOException {
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

	@After
	public void cleanup() {
		if (this.ctx != null) {
			this.ctx.close();
		}
		this.tmpFolder.delete();
		System.clearProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED);
		System.clearProperty(ConfigurationFactory.METADATA);
		System.clearProperty(ConfigurationFactory.CONTROLLER_NAME);
		System.clearProperty(ConfigurationFactory.WRITER_CLASSNAME);
		System.clearProperty(ConfigurationFactory.HOST_NAME);
	}
}
