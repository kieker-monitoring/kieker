package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

public class TestBeforeAfterConstructorEvent {
	private static final String OPERATION_BEFOREAFTER_PROJECT = "example-beforeafterconstructorevent";

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestSimpleOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		String firstSignature = lines.get(2).split(";")[5];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		String secondSignature = lines.get(3).split(";")[5];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", secondSignature);
		String thirdSignature = lines.get(5).split(";")[5];
		Assert.assertEquals("public void net.example.Instrumentable.callee()", thirdSignature);
	}
	
	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		File logFolder = Util.runTestcase(OPERATION_BEFOREAFTER_PROJECT, "TestOperationExecutionException");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		String firstSignature = lines.get(2).split(";")[5];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", firstSignature);
		String secondSignature = lines.get(3).split(";")[5];
		Assert.assertEquals("public net.example.Instrumentable.<init>()", secondSignature);
		String thirdSignature = lines.get(5).split(";")[5];
		Assert.assertEquals("public void net.example.Instrumentable.throwingCallee()", thirdSignature);
	}
}
