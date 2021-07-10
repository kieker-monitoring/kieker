package kieker.monitoring.probe.aspectj.flow;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.probe.aspectj.operationExecution.Util;

/**
 * TODO Does currently not work (no monitoring logs are produced), added for completeness
 */
public class TestConstructorCallObject {
	@Test
	public void testBasicExecution() throws IOException, InterruptedException {
		File temporaryFile = Util.createTemporaryProject(new File(Util.EXAMPLE_PROJECT_FOLDER, "aop_constructorCallObject.xml"));
		
		System.out.println(temporaryFile.getAbsolutePath());
		
		File logFolder = Util.runTestcase(temporaryFile, "TestSimpleOperationExecution");

		List<String> lines = Util.getLatestLogRecord(logFolder);
		System.out.println(lines);
		String firstSignature = lines.get(2).split(";")[TestConstructorCall.CALL_SIGNATURE_INDEX];
		Assert.assertEquals("public example.kieker.Instrumentable.<init>()", firstSignature);
	}
}
