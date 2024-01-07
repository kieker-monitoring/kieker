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

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

/**
 * Tests whether instrumented execution with constructorExecutionObjectInterface
 * creates the expected records.
 *
 * @author DaGeRe
 *
 */
@Ignore // Ignore test, since there is some problem with aop configuration
public class TestConstructorExecutionObjectInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestConstructorExecutionObjectInterface.class);

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecutionObjectInterface.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);

		TestConstructorExecutionObject.checkConstructorResult(lines);
	}

	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecutionObjectInterface.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestOperationExecutionException");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		TestConstructorExecutionObject.checkThrowingConstructorResult(lines);
	}
}
