/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.sampler.oshi;

import org.junit.Before;
import org.junit.Test;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.oshi.samplers.CPUsDetailedPercSampler;

/**
 * @since 1.14
 *
 */

public class OshiCPUsDetailedPercSamplerTest {

	private IMonitoringController monitoringController;

	@Before
	public void setUp() throws Exception {
		this.monitoringController = MonitoringController.getInstance();
	}

	@Test
	public void test() {
		final CPUsDetailedPercSampler c = OshiSamplerFactory.INSTANCE.createSensorCPUsDetailedPerc();
		for (int i = 0; i < 20; i++) {
			System.out.println("Iteration: " + i);
			c.sample(this.monitoringController);
			try {
				Thread.sleep(1100);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
