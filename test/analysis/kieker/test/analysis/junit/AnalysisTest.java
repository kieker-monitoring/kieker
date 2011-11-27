package kieker.test.analysis.junit;

import kieker.test.analysis.junit.plugin.GeneralPluginTest;
import kieker.test.analysis.junit.plugin.SilentCountingRecordConsumerTest;
import kieker.test.analysis.junit.plugin.port.PortTest;
import kieker.test.analysis.junit.reader.namedRecordPipe.TestPipeReader;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite is just used to run all available analysis test with one
 * call. It should call all tests within <i>test/analysis</i>.
 * 
 * @author Nils Christian Ehmke
 */
@RunWith(Suite.class)
@SuiteClasses({
	PortTest.class,
	TestPipeReader.class,
	GeneralPluginTest.class,
	SilentCountingRecordConsumerTest.class })
public class AnalysisTest {
}
