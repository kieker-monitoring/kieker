package kieker;

import kieker.analysis.architecture.dependency.*;
import kieker.analysis.architecture.recovery.AbstractSourceModelAssemblerTest;
import kieker.analysis.architecture.recovery.ModelAssemblerStageTest;
import kieker.analysis.architecture.recovery.OperationAndCallGeneratorStageTest;
import kieker.analysis.architecture.recovery.assembler.OperationTypeModelAssemblerTest;
import kieker.analysis.architecture.recovery.assembler.StorageAssemblyModelAssemblerTest;
import kieker.analysis.architecture.recovery.assembler.StorageDeploymentModelAssemblerTest;
import kieker.analysis.architecture.recovery.assembler.StorageTypeModelAssemblerTest;
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

        MTreeTest.class, // - 143
        OpticsStageTest.class, //- 101
        TestPluginConfigurationRetention.class, //- 90
        TestRealtimeRecordDelayFilterAccelerationFaster.class, //- 79
        TestRealtimeRecordDelayFilterAccelerationSlowdown.class, //- 79
        TestRealtimeRecordDelayFilterNoAcceleration.class, //- 79
        TestEventRecordTraceReconstructionFilter.class, //- 76
        TestPlugin.class, //- 64
        TimeReaderTest.class, //- 60
        TestMonitoringThroughputFilter.class, //- 58
        TestTraceIdFilter.class, //- 57
        TestTimestampFilter.class, //- 55
        TypeFilterTest.class, //- 53
        MTreeGeneratorTest.class, //- 53
        TestCountingFilter.class, //- 52
        TestTeeFilter.class, //- 50
        TestPluginShutdown.class, //- 39
        MTreeGeneratorStageTest.class, //- 33
        OPTICSTest.class, //- 32
        OperationAndCallGeneratorStageTest.class, //- 32
        UserSessionToBehaviorModelTransformationTest.class, //- 31
        EventRecordTraceReconstructionStageTest.class, //- 29
        TraceRecordsTraceReconstructionStageTest.class, //- 27
        SessionReconstructionFilterTest.class, //- 27
        EntryCallSequenceStageTest.class, //- 22
        UserBehaviorCostFunctionTest.class, //- 21
        TraceReconstructionFilterTest.class, //- 15
        GraphEditDistanceTest.class, // -15
        CreateEntryLevelEventStageTest.class, //- 14
        //StorageTypeModelAssemblerTest.class, //- 10
        //StorageDeploymentModelAssemblerTest.class, //- 22
        //StorageAssemblyModelAssemblerTest.class, //- 15
        OperationTypeModelAssemblerTest.class, //- 14
        TimestampFilterTest.class, //- 14
        DynamicEventDispatcherTest.class, //- 13
        BinaryDeserializerTest.class, //- 11
        AssemblyLevelComponentDependencyGraphBuilderFactoryTest.class, //- 10
        TypeLevelOperationDependencyGraphBuilderFactoryTest.class, //- 10
        AssemblyLevelOperationDependencyGraphBuilderFactoryTest.class, //- 10
        DeploymentLevelComponentDependencyGraphBuilderFactoryTest.class, //- 10
        DeploymentLevelContextDependencyGraphBuilderFactoryTest.class, //- 10
        DeploymentLevelOperationDependencyGraphBuilderFactoryTest.class, //- 10
        TypeLevelComponentDependencyGraphBuilderFactoryTest.class, //- 10
        RunningMedianTest.class, //- 9
        ExecutionRecordTransformationStageTest.class, //- 8
        BasicCostFunctionTest.class, //- 8
        TraceIdFilterTest.class, //- 8
        NetworkAccessHandlerTest.class, //- 8
        MedoidGeneratorTest.class, //- 7
        CurrentTimeEventGeneratorFilterTest.class, //- 7
        EventRecordTraceCounterTest.class, //- 7
        HostnameRepositoryTest.class, //- 6
        ThreadEvent2TraceEventStageTest.class, //- 6
        DataCollectorStageTest.class, //- 6
        DirectoryScannerStageIntegrationTest.class, //- 6
        EquivalenceClassWriterTest.class, //- 5
        NaiveMedoidGeneratorTest.class, //- 5
        MonitoringThroughputStageTest.class, //- 5
        AcceptanceModeConverterTest.class, //- 4
        CountingFilterTest.class, //- 4
        TypeFilterTest.class, //- 4
        PipeReaderThreadTest.class, //- 3
        TeeFilterTest.class, //- 3
        JavaComponentSignatureExtractorTest.class, //- 2
        JavaOperationSignatureExtractorTest.class,// - 2
        AbstractSourceModelAssemblerTest.class, //- 2
        TrimmedAlgorithmTest.class, //- 2
        ModelAssemblerStageTest.class, //- 0
        ExtractDBScanClustersStageTest.class, //- 0
        TestStringBufferFilter.class, //- 0
        TestLegacyExecutionRecordReader.class, //- 0
        TestNoInputPortsForReader.class, //- 0
        TestGlobalConfiguration.class, //- 0
})
public class TopLevelSuite {
    /*static class InSuiteOnly {
        @Ignore
        public static class SuiteProbeControllerTest extends ProbeControllerTest {}
    }*/
}