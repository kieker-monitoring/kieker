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

package kieker.panalysis.stage.kieker.fileToRecord.textLine;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kieker.common.exception.IllegalRecordFormatException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.IMonitoringRecord;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.kieker.RecordFromTextLineCreator;
import kieker.panalysis.stage.MappingException;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class TextLine2RecordFilter extends AbstractFilter<TextLine2RecordFilter> {

	public final IInputPort<TextLine2RecordFilter, String> textLineInputPort = this.createInputPort();

	public final IOutputPort<TextLine2RecordFilter, IMonitoringRecord> recordOutputPort = this.createOutputPort();

	private boolean ignoreUnknownRecordTypes;
	private boolean abortDueToUnknownRecordType;

	private final Set<String> unknownTypesObserved = new HashSet<String>();

	private Map<Integer, String> stringRegistry;

	private final RecordFromTextLineCreator recordFromTextLineCreator;

	/**
	 * @since 1.10
	 */
	public TextLine2RecordFilter() {
		// FIXME stringRegistry
		this.recordFromTextLineCreator = new RecordFromTextLineCreator(this.stringRegistry);
	}

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<TextLine2RecordFilter> context) {
		final String textLine = context.tryTake(this.textLineInputPort);
		if (textLine == null) {
			return false;
		}

		try {
			final IMonitoringRecord record = this.recordFromTextLineCreator.createRecordFromLine(textLine);
			context.put(this.recordOutputPort, record);
		} catch (final MonitoringRecordException e) {
			this.logger.error("Could not create record from text line: '" + textLine + "'", e);
		} catch (final IllegalRecordFormatException e) {
			this.logger.error("Illegal record format: " + textLine, e);
		} catch (final MappingException e) {
			this.logger.error("", e);
		} catch (final UnknownRecordTypeException e) {
			final String classname = e.getClassName();
			if (!this.ignoreUnknownRecordTypes) {
				this.abortDueToUnknownRecordType = true;
				this.logger.error("Failed to load record type " + classname, e);
			} else if (!this.unknownTypesObserved.contains(classname)) {
				this.unknownTypesObserved.add(classname);
				this.logger.error("Failed to load record type " + classname, e); // log once for this type
			}
		}

		return true;
	}

}
