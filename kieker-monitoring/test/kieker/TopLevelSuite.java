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
import kieker.test.monitoring.junit.probe.adaptiveMonitoring.spring.TestSpringMethodInterceptor;
import kieker.test.monitoring.junit.probe.cxf.executions.TestCXFClientServerInterceptorsNoSessionRegisteredBefore;
import kieker.test.monitoring.junit.probe.cxf.executions.TestCXFClientServerInterceptorsSessionRegisteredBefore;
import kieker.test.monitoring.junit.probe.spring.executions.TestSpringMethodInterceptorInterceptorIsEntryPoint;
import kieker.test.monitoring.junit.probe.spring.executions.TestSpringMethodInterceptorInterceptorIsNotEntryPoint;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        TopLevelSuite.SuiteProbeControllerTest.class,
        //TCPControllerTest.class,
        WriterControllerTest.class,
        SystemMilliTimerTest.class,
        SystemNanoTimerTest.class,
        DeflateCompressionFilterTest.class,
        GZipCompressionFilterTest.class,
        NoneCompressionFilterTest.class,
        ZipCompressionFilterTest.class,
        AsciiFileWriterTest.class,
        BinaryLogStreamHandlerTest.class,
        GenericBinaryFileWriterTest.class,
        TextLogStreamHandlerTest.class,
        InfluxDBWriterFailingTest.class,
        TestPipeWriter.class,
        //SingleSocketTcpWriterTest.class,
        SocketChannelTest.class,
        MonitoringWriterThreadTest.class,
        // test.monitoring
        TestJMXInterface.class,
        TestControllerConstruction.class,
        TestAutoSetLoggingTimestamp.class,
        TestMonitoringControllerStateTransitions.class,
        TestMonitoringControllerRecordsPassedInMonitoringStates.class,
        TestConfigurationFactoryMethods.class,
        TestCXFClientServerInterceptorsNoSessionRegisteredBefore.class,
        TestCXFClientServerInterceptorsSessionRegisteredBefore.class,
        TestSpringMethodInterceptorInterceptorIsNotEntryPoint.class,
        TestSpringMethodInterceptorInterceptorIsEntryPoint.class,
        TestSpringMethodInterceptor.class,
        TestCXFClientServerInterceptors.class,
        TestGCSampler.class,
        //
        XZCompressionFilterTest.class,
        TcpRecordReaderTest.class,
        TestPatternParser.class,
        TestProbeController.class,
        TestPeriodicSampling.class,
        BinaryFileWriterTest.class,
        GenericTextFileWriterTest.class,
        AsciiFileWriterTest.class
})
public class TopLevelSuite {
    //static class InSuiteOnly {
        @Ignore
        public static class SuiteProbeControllerTest extends ProbeControllerTest {}
    //}
}