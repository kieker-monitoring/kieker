package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

public class TestBeforeAfterConstructorEvent {
	public static final int BEFOREAFTER_COLUMN_SIGNATURE = 5;
	private static final String OPERATION_BEFOREAFTER_PROJECT = "example-beforeafterconstructorevent";

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestSimpleOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		String firstSignature = lines.get(2).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		String secondSignature = lines.get(3).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", secondSignature);

		/*
		 * TODO Currently, the ApplicationTraceMetadata are added twice
		 * (https://kieker-monitoring.atlassian.net/browse/KIEKER-1865) Therefore, this
		 * tests for line 5; after this bug is fixed, it should test for line 4
		 */
		String thirdSignature = lines.get(5).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", thirdSignature);
	}

	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestOperationExecutionException");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		String firstSignature = lines.get(2).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		String secondSignature = lines.get(3).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", secondSignature);
		String thirdSignature = lines.get(5).split(";")[BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", thirdSignature);
	}
}
