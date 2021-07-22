/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests whether instrumented execution with OperationExecutionInstrumentation
 * creates the expected records. Since this tests uses an individual method
 * order, an individual test project is used
 * 
 * @author DaGeRe
 *
 */
public class TestOperationExecutionInstrumentation {

	private static final String OPERATION_EXECUTION_PROJECT = "example-operationexecutionrecord";

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		final File logFolder = Util.runTestcase(OPERATION_EXECUTION_PROJECT, "TestSimpleOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		final String staticSignature = lines.get(1).split(";")[2];
		Assert.assertEquals("public static void net.example.Instrumentable.staticMethod()", staticSignature);
		final String firstSignature = lines.get(2).split(";")[2];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		final String secondSignature = lines.get(3).split(";")[2];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", secondSignature);
		final String thirdSignature = lines.get(4).split(";")[2];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", thirdSignature);
	}

	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		final File logFolder = Util.runTestcase(OPERATION_EXECUTION_PROJECT, "TestOperationExecutionException");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		final String firstSignature = lines.get(1).split(";")[2];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		final String secondSignature = lines.get(2).split(";")[2];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", secondSignature);
		final String thirdSignature = lines.get(3).split(";")[2];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", thirdSignature);
	}
}
