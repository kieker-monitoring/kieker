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

package kieker.panalysis.composite;

import java.util.Map;

import kieker.panalysis.File2TextLinesFilter;
import kieker.panalysis.MethodCallPipe;
import kieker.panalysis.TextLine2RecordFilter;
import kieker.panalysis.base.AbstractFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ReadRecordFromCsvFileFilter extends AbstractFilter<File2TextLinesFilter.INPUT_PORT, TextLine2RecordFilter.OUTPUT_PORT> {

	private final File2TextLinesFilter stage0;
	private final TextLine2RecordFilter stage1;

	public ReadRecordFromCsvFileFilter() {
		super(File2TextLinesFilter.INPUT_PORT.class, TextLine2RecordFilter.OUTPUT_PORT.class);

		// FIXME replace null value
		final Map<Integer, String> stringRegistry = null;

		this.stage1 = new TextLine2RecordFilter(stringRegistry);
		this.stage0 = new File2TextLinesFilter();

		new MethodCallPipe().connect(this.stage0, File2TextLinesFilter.OUTPUT_PORT.TEXT_LINE, this.stage1, TextLine2RecordFilter.INPUT_PORT.TEXT_LINE);
	}

	public void execute() {
		this.stage0.execute();
	}

}
