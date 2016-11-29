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

import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.common.record.jvm.GCRecord;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.10
 */
public class GCDisplayFilter extends AbstractConsumerStage<GCRecord> {

	public static final String COLLECTION_TIME = "Collection Time";
	public static final String COLLECTION_COUNT = "Collection Count";

	private final XYPlot xyplot;

	private final int numberOfEntries;

	private final TimeUnit recordsTimeUnit;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param numberOfEntries
	 *            Maximal number of entries in a XYPlot
	 * @param recordsTimeUnit
	 *            Time unit to interpret the timestamp of a record passed to the input port
	 */
	public GCDisplayFilter(final int numberOfEntries, final TimeUnit recordsTimeUnit) {
		this.numberOfEntries = numberOfEntries;
		this.recordsTimeUnit = recordsTimeUnit;

		// Create the display objects
		this.xyplot = new XYPlot(this.numberOfEntries);
	}

	@Override
	protected void execute(final GCRecord record) {
		this.updateDisplays(record);
	}

	private void updateDisplays(final GCRecord record) {
		// Calculate the minutes and seconds of the logging timestamp of the record
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.recordsTimeUnit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = record.getHostname() + " - " + record.getVmName() + " - " + record.getGcName();

		this.xyplot.setEntry(id + " - " + GCDisplayFilter.COLLECTION_COUNT, minutesAndSeconds, record.getCollectionCount());
		this.xyplot.setEntry(id + " - " + GCDisplayFilter.COLLECTION_TIME, minutesAndSeconds, record.getCollectionTimeMS());
	}

	@Display(name = "XYPlot Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}

}
