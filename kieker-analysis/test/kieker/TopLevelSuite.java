package kieker;

import kieker.analysis.architecture.dependency.*;
import kieker.analysis.architecture.recovery.AbstractSourceModelAssemblerTest;
import kieker.analysis.architecture.recovery.OperationAndCallGeneratorStageTest;
import kieker.analysis.architecture.recovery.assembler.OperationTypeModelAssemblerTest;
import kieker.analysis.architecture.recovery.signature.JavaComponentSignatureExtractorTest;
import kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractorTest;
import kieker.analysis.architecture.trace.SessionReconstructionFilterTest;
import kieker.analysis.architecture.trace.TraceEventRecords2ExecutionAndMessageTraceStageTest;
import kieker.analysis.architecture.trace.TraceIdFilterTest;
import kieker.analysis.architecture.trace.execution.ExecutionRecordTransformationStageTest;
import kieker.analysis.architecture.trace.flow.EventRecordTraceReconstructionStageTest;
import kieker.analysis.architecture.trace.flow.ThreadEvent2TraceEventStageTest;
import kieker.analysis.architecture.trace.flow.TraceRecordsTraceReconstructionStageTest;
import kieker.analysis.architecture.trace.reconstruction.TraceReconstructionFilterTest;
import kieker.analysis.behavior.CreateEntryLevelEventStageTest;
import kieker.analysis.behavior.EntryCallSequenceStageTest;
import kieker.analysis.behavior.UserSessionToBehaviorModelTransformationTest;
import kieker.analysis.behavior.acceptance.matcher.AcceptanceModeConverterTest;
import kieker.analysis.behavior.clustering.UserBehaviorCostFunctionTest;
import kieker.analysis.generic.*;
import kieker.analysis.generic.graph.clustering.*;
import kieker.analysis.generic.graph.mtree.MTreeTest;
import kieker.analysis.generic.sink.EquivalenceClassWriterTest;
import kieker.analysis.generic.source.NetworkAccessHandlerTest;
import kieker.analysis.generic.source.namedpipe.PipeReaderThreadTest;
import kieker.analysis.generic.time.CurrentTimeEventGeneratorFilterTest;
import kieker.analysis.generic.time.TimestampFilterTest;
import kieker.analysis.plugin.trace.EventRecordTraceCounterTest;
import kieker.analysis.util.HostnameRepositoryTest;
import kieker.analysis.util.RunningMedianTest;
import kieker.test.analysis.junit.plugin.TestGlobalConfiguration;
import kieker.test.analysis.junit.plugin.TestPlugin;
import kieker.test.analysis.junit.plugin.TestPluginConfigurationRetention;
import kieker.test.analysis.junit.plugin.TestPluginShutdown;
import kieker.test.analysis.junit.plugin.filter.flow.TestEventRecordTraceReconstructionFilter;
import kieker.test.analysis.junit.plugin.filter.forward.TestCountingFilter;
import kieker.test.analysis.junit.plugin.filter.forward.TestStringBufferFilter;
import kieker.test.analysis.junit.plugin.filter.forward.TestTeeFilter;
import kieker.test.analysis.junit.plugin.filter.record.TestMonitoringThroughputFilter;
import kieker.test.analysis.junit.plugin.filter.record.TestRealtimeRecordDelayFilterAccelerationFaster;
import kieker.test.analysis.junit.plugin.filter.record.TestRealtimeRecordDelayFilterAccelerationSlowdown;
import kieker.test.analysis.junit.plugin.filter.record.TestRealtimeRecordDelayFilterNoAcceleration;
import kieker.test.analysis.junit.plugin.filter.select.TestTimestampFilter;
import kieker.test.analysis.junit.plugin.filter.trace.TestTraceIdFilter;
import kieker.test.analysis.junit.plugin.reader.TestNoInputPortsForReader;
import kieker.test.analysis.junit.plugin.reader.filesystem.DirectoryScannerStageIntegrationTest;
import kieker.test.analysis.junit.plugin.reader.filesystem.TestLegacyExecutionRecordReader;
import kieker.test.analysis.junit.plugin.reader.newio.deserializer.BinaryDeserializerTest;
import kieker.test.analysis.junit.plugin.reader.timer.TimeReaderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        TimeReaderTest.class,  // 28.038
        TestRealtimeRecordDelayFilterNoAcceleration.class,   // 20.018
        TestRealtimeRecordDelayFilterAccelerationSlowdown.class,  //  20.017
        TestRealtimeRecordDelayFilterAccelerationFaster.class,  //  15.013
        EventRecordTraceReconstructionStageTest.class,  //  1.604
        MTreeGeneratorTest.class, //   1.142
        TestPluginConfigurationRetention.class,  //  1.117
        PipeReaderThreadTest.class, //  1.016
        MTreeTest.class, //   0.594
        AssemblyLevelComponentDependencyGraphBuilderFactoryTest.class,  //   0.511
        OperationAndCallGeneratorStageTest.class,  //  0.414
        UserSessionToBehaviorModelTransformationTest.class,  //  0.109
        TraceEventRecords2ExecutionAndMessageTraceStageTest.class,  //  0.103
        OpticsStageTest.class, //   0.08
        DirectoryScannerStageIntegrationTest.class,  //  0.076
        SessionReconstructionFilterTest.class, //   0.072
        TraceIdFilterTest.class,  //  0.072
        CreateEntryLevelEventStageTest.class,  //  0.043
        TimestampFilterTest.class,  //  0.04
        TestEventRecordTraceReconstructionFilter.class,  //  0.04
        TestGlobalConfiguration.class,  //  0.039
        AbstractSourceModelAssemblerTest.class,  //  0.037
        TraceReconstructionFilterTest.class,  //  0.036
        BinaryDeserializerTest.class,   // 0.036
        CurrentTimeEventGeneratorFilterTest.class, //   0.031
        TestPluginShutdown.class,  //  0.031
        EntryCallSequenceStageTest.class,   // 0.028
        TeeFilterTest.class,   // 0.028
        GraphEditDistanceTest.class,   // 0.027
        TestPlugin.class,  //  0.027
        DataCollectorStageTest.class,   // 0.026
        DynamicEventDispatcherTest.class,   // 0.025
        TestTimestampFilter.class,   // 0.022
        MTreeGeneratorStageTest.class,  //  0.02
        TraceRecordsTraceReconstructionStageTest.class,  //  0.016
        OperationTypeModelAssemblerTest.class,   // 0.015
        CountingFilterTest.class,   // 0.015
        EquivalenceClassWriterTest.class,    //0.014
        TestTeeFilter.class,    //0.014
        TestNoInputPortsForReader.class,   // 0.012
        RunningMedianTest.class,  // 0.011
        TestMonitoringThroughputFilter.class,  // 0.009
        TestCountingFilter.class,   // 0.008
        JavaOperationSignatureExtractorTest.class,  // 0.007
        TestTraceIdFilter.class,   // 0.007
        ThreadEvent2TraceEventStageTest.class,   // 0.006
        UserBehaviorCostFunctionTest.class,   // 0.006
        BasicCostFunctionTest.class,  // 0.006
        MedoidGeneratorTest.class,  //  0.006
        MonitoringThroughputStageTest.class,   // 0.006
        TypeFilterTest.class,   // 0.006
        ExecutionRecordTransformationStageTest.class, //   0.005
        NaiveMedoidGeneratorTest.class,   // 0.005
        TypeFilterTest.class,  // 0.005
        OPTICSTest.class,   // 0.004
        NetworkAccessHandlerTest.class,   // 0.004
        EventRecordTraceCounterTest.class,   // 0.004
        AssemblyLevelOperationDependencyGraphBuilderFactoryTest.class,   // 0.003
        JavaComponentSignatureExtractorTest.class,   // 0.003
        AcceptanceModeConverterTest.class,  //  0.003
        HostnameRepositoryTest.class,   // 0.003
        DeploymentLevelContextDependencyGraphBuilderFactoryTest.class,    //0.002
        DeploymentLevelOperationDependencyGraphBuilderFactoryTest.class,  //  0.002
        TypeLevelComponentDependencyGraphBuilderFactoryTest.class,  //  0.002
        TypeLevelOperationDependencyGraphBuilderFactoryTest.class,  //  0.002
        DeploymentLevelComponentDependencyGraphBuilderFactoryTest.class,   // 0.001
        TrimmedAlgorithmTest.class,  //  0.001
        ExtractDBScanClustersStageTest.class,   // 0.0
        TestStringBufferFilter.class,  //  0.0
        TestLegacyExecutionRecordReader.class,    //0.0
})
public class TopLevelSuite {
    /*static class InSuiteOnly {
        @Ignore
        public static class SuiteProbeControllerTest extends ProbeControllerTest {}
    }*/
}