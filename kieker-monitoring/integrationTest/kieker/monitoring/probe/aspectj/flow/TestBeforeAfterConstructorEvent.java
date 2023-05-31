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

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

/**
 * Tests whether instrumented execution with beforeafterconstructorevent creates the
 * expected records. Since this tests also that a method after throwing an
 * exception is correctly recorded (throwingCallee), a individual test project
 * is used.
 * 
 * @author DaGeRe
 *
 */
public class TestBeforeAfterConstructorEvent {

	public static final int BEFOREAFTER_COLUMN_SIGNATURE = 5;
	private static final String OPERATION_BEFOREAFTER_PROJECT = "example-beforeafterconstructorevent";

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		final File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestSimpleOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		final String firstSignature = lines.get(2).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		final String secondSignature = lines.get(3).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", secondSignature);

		final String thirdSignature = lines.get(5).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", thirdSignature);
	}

	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		final File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestOperationExecutionException");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		final String firstSignature = lines.get(2).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		final String secondSignature = lines.get(3).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", secondSignature);
		final String thirdSignature = lines.get(5).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", thirdSignature);
	}
}
