package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

public class TestConstructorCall {
	static final int CALL_SIGNATURE_INDEX = 7;

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCall.xml"));
		
		System.out.println(temporaryFile.getAbsolutePath());
		
		File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		System.out.println(lines);
		String firstSignature = lines.get(2).split(";")[CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
	}
	
	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCall.xml"));
		
		System.out.println(temporaryFile.getAbsolutePath());
		
		File logFolder = Util.runTestcase(temporaryFile, "TestOperationExecutionException");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		System.out.println(lines);
		String firstSignature = lines.get(2).split(";")[CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>(int)", firstSignature);
	}
	
	@Test
	public void testStaticExecution() throws IOException, InterruptedException {
		File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCall_static.xml"));
		
		System.out.println(temporaryFile.getAbsolutePath());
		
		File logFolder = Util.runTestcase(temporaryFile, "TestStaticOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		System.out.println(lines);
		String firstSignature = lines.get(2).split(";")[CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
	}
}
