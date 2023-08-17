package kieker;

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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
        TestPeriodicSampling.class,
        TestJMXInterface.class,
        TestProbeController.class,
        TestControllerConstruction.class,
        TestAutoSetLoggingTimestamp.class,
        TestMonitoringControllerStateTransitions.class,
        TestMonitoringControllerRecordsPassedInMonitoringStates.class,
        TestConfigurationFactoryMethods.class,
        TestPatternParser.class,
        TestCXFClientServerInterceptorsNoSessionRegisteredBefore.class,
        TestCXFClientServerInterceptorsSessionRegisteredBefore.class,
        TestSpringMethodInterceptorInterceptorIsEntryPoint.class,
        TestCXFClientServerInterceptors.class,
        TestGCSampler.class,
        TestSpringMethodInterceptor.class,
        TestSpringMethodInterceptorInterceptorIsNotEntryPoint.class
})
@RunWith(Suite.class)
public class TopLevelSuite {
}