package kieker.test.analysis.junit;

import kieker.test.analysis.junit.plugin.GeneralPluginTest;
import kieker.test.analysis.junit.plugin.port.PortTest;
import kieker.test.analysis.junit.reader.namedRecordPipe.TestPipeReader;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PortTest.class,
	TestPipeReader.class,
	GeneralPluginTest.class })
public class AnalysisTest {
}
