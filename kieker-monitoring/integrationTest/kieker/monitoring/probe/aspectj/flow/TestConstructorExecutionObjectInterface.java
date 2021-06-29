package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

public class TestConstructorExecutionObjectInterface {
	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		File temporaryFile = Util
				.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecutionObjectInterface.xml"));

		System.out.println(temporaryFile.getAbsolutePath());

		File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);

		TestConstructorExecutionObject.checkConstructorResult(lines);
	}
	
	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecutionObjectInterface.xml"));
		
		System.out.println(temporaryFile.getAbsolutePath());
		
		File logFolder = Util.runTestcase(temporaryFile, "TestOperationExecutionException");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		TestConstructorExecutionObject.checkThrowingConstructorResult(lines);
	}
}
