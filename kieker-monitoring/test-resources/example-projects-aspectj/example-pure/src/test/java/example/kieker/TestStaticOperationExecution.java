package example.kieker;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestStaticOperationExecution {

	@Test
	public void testRegularExecution() throws IOException {
		Instrumentable.staticMethod();
	}
}
