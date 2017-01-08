/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.analysisteetime.plugin.filter.forward;

import java.util.LinkedList;
import java.util.List;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.framework.signal.ISignal;
import teetime.framework.signal.TerminatingSignal;

/**
 * @author LarsBlumke
 *
 * @since 1.13
 */
public class AnalysisThroughputFilter2 extends AbstractConsumerStage<IMonitoringRecord> {

	private final InputPort<Long> triggerInputPort = this.createInputPort();
	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();
	private long numPassedElements;
	private final List<Long> throughputs = new LinkedList<Long>();

	/**
	 * Empty default constructor
	 */
	public AnalysisThroughputFilter2() {
		// empty default constructor
	}

	/**
	 * This method represents the input port of this filter.
	 *
	 * @param record
	 *            The incoming {@link IMonitoringRecord}
	 */
	@Override
	protected void execute(final IMonitoringRecord record) {
		final Long timestampInNs = this.triggerInputPort.receive();
		if (timestampInNs != null) {
			this.computeElementThroughput();
		}
		this.numPassedElements++;
		this.outputPort.send(record);
	}

	@Override
	public void onTerminating() throws Exception { // NOPMD
		System.out.println("ATF terminating");
		final Long timestampInNs = this.triggerInputPort.receive();
		if (timestampInNs != null) {
			this.computeElementThroughput();
		}
		super.onTerminating();
	}

	private void computeElementThroughput() {
		System.out.println("ATF Computing element throughput " + this.numPassedElements);
		this.throughputs.add(this.numPassedElements);
		this.numPassedElements = 0;

		// Also log intervals where no records arrived
		while (this.triggerInputPort.receive() != null) {
			System.out.println("ATF Computing element throughput " + this.numPassedElements);
			this.throughputs.add(0L);
		}
	}

	@Override
	public void onSignal(final ISignal signal, final InputPort<?> inputPort) {
		if (signal instanceof TerminatingSignal) {
			System.out.println("I GOT A TERMINATION SIGNAL " + signal);
			this.terminateStage();
		}
		super.onSignal(signal, inputPort);
	}

	public List<Long> getThroughputs() {
		return this.throughputs;
	}

	public InputPort<Long> getTriggerInputPort() {
		return this.triggerInputPort;
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}
}
