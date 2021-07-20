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
		final String firstSignature = lines.get(1).split(";")[2];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		final String secondSignature = lines.get(2).split(";")[2];
		Assert.assertEquals("public void net.example.Instrumentable.callee2(java.lang.String)", secondSignature);
		final String thirdSignature = lines.get(3).split(";")[2];
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
