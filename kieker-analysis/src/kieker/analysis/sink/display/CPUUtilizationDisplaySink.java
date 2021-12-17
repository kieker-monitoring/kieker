/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.sink.display;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.display.MeterGauge;
import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.common.record.system.CPUUtilizationRecord;

import teetime.framework.AbstractConsumerStage;

/**
 * This is a filter which accepts {@link CPUUtilizationRecord} instances and provides different views (XYPlot or MeterGauge) to visualize them.
 *
 * @author Bjoern Weissenfels, Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.8
 */
public class CPUUtilizationDisplaySink extends AbstractConsumerStage<CPUUtilizationRecord> {

	public static final String TOTAL_UTILIZATION = "totalUtilization";
	public static final String IDLE = "idle";
	public static final String IRQ = "irq";
	public static final String NICE = "nice";
	public static final String SYSTEM = "system";
	public static final String USER = "user";

	private final MeterGauge meterGauge;
	private final XYPlot xyplot;

	private final Number[] warningIntervals;
	private final TimeUnit recordsTimeUnit;

	/**
	 * Creates a new instance of this filter.
	 *
	 * @param numberOfEntries
	 *            Maximal number of entries in a XYPlot
	 * @param warningIntervals
	 *            The intervals for the colors displayed in a XYPlot
	 * @param recordsTimeUnit
	 *            Time unit to interpret the timestamp of a record passed to the input port
	 */
	public CPUUtilizationDisplaySink(final int numberOfEntries, final Number[] warningIntervals, final TimeUnit recordsTimeUnit) {
		this.warningIntervals = warningIntervals.clone();
		this.recordsTimeUnit = recordsTimeUnit;

		// Create the display objects
		this.meterGauge = new MeterGauge();
		this.xyplot = new XYPlot(numberOfEntries);
	}

	@Override
	protected void execute(final CPUUtilizationRecord record) {
		this.updateDisplays(record);
	}

	private void updateDisplays(final CPUUtilizationRecord record) {
		// Calculate the minutes and seconds of the logging timestamp of the record
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.recordsTimeUnit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = record.getHostname() + " - " + record.getCpuID();

		this.meterGauge.setIntervals(id, Arrays.asList(this.warningIntervals), Arrays.asList("66cc66", "E7E658", "cc6666"));
		this.meterGauge.setValue(id, record.getTotalUtilization() * 100);

		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplaySink.TOTAL_UTILIZATION, minutesAndSeconds, record.getTotalUtilization() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplaySink.IDLE, minutesAndSeconds, record.getIdle() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplaySink.IRQ, minutesAndSeconds, record.getIrq() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplaySink.NICE, minutesAndSeconds, record.getNice() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplaySink.SYSTEM, minutesAndSeconds, record.getSystem() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplaySink.USER, minutesAndSeconds, record.getUser() * 100);
	}

	@Display(name = "Meter Gauge CPU total utilization Display")
	public MeterGauge getMeterGauge() {
		return this.meterGauge;
	}

	@Display(name = "XYPlot CPU utilization Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}

}
