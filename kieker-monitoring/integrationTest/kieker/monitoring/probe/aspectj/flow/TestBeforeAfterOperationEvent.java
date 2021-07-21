package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

/**
 * Tests whether instrumented execution with beforeafteroperationevent creates the
 * expected records. Since this tests uses an individual method order, an individual test project
 * is used
 *
 * @author DaGeRe
 *
 */
public class TestBeforeAfterOperationEvent { // NOCS
	private static final String OPERATION_BEFOREAFTER_PROJECT = "example-beforeafteroperationevent";

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		final File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestSimpleOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		final String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", firstSignature);
		final String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", secondSignature);
		final String thirdSignature = lines.get(4).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", thirdSignature);
		final String forthSignature = lines.get(5).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", forthSignature);
	}

	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		final File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestOperationExecutionException");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		final String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", firstSignature);
		final String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", secondSignature);
		final String thirdSignature = lines.get(4).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", thirdSignature);
		final String forthSignature = lines.get(5).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", forthSignature);
	}
}
