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
public class TestBeforeAfterOperationEvent {
	private static final String OPERATION_BEFOREAFTER_PROJECT = "example-beforeafteroperationevent";

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestSimpleOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", firstSignature);
		String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", secondSignature);
		String thirdSignature = lines.get(4).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", thirdSignature);
		String forthSignature = lines.get(5).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", forthSignature);
	}
	
	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestOperationExecutionException");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", firstSignature);
		String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", secondSignature);
		String thirdSignature = lines.get(4).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", thirdSignature);
		String forthSignature = lines.get(5).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", forthSignature);
	}
}
