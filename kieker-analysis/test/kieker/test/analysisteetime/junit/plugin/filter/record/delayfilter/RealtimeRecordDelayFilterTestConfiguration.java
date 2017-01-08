package kieker.test.analysisteetime.junit.plugin.filter.record.delayfilter;

import kieker.analysisteetime.plugin.filter.forward.AnalysisThroughputFilter;
import kieker.analysisteetime.plugin.filter.record.delayfilter.RealtimeRecordDelayFilter;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.Configuration;
import teetime.stage.Clock;
import teetime.stage.CollectorSink;
import teetime.stage.Counter;
import teetime.stage.ElementThroughputMeasuringStage;
import teetime.stage.InitialElementProducer;
import teetime.stage.basic.distributor.Distributor;

public class RealtimeRecordDelayFilterTestConfiguration extends Configuration {

	ElementThroughputMeasuringStage<IMonitoringRecord> dfsfds;

	public RealtimeRecordDelayFilterTestConfiguration(
			final InitialElementProducer<IMonitoringRecord> recordProducer,
			final Counter<IMonitoringRecord> preDelayCounter,
			final RealtimeRecordDelayFilter delayFilter,
			final Clock clock,
			final AnalysisThroughputFilter throughputStage,
			final Counter<IMonitoringRecord> postDelayCounter,
			final CollectorSink<IMonitoringRecord> recordCollectorSink,
			final CollectorSink<Long> throughputCollectorSink) {

		/////////////// CONFIGURATION AS INTENDED ///////////////////////////////////

		// Connect ports of test configuration
		final Distributor<IMonitoringRecord> distributor = new Distributor<IMonitoringRecord>();
		throughputStage.declareActive();

		// this.connectPorts(recordProducer.getOutputPort(), preDelayCounter.getInputPort());
		// this.connectPorts(preDelayCounter.getOutputPort(), delayFilter.getInputPort());
		// this.connectPorts(delayFilter.getOutputPort(), postDelayCounter.getInputPort());
		// this.connectPorts(postDelayCounter.getOutputPort(), distributor.getInputPort());
		// this.connectPorts(distributor.getNewOutputPort(), recordCollectorSink.getInputPort());
		// this.connectPorts(distributor.getNewOutputPort(), throughputStage.getRecordsInputPort());
		// this.connectPorts(clock.getOutputPort(), throughputStage.getTimestampsInputPort());
		// this.connectPorts(throughputStage.getRecordsCountOutputPort(), throughputCollectorSink.getInputPort());

		/////////////// SEVERAL CONFIGS FOR DEBUGGING: /////////////////////////////

		// test without RRDF - doesn't terminate
		// this.connectPorts(recordProducer.getOutputPort(), throughputStage.getRecordsInputPort());
		// this.connectPorts(clock.getOutputPort(), throughputStage.getTimestampsInputPort());
		// this.connectPorts(throughputStage.getRecordsCountOutputPort(), throughputCollectorSink.getInputPort());

		// test with TeeTime's throughput stage - doesn't terminate
		// final ElementThroughputMeasuringStage<IMonitoringRecord> tpstage = new ElementThroughputMeasuringStage<IMonitoringRecord>();
		// tpstage.declareActive();
		// this.connectPorts(recordProducer.getOutputPort(), tpstage.getInputPort());
		// this.connectPorts(clock.getOutputPort(), tpstage.getTriggerInputPort());
		// this.connectPorts(tpstage.getOutputPort(), recordCollectorSink.getInputPort());

		// mini test - doesn't terminate with old teetime but does with new teetime
		// this.connectPorts(recordProducer.getOutputPort(), recordCollectorSink.getInputPort());
		// this.connectPorts(clock.getOutputPort(), throughputCollectorSink.getInputPort());

		// basic configuration without AnalysisThrouputFilter WORKS
		// this.connectPorts(recordProducer.getOutputPort(), preDelayCounter.getInputPort());
		// this.connectPorts(preDelayCounter.getOutputPort(), delayFilter.getInputPort());
		// this.connectPorts(delayFilter.getOutputPort(), postDelayCounter.getInputPort());
		// this.connectPorts(postDelayCounter.getOutputPort(), recordCollectorSink.getInputPort());

		// basic configuration without AnalysisThrouputFilter but with clock
		// this.connectPorts(recordProducer.getOutputPort(), preDelayCounter.getInputPort());
		// this.connectPorts(preDelayCounter.getOutputPort(), delayFilter.getInputPort());
		// this.connectPorts(delayFilter.getOutputPort(), postDelayCounter.getInputPort());
		// this.connectPorts(postDelayCounter.getOutputPort(), recordCollectorSink.getInputPort());

	}

}
