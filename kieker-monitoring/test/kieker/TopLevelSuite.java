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
/*
@RunWith(Suite.class)
@Suite.SuiteClasses({

        TestSpringMethodInterceptorInterceptorIsEntryPoint.class,
        TestSpringMethodInterceptorInterceptorIsNotEntryPoint.class,
        TestSpringMethodInterceptor.class,
        ProbeControllerTest.class,
        TestControllerConstruction.class,
        kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean.TestCompilationSampler.class,
        kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean.TestClassLoadingSampler.class,
        kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean.TestMemorySampler.class,
        kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean.TestThreadsStatusSampler.class,
        kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean.TestGCSampler.class,
        kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean.TestUptimeSampler.class,
        TestProbeController.class,
        TestChunkingCollector.class,
        TestMemSwapUsageSampler.class,
        TestCPUsDetailedPercSampler.class,
        TestCPUsCombinedPercSampler.class,
        TestPeriodicSampling.class,
        TestMonitoringControllerRecordsPassedInMonitoringStates.class,
        TestAutoSetLoggingTimestamp.class,
        TestMonitoringControllerStateTransitions.class,
        TestPipeWriter.class,
        GenericBinaryFileWriterTest.class,
        BinaryFileWriterTest.class,
        GenericTextFileWriterTest.class,
        AsciiFileWriterTest.class,
        WriterControllerTest.class,
        BinaryLogStreamHandlerTest.class,
        SingleSocketTcpWriterTest.class,
        TcpRecordReaderTest.class,
        TextLogStreamHandlerTest.class,
        TestConfigurationFactoryMethods.class,
        TestPatternParser.class,
        SystemMilliTimerTest.class,
        SystemNanoTimerTest.class,
        MonitoringWriterThreadTest.class,
        ZipCompressionFilterTest.class,
        XZCompressionFilterTest.class,
        DeflateCompressionFilterTest.class,
        NoneCompressionFilterTest.class,
        GZipCompressionFilterTest.class,
        TCPControllerTest.class,
        kieker.test.monitoring.junit.probe.adaptiveMonitoring.cxf.TestCXFClientServerInterceptors.class,
        kieker.test.monitoring.junit.probe.cxf.executions.TestCXFClientServerInterceptorsNoSessionRegisteredBefore.class,
        kieker.test.monitoring.junit.probe.cxf.executions.TestCXFClientServerInterceptorsSessionRegisteredBefore.class,
        TestSpringMethodInterceptor.class,
        TestJMXInterface.class,
        SocketChannelTest.class,
        InfluxDBWriterFailingTest.class
})
public class TopLevelSuite {
    /*static class InSuiteOnly {
        @Ignore
        public static class SuiteProbeControllerTest extends ProbeControllerTest {}
    }*/
//}

