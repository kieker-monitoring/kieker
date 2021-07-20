package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

/**
 * Tests whether instrumented execution with constructorCall creates the expected records
 * 
 * @author DaGeRe
 *
 */
public class TestConstructorCall {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestConstructorCall.class);

	public static final int CALL_SIGNATURE_INDEX = 7;

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCall.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		LOGGER.debug("Lines: {}", lines);
		final String firstSignature = lines.get(2).split(";")[CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
	}

	@Test
	public void testThrowingExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCall.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestOperationExecutionException");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		LOGGER.debug("Lines: {}", lines);
		final String firstSignature = lines.get(2).split(";")[CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>(int)", firstSignature);
	}

	@Test
	public void testStaticExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCall_static.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestStaticOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		LOGGER.debug("Lines: {}", lines);
		final String firstSignature = lines.get(2).split(";")[CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
	}
}
