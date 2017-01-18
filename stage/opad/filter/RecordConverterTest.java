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
package teetime.stage.opad.filter;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static teetime.framework.test.StageTester.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.record.NamedDoubleRecord;

/** @author Arne Jan Salveter */
public class RecordConverterTest {

	private RecordConverter recordConverter;
	private OperationExecutionRecord input1;
	private OperationExecutionRecord input2;
	private OperationExecutionRecord input3;
	private OperationExecutionRecord input4;

	private NamedDoubleRecord output1;
	private NamedDoubleRecord output2;
	private NamedDoubleRecord output3;
	private List<OperationExecutionRecord> inputElements;
	private List<NamedDoubleRecord> resultsNdrOutputport;

	@Before
	public void initializeRecordConverterAndInputsOutputs() {
		recordConverter = new RecordConverter();
		resultsNdrOutputport = new LinkedList<NamedDoubleRecord>();
		long timeOne = 1;

		input1 = new OperationExecutionRecord("Signature-1", "TestID-1", 100, 50, 100, "Host-1", 1, 1);
		input2 = new OperationExecutionRecord("Signature-2", "TestID-2", 101, 1, 20, "Host-2", 1, 1);
		input3 = new OperationExecutionRecord("Signature-3", "TestID-3", 104, 0, 0, "Host-3", 1, 1);
		input4 = new OperationExecutionRecord("Signature-4", "TestID-4", 100, 99, 1, "Host-4", 1, 1);
		inputElements = Arrays.asList(input1, input2, input3, input4);

		for (OperationExecutionRecord oer : inputElements) {
			oer.setLoggingTimestamp(timeOne);
		}

		output1 = new NamedDoubleRecord("Host-1:Signature-1", timeOne, 50);
		output2 = new NamedDoubleRecord("Host-2:Signature-2", timeOne, 19);
		output3 = new NamedDoubleRecord("Host-3:Signature-3", timeOne, 0);
	}

	@Test
	public void theOutputPortNdrShouldForwardConvertedElement() {
		test(recordConverter)
				.and().send(input1).to(recordConverter.getInputPort())
				.and().receive(resultsNdrOutputport).from(recordConverter.getOutputPort())
				.start();
		assertThat(resultsNdrOutputport, contains(output1));
	}

	@Test
	public void theOutputPortNdrShouldForwardZeroElements() {
		test(recordConverter)
				.and().send(input4).to(recordConverter.getInputPort())
				.and().receive(resultsNdrOutputport).from(recordConverter.getOutputPort())
				.start();
		assertThat(this.resultsNdrOutputport, is(empty()));
	}

	@Test
	public void theOutputPortNdrShouldForwardThreeConvertedElements() {
		test(recordConverter)
				.and().send(inputElements).to(recordConverter.getInputPort())
				.and().receive(resultsNdrOutputport).from(recordConverter.getOutputPort())
				.start();
		assertThat(this.resultsNdrOutputport, contains(output1, output2, output3));
	}
}
