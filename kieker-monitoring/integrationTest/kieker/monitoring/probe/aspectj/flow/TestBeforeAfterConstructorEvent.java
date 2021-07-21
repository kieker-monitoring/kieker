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
 * is used
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

		/*
		 * TODO Currently, the ApplicationTraceMetadata are added twice
		 * (https://kieker-monitoring.atlassian.net/browse/KIEKER-1865) Therefore, this
		 * tests for line 5; after this bug is fixed, it should test for line 4
		 */
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
