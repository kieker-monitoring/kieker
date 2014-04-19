/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.stage.composite;

import java.util.Map;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.stage.File2TextLinesFilter;
import kieker.panalysis.stage.TextLine2RecordFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ReadRecordFromCsvFileFilter extends AbstractFilter<File2TextLinesFilter> {

	private final File2TextLinesFilter stage0;
	private final TextLine2RecordFilter stage1;

	/**
	 * @since 1.10
	 * @param textLinePipe
	 */
	public ReadRecordFromCsvFileFilter(final IPipe<String, ?> textLinePipe) {
		// FIXME replace null value
		final Map<Integer, String> stringRegistry = null;

		this.stage0 = new File2TextLinesFilter();
		this.stage1 = new TextLine2RecordFilter(stringRegistry);

		textLinePipe
				.source(this.stage0.TEXT_LINE)
				.target(this.stage1, this.stage1.TEXT_LINE);
		// FIXME textLinePipe needs to be added to a group

		/*
		 * FIXME a composite filter only serves as a convenient way to connect multiple stages or to realize self-connected queues.<br>
		 * How should the stage scheduler be aware of these internal stages?<br>
		 * Should the context not be passed to each of the internal stages?
		 */
	}

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<File2TextLinesFilter> context) {
		return this.stage0.execute();
	}

}
