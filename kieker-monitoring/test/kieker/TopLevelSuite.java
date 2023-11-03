package kieker;

import kieker.monitoring.core.controller.ProbeControllerTest;
import kieker.monitoring.core.controller.TCPControllerTest;
import kieker.monitoring.core.controller.WriterControllerTest;
import kieker.monitoring.core.controller.tcp.TcpRecordReaderTest;
import kieker.monitoring.timer.SystemMilliTimerTest;
import kieker.monitoring.timer.SystemNanoTimerTest;
import kieker.monitoring.writer.MonitoringWriterThread;
import kieker.monitoring.writer.MonitoringWriterThreadTest;
import kieker.monitoring.writer.compression.*;
import kieker.monitoring.writer.filesystem.*;
import kieker.monitoring.writer.influxdb.InfluxDBWriterFailingTest;
import kieker.monitoring.writer.namedRecordPipe.TestPipeWriter;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriterTest;
import kieker.monitoring.writer.tcp.SocketChannelTest;
import kieker.test.monitoring.junit.core.configuration.TestConfigurationFactoryMethods;
import kieker.test.monitoring.junit.core.controller.*;
import kieker.test.monitoring.junit.core.sampler.TestPeriodicSampling;
import kieker.test.monitoring.junit.core.signaturePattern.TestPatternParser;
import kieker.test.monitoring.junit.probe.adaptiveMonitoring.cxf.TestCXFClientServerInterceptors;
import kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean.*;
import kieker.test.monitoring.junit.probe.adaptiveMonitoring.oshi.TestCPUsCombinedPercSampler;
import kieker.test.monitoring.junit.probe.adaptiveMonitoring.oshi.TestCPUsDetailedPercSampler;
import kieker.test.monitoring.junit.probe.adaptiveMonitoring.oshi.TestMemSwapUsageSampler;
import kieker.test.monitoring.junit.probe.adaptiveMonitoring.spring.TestSpringMethodInterceptor;
import kieker.test.monitoring.junit.probe.cxf.executions.TestCXFClientServerInterceptorsNoSessionRegisteredBefore;
import kieker.test.monitoring.junit.probe.cxf.executions.TestCXFClientServerInterceptorsSessionRegisteredBefore;
import kieker.test.monitoring.junit.probe.spring.executions.TestSpringMethodInterceptorInterceptorIsEntryPoint;
import kieker.test.monitoring.junit.probe.spring.executions.TestSpringMethodInterceptorInterceptorIsNotEntryPoint;
import kieker.test.monitoring.junit.writer.collector.TestChunkingCollector;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        AsciiFileWriterTest.class,   // 89.181
        GenericTextFileWriterTest.class,   // 88.257
        BinaryFileWriterTest.class,  //  62.781
        GenericBinaryFileWriterTest.class,  //  62.092
        TestPeriodicSampling.class, //   16.62
        TestCompilationSampler.class,   // 11.545
        TestClassLoadingSampler.class, //  11.543
        TestMemorySampler.class, //   11.534
        TestThreadsStatusSampler.class,  //  11.524
        TestGCSampler.class,  //  11.52
        TestUptimeSampler.class,   // 11.513
        TestProbeController.class, //  10.214
        TestCPUsCombinedPercSampler.class,  //  8.832
        TestCPUsDetailedPercSampler.class,  //  8.825
        TestMemSwapUsageSampler.class, //   7.615
        TestPatternParser.class,  //  1.862
        TcpRecordReaderTest.class,   // 1.323
        TestChunkingCollector.class,  //  1.01
        XZCompressionFilterTest.class,  //  1.006
        SingleSocketTcpWriterTest.class,   // 0.711
        ProbeControllerTest.class,  //  0.604
        TestAutoSetLoggingTimestamp.class,   // 0.063
        WriterControllerTest.class,   // 0.039
        TCPControllerTest.class,   // 0.036
        TestMonitoringControllerRecordsPassedInMonitoringStates.class,   // 0.035
        SystemMilliTimerTest.class,   // 0.025
        SystemNanoTimerTest.class,  //  0.018
        MonitoringWriterThreadTest.class,  //  0.017
        TestPipeWriter.class,  //  0.017
        TestMonitoringControllerStateTransitions.class,   // 0.017
        TestConfigurationFactoryMethods.class,  //  0.012
        TestControllerConstruction.class,  //  0.012
        TestSpringMethodInterceptorInterceptorIsEntryPoint.class,  //  0.011
        ZipCompressionFilterTest.class,  //  0.008
        DeflateCompressionFilterTest.class,  //  0.006
        GZipCompressionFilterTest.class,  //  0.006
        SocketChannelTest.class,  //  0.005
        TestSpringMethodInterceptorInterceptorIsNotEntryPoint.class,   // 0.005
        TestJMXInterface.class,   // 0.004
        NoneCompressionFilterTest.class,  //  0.002
        BinaryLogStreamHandlerTest.class,  //  0.002
        TextLogStreamHandlerTest.class,  //  0.002
        TestSpringMethodInterceptor.class,  //  0.001
        kieker.monitoring.writer.influxdb.InfluxDBWriterFailingTest.class,  //  0.0
        TestCXFClientServerInterceptors.class,    //0.0
        TestSpringMethodInterceptor.class, //   0.0
        TestCXFClientServerInterceptorsNoSessionRegisteredBefore.class,  //  0.0
        TestCXFClientServerInterceptorsSessionRegisteredBefore.class,  //  0.0
})
public class TopLevelSuite {
    /*static class InSuiteOnly {
        @Ignore
        public static class SuiteProbeControllerTest extends ProbeControllerTest {}
    }*/
}