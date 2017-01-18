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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static teetime.framework.test.StageTester.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import teetime.util.ThreadThrowableContainer;

import kieker.tools.opad.record.ExtendedStorableDetectionResult;
import kieker.tools.opad.record.StorableDetectionResult;

/**
 * @author Arne Jan Salveter
 */
public class StorableDetectionResultExtenderTest {

	Collection<ThreadThrowableContainer> exceptions;
	private StorableDetectionResultExtender sDRExtender;
	private StorableDetectionResult input;
	private StorableDetectionResult output;

	private List<StorableDetectionResult> results;

	@Before
	public void initializeAnomalyDetectionFilterAndInputs() {
		sDRExtender = new StorableDetectionResultExtender(6);
		input = new StorableDetectionResult("Test1", 1, 1, 1, 1);
		output = new ExtendedStorableDetectionResult("Test1", 1, 1, 1, 1, 6);
		results = new ArrayList<StorableDetectionResult>();
	}

	@Test
	public void theOutputPortShouldConvertElements() {
		test(sDRExtender)
				.and().send(input).to(sDRExtender.getInputPort())
				.and().receive(results).from(sDRExtender.getOutputPort())
				.start();
		assertThat(results, is(not(empty())));
		assertThat(results, contains(output));
	}
}
