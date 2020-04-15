/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader.filesystem.format.text.file;

import java.util.HashSet;
import java.util.Set;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysisteetime.plugin.reader.filesystem.util.MappingException;
import kieker.common.exception.IllegalRecordFormatException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import teetime.stage.util.TextLineContainer;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 *
 * @deprecated since 1.15 removed 1.16
 */
@Deprecated
public class TextLine2RecordFilter extends AbstractConsumerStage<TextLineContainer> {

	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();

	private final Set<String> unknownTypesObserved = new HashSet<>();

	private boolean ignoreUnknownRecordTypes;

	// private boolean abortDueToUnknownRecordType; // currently not used

	private RecordFromTextLineCreator recordFromTextLineCreator;

	/**
	 * @since 1.10
	 */
	public TextLine2RecordFilter(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.recordFromTextLineCreator = new RecordFromTextLineCreator(classNameRegistryRepository);
	}

	/**
	 * @since 1.10
	 */
	public TextLine2RecordFilter() {
		super();
	}

	public boolean isIgnoreUnknownRecordTypes() {
		return this.ignoreUnknownRecordTypes;
	}

	public void setIgnoreUnknownRecordTypes(final boolean ignoreUnknownRecordTypes) {
		this.ignoreUnknownRecordTypes = ignoreUnknownRecordTypes;
	}

	public RecordFromTextLineCreator getRecordFromTextLineCreator() {
		return this.recordFromTextLineCreator;
	}

	public void setRecordFromTextLineCreator(final RecordFromTextLineCreator recordFromTextLineCreator) {
		this.recordFromTextLineCreator = recordFromTextLineCreator;
	}

	@Override
	protected void execute(final TextLineContainer textLine) {
		try {
			final IMonitoringRecord record = this.recordFromTextLineCreator.createRecordFromLine(textLine.getTextFile(), textLine.getTextLine());
			this.outputPort.send(record);
		} catch (final MonitoringRecordException e) {
			this.logger.error("Could not create record from text line: '" + textLine + "'", e);
		} catch (final IllegalRecordFormatException e) {
			this.logger.error("Illegal record format: " + textLine, e);
		} catch (final MappingException e) {
			this.logger.error("", e);
		} catch (final UnknownRecordTypeException e) {
			final String classname = e.getClassName();
			if (!this.ignoreUnknownRecordTypes) {
				// this.abortDueToUnknownRecordType = true; // currently not used
				this.logger.error("Failed to load record type " + classname, e);
			} else if (!this.unknownTypesObserved.contains(classname)) {
				this.unknownTypesObserved.add(classname);
				this.logger.error("Failed to load record type " + classname, e); // log once for this type
			}
		}
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

}
