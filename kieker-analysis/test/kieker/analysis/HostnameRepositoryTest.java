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

package kieker.analysis;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.util.HostnameRepository;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class HostnameRepositoryTest {

	private static final long EXAMPLE_TRACE_ID = 1234;
	private static final String EXAMPLE_HOSTNAME = "Host123";

	private HostnameRepository hostnameRepository;

	public HostnameRepositoryTest() {
		// default empty constructor
	}

	@Before
	public void setUp() throws Exception {
		this.hostnameRepository = new HostnameRepository();
	}

	@After
	public void tearDown() throws Exception {
		this.hostnameRepository = null; // NOPMD (resetting to null intended)
	}

	/**
	 * Test method for {@link kieker.analysis.util.HostnameRepository#addEntry(long, java.lang.String)}
	 * and {@link kieker.analysis.util.HostnameRepository#getHostname(long)}.
	 */
	@Test
	public void testAddEntryAndGetHostname() {
		this.hostnameRepository.addEntry(EXAMPLE_TRACE_ID, EXAMPLE_HOSTNAME);

		final String hostname = this.hostnameRepository.getHostname(EXAMPLE_TRACE_ID);

		Assert.assertEquals(EXAMPLE_HOSTNAME, hostname);
	}

	/**
	 * Test method for {@link kieker.analysis.util.HostnameRepository#inc(long)} and
	 * {@link kieker.analysis.util.HostnameRepository#dec(long)}.
	 */
	@Test
	public void testAutomaticallyDelete() {
		this.hostnameRepository.addEntry(EXAMPLE_TRACE_ID, EXAMPLE_HOSTNAME);
		this.hostnameRepository.inc(EXAMPLE_TRACE_ID);
		this.hostnameRepository.inc(EXAMPLE_TRACE_ID);
		this.hostnameRepository.dec(EXAMPLE_TRACE_ID);
		this.hostnameRepository.inc(EXAMPLE_TRACE_ID);
		this.hostnameRepository.dec(EXAMPLE_TRACE_ID);
		this.hostnameRepository.dec(EXAMPLE_TRACE_ID);

		final String hostname = this.hostnameRepository.getHostname(EXAMPLE_TRACE_ID);

		Assert.assertNull(hostname);
	}

}
