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

package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

/**
 * Tests whether instrumented execution with constructorExecution creates the expected records.
 *
 * @author DaGeRe
 *
 */
public class TestConstructorExecution {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestConstructorExecution.class);

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecution.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		TestConstructorExecution.checkConstructorResult(lines);
	}

	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecution.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestOperationExecutionException");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		TestConstructorExecution.checkThrowingConstructorResult(lines);
	}

	public static void checkConstructorResult(final List<String> lines) {
		LOGGER.debug("Lines: {}", lines);
		final String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
		final String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", secondSignature);
		MatcherAssert.assertThat(lines.get(3), Matchers.not(Matchers.containsString("java.lang.IllegalAccessError")));
	}

	public static void checkThrowingConstructorResult(final List<String> lines) {
		LOGGER.debug("Lines: {}", lines);
		final String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>(int)", firstSignature);
		final String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>(int)", secondSignature);
		MatcherAssert.assertThat(lines.get(3), Matchers.not(Matchers.containsString("java.lang.IllegalAccessError")));
	}
}
