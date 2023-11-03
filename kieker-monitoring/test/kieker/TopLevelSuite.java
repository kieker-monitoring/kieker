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

        ProbeControllerTest.class, // - 2142
        TestSpringMethodInterceptorInterceptorIsEntryPoint.class,
        TestSpringMethodInterceptorInterceptorIsNotEntryPoint.class,
        TestSpringMethodInterceptor.class,
        TestSpringMethodInterceptor.class,
        TestProbeController.class, // - 1792
        TestControllerConstruction.class, //- 1596
        TestCompilationSampler.class,    //11.545
        TestClassLoadingSampler.class,    //11.543
        TestMemorySampler.class,  //  11.534
        TestThreadsStatusSampler.class,   // 11.524
        TestGCSampler.class,   // 11.52
        TestUptimeSampler.class,    //11.513
        TestCPUsDetailedPercSampler.class, //- 1514
        TestChunkingCollector.class, //- 1468
        TestCPUsCombinedPercSampler.class, //- 1370
        TestMemSwapUsageSampler.class, //- 1366
        TestPeriodicSampling.class, //- 1150
        TestMonitoringControllerStateTransitions.class, //- 1114
        TestMonitoringControllerRecordsPassedInMonitoringStates.class, //- 1120
        TestPipeWriter.class, //- 1112
        TestAutoSetLoggingTimestamp.class, //- 1112
        GenericBinaryFileWriterTest.class, //- 588
        BinaryFileWriterTest.class, //- 580
        GenericTextFileWriterTest.class, //- 566
        AsciiFileWriterTest.class, //- 558
        TestPatternParser.class, //- 428
        WriterControllerTest.class, //- 408
        BinaryLogStreamHandlerTest.class, //- 210
        TcpRecordReaderTest.class, //- 206
        SingleSocketTcpWriterTest.class, //- 196
        TextLogStreamHandlerTest.class, //- 188
        TestConfigurationFactoryMethods.class, //- 176
        MonitoringWriterThreadTest.class, //- 112
        SystemNanoTimerTest.class, //- 94
        SystemMilliTimerTest.class, //- 92
        ZipCompressionFilterTest.class, //- 50
        XZCompressionFilterTest.class, //- 46
        DeflateCompressionFilterTest.class, //- 44
        GZipCompressionFilterTest.class, //- 44
        NoneCompressionFilterTest.class, //- 44
        TCPControllerTest.class, //- 38
        InfluxDBWriterFailingTest.class, //- 0
        SocketChannelTest.class, //- 0
        TestJMXInterface.class, //- 0
        TestCXFClientServerInterceptors.class,
        TestCXFClientServerInterceptorsNoSessionRegisteredBefore.class,
        TestCXFClientServerInterceptorsSessionRegisteredBefore.class,
})
public class TopLevelSuite {
    /*static class InSuiteOnly {
        @Ignore
        public static class SuiteProbeControllerTest extends ProbeControllerTest {}
    }*/
}