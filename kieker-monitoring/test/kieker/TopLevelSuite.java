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

        ProbeControllerTest.class, // - 175
        TestSpringMethodInterceptorInterceptorIsNotEntryPoint.class, // - 169
        TestCXFClientServerInterceptors.class, // - 166
        TestSpringMethodInterceptorInterceptorIsEntryPoint.class, // - 164
        TestProbeController.class, // - 163
        TestPeriodicSampling.class, // - 163
        TestCXFClientServerInterceptorsNoSessionRegisteredBefore.class, // - 162
        TestCXFClientServerInterceptorsSessionRegisteredBefore.class, // - 160
        TestControllerConstruction.class, // - 148
        TestCPUsDetailedPercSampler.class, // - 145
        TestClassLoadingSampler.class, // - 138
        TestGCSampler.class, // - 138
        TestMemorySampler.class, // - 138
        TestThreadsStatusSampler.class, // - 138
        TestUptimeSampler.class, // - 138
        TestChunkingCollector.class, // - 137
        TestCompilationSampler.class, // - 136
        TestMemSwapUsageSampler.class, // - 135
        TestCPUsCombinedPercSampler.class, // - 134
        TestMonitoringControllerRecordsPassedInMonitoringStates.class, // - 113
        TestPipeWriter.class, // - 111
        TestAutoSetLoggingTimestamp.class, // - 111
        WriterControllerTest.class, // - 35
        BinaryFileWriterTest.class, // - 33
        GenericBinaryFileWriterTest.class, // - 33
        AsciiFileWriterTest.class, // - 31
        GenericTextFileWriterTest.class, // - 31
        TcpRecordReaderTest.class, // - 24
        TestPatternParser.class, // - 20
        SingleSocketTcpWriterTest.class, // - 14
        SystemNanoTimerTest.class, // - 10
        SystemMilliTimerTest.class, // - 10
        TestConfigurationFactoryMethods.class, // - 10
        MonitoringWriterThreadTest.class, // - 9
        DeflateCompressionFilterTest.class, // - 8
        NoneCompressionFilterTest.class, // - 8
        XZCompressionFilterTest.class, //- 8
        GZipCompressionFilterTest.class, // - 8
        ZipCompressionFilterTest.class, // - 8
        BinaryLogStreamHandlerTest.class, // - 8
        TextLogStreamHandlerTest.class, // - 7
        TCPControllerTest.class, // - 2
        TextLogStreamHandlerTest.class, // - 0
        InfluxDBWriterFailingTest.class, //- 0
        SocketChannelTest.class, // - 0
        TestJMXInterface.class, // - 0
        TestMonitoringControllerStateTransitions.class, // - 0
        TestSpringMethodInterceptor.class, // - 0
})
public class TopLevelSuite {
    /*static class InSuiteOnly {
        @Ignore
        public static class SuiteProbeControllerTest extends ProbeControllerTest {}
    }*/
}

