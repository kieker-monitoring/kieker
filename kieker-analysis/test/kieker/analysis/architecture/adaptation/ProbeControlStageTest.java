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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.architecture.adaptation.data.Alarms;
import kieker.analysis.architecture.adaptation.data.IErrorMessages;
import kieker.analysis.architecture.adaptation.events.AbstractTcpControlEvent;
import kieker.analysis.architecture.adaptation.events.TcpActivationControlEvent;
import kieker.analysis.architecture.adaptation.events.TcpDeactivationControlEvent;
import kieker.analysis.architecture.adaptation.events.TcpParameterControlEvent;

import teetime.framework.test.StageTester;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ProbeControlStageTest {

	private static final String IP = "1.2.3.4";
	private static final int PORT = 1234;
	private static final String HOSTNAME = "test";
	private static final String PATTERN = "*";

	private static final TcpActivationControlEvent ACTIVATION = new TcpActivationControlEvent(IP, PORT, HOSTNAME, PATTERN, 0);
	private static final TcpDeactivationControlEvent DEACTIVATION = new TcpDeactivationControlEvent(IP, PORT, HOSTNAME, PATTERN, 2);
	private static final Map<String, List<String>> MAP = new HashMap<>();

	private static final TcpParameterControlEvent PARAMETER = new TcpParameterControlEvent(IP, PORT, HOSTNAME, PATTERN, 1, MAP);

	@Test
	public void testGoodCase() {
		final List<AbstractTcpControlEvent> results = new ArrayList<>();
		final IProbeController probeController = new IProbeController() {

			@Override
			public void controlProbe(final AbstractTcpControlEvent event) throws RemoteControlFailedException {
				results.add(event);
			}
		};
		final ProbeControlStage stage = new ProbeControlStage(probeController);

		StageTester.test(stage).send(ProbeControlStageTest.ACTIVATION, ProbeControlStageTest.PARAMETER, ProbeControlStageTest.DEACTIVATION).to(stage.getInputPort())
				.start();
		Assert.assertEquals("missing events", 3, results.size());
		Assert.assertEquals("Wrong event", ACTIVATION, results.get(0));
		Assert.assertEquals("Wrong event", PARAMETER, results.get(1));
		Assert.assertEquals("Wrong event", DEACTIVATION, results.get(2));
	}

	@Test
	public void testConnectionFail() {
		final IProbeController probeController = new IProbeController() {

			@Override
			public void controlProbe(final AbstractTcpControlEvent event) throws RemoteControlFailedException {
				throw new RemoteControlFailedException("TEST ERROR");
			}
		};
		final ProbeControlStage stage = new ProbeControlStage(probeController);

		final List<IErrorMessages> alarms = new ArrayList<>();
		StageTester.test(stage).send(ProbeControlStageTest.ACTIVATION).to(stage.getInputPort())
				.receive(alarms).from(stage.getOutputPort()).start();
		Assert.assertTrue("wrong number of alarms", alarms.size() == 1);
		Assert.assertEquals("wrong message in log", Alarms.class, alarms.get(0).getClass());
	}
}
