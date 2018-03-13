/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysisteetime.junit.plugin.reader.tcp;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Ignore;
import org.junit.Test;

import kieker.analysisteetime.plugin.reader.tcp.dualsocket.DualSocketTcpReaderStage;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.test.StageTester;

/**
 * @author Christian Wulf
 *
 * @since 1.14
 *
 */
public class TcpReaderStageTest {

	public TcpReaderStageTest() {
		// empty constructor
	}

	@Test
	@Ignore
	public void testNonStringRecordTransmission() throws Exception {
		final List<IMonitoringRecord> receivedRecords = new ArrayList<>();

		final DualSocketTcpReaderStage stage = new DualSocketTcpReaderStage();
		StageTester.test(stage).and()
				.receive(receivedRecords).from(stage.getOutputPort())
				.start();

		final List<IMonitoringRecord> sentRecords = new ArrayList<>();
		// TODO

		MatcherAssert.assertThat(receivedRecords, Is.is(IsEqual.equalTo(sentRecords)));
	}
}
