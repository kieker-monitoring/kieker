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

package kieker.analysisteetime.plugin.filter.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.display.PieChart;
import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.common.record.system.MemSwapUsageRecord;

import teetime.framework.AbstractConsumerStage;

/**
 * This is a filter which accepts {@link MemSwapUsageRecord} instances and provides different views to visualize them.
 *
 * @author Bjoern Weissenfels, Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.8
 */
public class MemSwapUtilizationDisplayFilter extends AbstractConsumerStage<MemSwapUsageRecord> {

	public static final String MEM_FREE = "memFree";
	public static final String MEM_TOTAL = "memTotal";
	public static final String MEM_USED = "memUsed";
	public static final String SWAP_FREE = "swapFree";
	public static final String SWAP_TOTAL = "swapTotal";
	public static final String SWAP_USED = "swapUsed";

	private final XYPlot xyplot;
	private final PieChart memPieChart;
	private final PieChart swapPieChart;

	private final TimeUnit recordsTimeUnit;

	/**
	 * Creates a new instance of this filter.
	 *
	 * @param numberOfEntries
	 *            Maximal number of entries in a XYPlot
	 * @param recordsTimeUnit
	 *            Time unit to interpret the timestamp of a record passed to the input port
	 */
	public MemSwapUtilizationDisplayFilter(final int numberOfEntries, final TimeUnit recordsTimeUnit) {
		this.recordsTimeUnit = recordsTimeUnit;

		// Create the display objects
		this.xyplot = new XYPlot(numberOfEntries);
		this.memPieChart = new PieChart();
		this.swapPieChart = new PieChart();
	}

	/**
	 * This method represents the input port receiving the incoming events.
	 *
	 * @param record
	 *            The record to display and relay.
	 */
	@Override
	protected void execute(final MemSwapUsageRecord record) {
		this.updateDisplays(record);
	}

	private void updateDisplays(final MemSwapUsageRecord record) {
		// Calculate the minutes and seconds of the logging timestamp of the record
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.recordsTimeUnit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = record.getHostname();

		// Sets Entries in MB instead of Bytes
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.MEM_FREE, minutesAndSeconds, record.getMemFree() / 1048576);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.MEM_TOTAL, minutesAndSeconds, record.getMemTotal() / 1048576);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.MEM_USED, minutesAndSeconds, record.getMemUsed() / 1048576);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_FREE, minutesAndSeconds, record.getSwapFree() / 1048576);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_TOTAL, minutesAndSeconds, record.getSwapTotal() / 1048576);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_USED, minutesAndSeconds, record.getSwapUsed() / 1048576);

		this.memPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.MEM_FREE, record.getMemFree());
		this.memPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.MEM_USED, record.getMemUsed());

		this.swapPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_FREE, record.getSwapFree());
		this.swapPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_USED, record.getSwapUsed());
	}

	@Display(name = "XYPlot Memory utilization Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}

	@Display(name = "PieChart Memory Utilization Display")
	public PieChart getMemPieChart() {
		return this.memPieChart;
	}

	@Display(name = "PieChart Swap Utilization Display")
	public PieChart getSwapPieChart() {
		return this.swapPieChart;
	}

}
