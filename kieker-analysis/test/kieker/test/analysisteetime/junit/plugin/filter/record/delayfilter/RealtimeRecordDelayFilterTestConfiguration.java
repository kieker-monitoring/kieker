package kieker.test.analysisteetime.junit.plugin.filter.record.delayfilter;

import kieker.analysisteetime.plugin.filter.forward.AnalysisThroughputFilter;
import kieker.analysisteetime.plugin.filter.record.delayfilter.RealtimeRecordDelayFilter;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.Configuration;
import teetime.stage.Clock;
import teetime.stage.CollectorSink;
import teetime.stage.Counter;
import teetime.stage.InitialElementProducer;

public class RealtimeRecordDelayFilterTestConfiguration extends Configuration {

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

		/*
		 * Problems with this config:
		 * - AnalysisThroughputFilter does not terminate!
		 */
		throughputStage.declareActive();

		this.connectPorts(recordProducer.getOutputPort(), preDelayCounter.getInputPort());
		this.connectPorts(preDelayCounter.getOutputPort(), delayFilter.getInputPort());
		this.connectPorts(delayFilter.getOutputPort(), postDelayCounter.getInputPort());
		this.connectPorts(postDelayCounter.getOutputPort(), throughputStage.getRecordsInputPort());
		this.connectPorts(throughputStage.getRecordsOutputPort(), recordCollectorSink.getInputPort());
		this.connectPorts(clock.getOutputPort(), throughputStage.getTimestampsInputPort());
		this.connectPorts(throughputStage.getRecordsCountOutputPort(), throughputCollectorSink.getInputPort());

		/*
		 * See also the alternative AnalysisThroughputFilter2!
		 * Differences to AnalysisThroughputFilter
		 * - Just triggered with incoming records not checking the ports all the time
		 *
		 * AnalysisThroughputFilter2 does not terminate either! See test of AnalysisThroughputFilter2
		 * at kieker.plugin.filter.forward...
		 */

		/////////////// MORE CONFIGS FOR DEBUGGING: /////////////////////////////

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
