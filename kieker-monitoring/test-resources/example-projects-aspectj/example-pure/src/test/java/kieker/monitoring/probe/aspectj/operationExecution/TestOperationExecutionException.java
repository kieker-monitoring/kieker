package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.example.Instrumentable;

public class TestOperationExecutionException {

	@Test
	public void testExceptionExecution() throws IOException {
		try {
			Instrumentable instrumentable = new Instrumentable(15);
			instrumentable.throwingCallee();
		} catch (RuntimeException e) {
			// Ignore, just for testing
		}
	}
}
