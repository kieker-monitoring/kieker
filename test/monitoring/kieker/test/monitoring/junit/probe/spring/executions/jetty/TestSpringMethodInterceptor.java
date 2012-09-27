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

package kieker.test.monitoring.junit.probe.spring.executions.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestSpringMethodInterceptor extends AbstractKiekerTest {
	// private static final Log LOG = LogFactory.getLog(TestSpringMethodInterceptor.class);

	private volatile FileSystemXmlApplicationContext ctx;

	@Before
	public void setup() {
		// start the server
		this.ctx = new FileSystemXmlApplicationContext("test/monitoring/kieker/test/monitoring/junit/probe/spring/executions/jetty/jetty.xml");
	}

	/*
	 * TODO: We need to refine the test to really test not only whether the configuration works but also
	 * to test what is being logged. This should be implemented similar to the tests in
	 * 'kieker.test.monitoring.junit.probe.cxf.executions'. One challenge is that the
	 * Spring interceptors use the singleton instance of the MonitoringController.
	 */
	@Test
	public void testIt() throws IOException {
		for (int i = 0; i < 5; i++) {
			final URL url = new URL("http://localhost:9293/bookstore/search/any/");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
				final String result = in.readLine();
				Assert.assertNotNull("Result is null", result);
				Assert.assertTrue("Result is empty", result.length() > 0);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}
	}

	@After
	public void cleanup() {
		this.ctx.destroy(); // TODO: is this shutting down the server?
	}
}
