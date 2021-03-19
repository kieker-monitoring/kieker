package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.example.Instrumentable;

public class TestSimpleOperationExecution {

	@Test
	public void testRegularExecution() throws IOException {
		Instrumentable instrumentable = new Instrumentable();
		instrumentable.callee();
	}
}
