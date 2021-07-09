package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

public class TestConstructorExecutionObject {
	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecutionObject.xml"));
		
		System.out.println(temporaryFile.getAbsolutePath());
		
		File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		checkConstructorResult(lines);
	}
	
	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorExecutionObject.xml"));
		
		System.out.println(temporaryFile.getAbsolutePath());
		
		File logFolder = Util.runTestcase(temporaryFile, "TestOperationExecutionException");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		checkThrowingConstructorResult(lines);
	}

	public static void checkConstructorResult(final List<String> lines) {
		System.out.println(lines);
		String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
		String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", secondSignature);
		MatcherAssert.assertThat(lines.get(3), Matchers.not(Matchers.containsString("java.lang.IllegalAccessError")));
	}
	
	public static void checkThrowingConstructorResult(final List<String> lines) {
		System.out.println(lines);
		String firstSignature = lines.get(2).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>(int)", firstSignature);
		String secondSignature = lines.get(3).split(";")[TestBeforeAfterConstructorEvent.BEFOREAFTER_COLUMN_SIGNATURE];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>(int)", secondSignature);
		MatcherAssert.assertThat(lines.get(3), Matchers.not(Matchers.containsString("java.lang.IllegalAccessError")));
	}
}
