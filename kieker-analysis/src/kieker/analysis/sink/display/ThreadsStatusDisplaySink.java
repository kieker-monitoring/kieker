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

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.common.record.jvm.ThreadsStatusRecord;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Nils Christian Ehmke, Lars Bluemke, Sören Henning
 *
 * @since 1.10
 */
public class ThreadsStatusDisplaySink extends AbstractConsumerStage<ThreadsStatusRecord> {

	public static final String TOTAL_THREADS = "Total Threads";
	public static final String THREADS = "Threads";
	public static final String DAEMON_THREADS = "Daemon Threads";
	public static final String PEAK_THREADS = "Peak Threads";

	private final XYPlot xyplot;

	private final TimeUnit recordsTimeUnit;

	/**
	 * Creates a new instance of this filter.
	 *
	 * @param numberOfEntries
	 *            Maximal number of entries in a XYPlot
	 * @param recordsTimeUnit
	 *            Time unit to interpret the timestamp of a record passed to the input port
	 */
	public ThreadsStatusDisplaySink(final int numberOfEntries, final TimeUnit recordsTimeUnit) {
		this.recordsTimeUnit = recordsTimeUnit;

		// Create the display objects
		this.xyplot = new XYPlot(numberOfEntries);
	}

	/**
	 * This method represents the input port receiving the incoming events.
	 *
	 * @param record
	 *            The record to display and relay.
	 */
	@Override
	public void execute(final ThreadsStatusRecord record) {
		this.updateDisplays(record);
	}

	private void updateDisplays(final ThreadsStatusRecord record) {
		// Calculate the minutes and seconds of the logging timestamp of the record
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.recordsTimeUnit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = record.getHostname() + " - " + record.getVmName();

		this.xyplot.setEntry(id + " - " + ThreadsStatusDisplaySink.THREADS, minutesAndSeconds, record.getThreadCount());
		this.xyplot.setEntry(id + " - " + ThreadsStatusDisplaySink.TOTAL_THREADS, minutesAndSeconds, record.getTotalStartedThreadCount());
		this.xyplot.setEntry(id + " - " + ThreadsStatusDisplaySink.PEAK_THREADS, minutesAndSeconds, record.getPeakThreadCount());
		this.xyplot.setEntry(id + " - " + ThreadsStatusDisplaySink.DAEMON_THREADS, minutesAndSeconds, record.getDaemonThreadCount());
	}

	@Display(name = "XYPlot Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}

}
