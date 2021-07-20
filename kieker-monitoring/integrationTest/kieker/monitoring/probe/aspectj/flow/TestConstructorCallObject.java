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
 * TODO Does currently not work (no monitoring logs are produced), added for completeness
 */
public class TestConstructorCallObject {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestConstructorCallObject.class);

	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		final File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCallObject.xml"));

		LOGGER.debug("Result path: {}", temporaryFile.getAbsolutePath());

		final File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		final List<String> lines = Util.getLatestLogRecord(logFolder);
		LOGGER.debug("Lines: {}", lines);
		final String firstSignature = lines.get(2).split(";")[TestConstructorCall.CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
	}
}
