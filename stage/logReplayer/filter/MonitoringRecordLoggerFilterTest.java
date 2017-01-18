/**
 * Copyright (C) 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime-framework.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package teetime.stage.logReplayer.filter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static teetime.framework.test.StageTester.test;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.opad.record.NamedDoubleRecord;

/**
 *
 * @author Arne Jan Salveter
 * @since 1.6
 */
public class MonitoringRecordLoggerFilterTest {

	private MonitoringRecordLoggerFilter filter;
	private IMonitoringRecord input1;
	private IMonitoringRecord input2;
	private IMonitoringRecord input3;
	private IMonitoringRecord input4;

	private Configuration config;

	private LinkedList<IMonitoringRecord> inputElements;
	private LinkedList<IMonitoringRecord> resultsOutputPort;

	@Before
	public void initializeRecordConverterAndInputsOutputs() {
		resultsOutputPort = new LinkedList<IMonitoringRecord>();
		config = new Configuration();
		filter = new MonitoringRecordLoggerFilter(config);
		input1 = new NamedDoubleRecord("TestName1", 1, 1);
		input2 = new NamedDoubleRecord("TestName2", 2, 3);
		input3 = new NamedDoubleRecord("TestName3", 3, 50);
		input4 = new NamedDoubleRecord("TestName4", 4, 150);
		inputElements = new LinkedList<IMonitoringRecord>();
		inputElements.add(input1);
		inputElements.add(input2);
		inputElements.add(input3);
		inputElements.add(input4);
	}

	@Test
	public void theOutputPortShouldForwardFourElements() {
		test(filter)
				.and().send(inputElements).to(filter.getInputPort())
				.and().receive(resultsOutputPort).from(filter.getOutputPort())
				.start();
		assertThat(resultsOutputPort, contains(input1, input2, input3, input4));
		assertThat(filter.getMonitoringController().isMonitoringTerminated(), is(true));
	}
}
