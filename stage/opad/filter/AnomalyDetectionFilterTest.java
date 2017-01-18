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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import teetime.util.ThreadThrowableContainer;

import kieker.tools.opad.record.StorableDetectionResult;

/**
 * @author Arne Jan Salveter
 */
public class AnomalyDetectionFilterTest {

	Collection<ThreadThrowableContainer> exceptions;
	private AnomalyDetectionFilter adf;
	private StorableDetectionResult input1;
	private StorableDetectionResult input2;
	private StorableDetectionResult input3;
	private StorableDetectionResult input4;
	private List<StorableDetectionResult> inputElements;
	private List<StorableDetectionResult> resultsPortAbnormal;
	private List<StorableDetectionResult> resultsPortRegular;

	@Before
	public void initializeAnomalyDetectionFilterAndInputs() {
		adf = new AnomalyDetectionFilter(6);
		input1 = new StorableDetectionResult("Test1", 1, 1, 1, 1);
		input2 = new StorableDetectionResult("Test2", 2, 1, 1, 1);
		input3 = new StorableDetectionResult("Test3", 6, 1, 1, 1);
		input4 = new StorableDetectionResult("Test4", 7, 1, 1, 1);
		inputElements = Arrays.asList(input1, input2, input3, input4);
		resultsPortAbnormal = new ArrayList<StorableDetectionResult>();
		resultsPortRegular = new ArrayList<StorableDetectionResult>();
	}

	@Test
	public void theOutputPortNormalShouldForwardElements() {
		test(adf).and().send(input1, input2).to(adf.getInputPort())
				.and().receive(resultsPortRegular).from(adf.getOutputPortRegular())
				.and().receive(resultsPortAbnormal).from(adf.getOutputPortAbnormal())
				.start();
		assertThat(resultsPortRegular, contains(input1, input2));
		assertThat(resultsPortAbnormal, is(empty()));
	}

	@Test
	public void theOutputPortAnnormalShouldForwardElements() {
		test(adf).and().send(input3, input4).to(adf.getInputPort())
				.and().receive(resultsPortRegular).from(adf.getOutputPortRegular())
				.and().receive(resultsPortAbnormal).from(adf.getOutputPortAbnormal())
				.start();
		assertThat(resultsPortRegular, is(empty()));
		assertThat(resultsPortAbnormal, contains(input3, input4));
	}

	@Test
	public void bothOutputPortsShouldForwardElements() {
		test(adf).and().send(inputElements).to(adf.getInputPort())
				.and().receive(resultsPortRegular).from(adf.getOutputPortRegular())
				.and().receive(resultsPortAbnormal).from(adf.getOutputPortAbnormal())
				.start();
		assertThat(resultsPortRegular, contains(input1, input2));
		assertThat(resultsPortAbnormal, contains(input3, input4));
	}
}
