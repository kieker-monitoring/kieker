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
package kieker.analysis.architecture.adaptation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.architecture.adaptation.events.AbstractTcpControlEvent;
import kieker.analysis.architecture.adaptation.events.TcpActivationControlEvent;

/**
 * Testing whether the dummy control prove controller stores information correctly.
 * This test is not very useful.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class DummyProbeControllerTest {

	private static final String IP = "1.2.3.4";
	private static final int PORT = 1234;
	private static final String CONTROL_HOST = "test-host";
	private static final String PATTERN = "*";
	private static final Map<String, List<String>> PARAMETERS = new HashMap<>();

	@Test
	public void testUpdateProbeParameter() {
		final DummyProbeController controller = new DummyProbeController();

		try {
			controller.updateProbeParameter(IP, PORT, CONTROL_HOST, PATTERN, PARAMETERS);
			final AbstractTcpControlEvent event = new TcpActivationControlEvent(IP, PORT, CONTROL_HOST, "*", 0);
			controller.controlProbe(event);
		} catch (final RemoteControlFailedException e) {
			Assert.fail("Remote connection failed. Cannot happen with dummy controller.");
		}
		Assert.fail("Not yet implemented");
	}

	@Test
	public void testActivateMonitoredPattern() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public void testActivateParameterMonitoredPattern() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public void testDeactivateMonitoredPattern() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public void testIsKnownHost() {
		Assert.fail("Not yet implemented");
	}

}
