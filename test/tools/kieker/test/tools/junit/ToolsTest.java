package kieker.test.tools.junit;

import kieker.test.tools.junit.currentTimeEventGeneratorFilter.TestCurrentTimeEventGenerator;
import kieker.test.tools.junit.traceAnalysis.plugins.TestTimestampFilter;
import kieker.test.tools.junit.traceAnalysis.plugins.TestTraceIdFilter;
import kieker.test.tools.junit.traceAnalysis.plugins.TestTraceReconstructionFilter;
import kieker.test.tools.junit.traceAnalysis.systemModel.TestExecutionTraceBookstore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite is just used to run all available tools test with one
 * call. It should call all tests within <i>test/tools</i>.
 * 
 * @author Nils Christian Ehmke
 */
@RunWith(Suite.class)
@SuiteClasses({
	TestCurrentTimeEventGenerator.class,
	TestTimestampFilter.class,
	TestTraceIdFilter.class,
	TestTraceReconstructionFilter.class,
	TestExecutionTraceBookstore.class
})
public class ToolsTest {

}
