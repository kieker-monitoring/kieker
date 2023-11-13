/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.pp.log;

import java.io.IOException;

import kieker.analysis.code.data.CallerCalleeEntry;
import kieker.analysis.generic.source.CsvRowReaderProducerStage;

import teetime.framework.Configuration;

/**
 * Pipe and Filter configuration for the static log preprocessor.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class FixStaticLogTeetimeConfiguration extends Configuration {

	public FixStaticLogTeetimeConfiguration(final Settings parameterConfiguration) throws IOException {
		final CsvRowReaderProducerStage<CallerCalleeEntry> readCsvStage = new CsvRowReaderProducerStage<>(
				parameterConfiguration.getInputPath(), ',', '"', '\\', true, CallerCalleeEntry.class);

		final CsvFunctionMapperStage functionMapperStage = new CsvFunctionMapperStage(
				parameterConfiguration.getMapPaths());
		final CorrectCallsStage correctCallsStage = new CorrectCallsStage();
		correctCallsStage.declareActive();
		final CsvWriterStage writeCsvStage = new CsvWriterStage(parameterConfiguration.getOutputFile().toPath());

		this.connectPorts(readCsvStage.getOutputPort(), correctCallsStage.getInputPort());
		this.connectPorts(functionMapperStage.getOutputPort(), correctCallsStage.getMapInputPort());
		this.connectPorts(correctCallsStage.getOutputPort(), writeCsvStage.getInputPort());
	}
}
