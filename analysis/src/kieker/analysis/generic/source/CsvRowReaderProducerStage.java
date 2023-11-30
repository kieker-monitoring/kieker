/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.csveed.report.CsvException;

import teetime.framework.AbstractProducerStage;

/**
 * Reader for a single CSV file. Every row is outputed as one event.
 *
 * @param <T>
 *            ICsvRecord datatype
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class CsvRowReaderProducerStage<T> extends AbstractProducerStage<T> {

	private final CsvClient<T> csvClient;
	private final BufferedReader reader;
	private final Path path;

	/**
	 * Read a single CSV file.
	 *
	 * @param path
	 *            file path
	 * @param separator
	 *            string containing the separator symbol for cells
	 * @param quoteSymbol
	 *            quote symbol used for cells
	 * @param escapeSymbol
	 *            escape character
	 * @param header
	 *            indicate how to interpret the first line in the CSV file, set to true to indicate
	 *            that the first line contains the header information
	 * @param clazz
	 *            bean class
	 * @throws IOException
	 *             when a stream could not be opened.
	 */
	public CsvRowReaderProducerStage(final Path path, final char separator, final char quoteSymbol,
			final char escapeSymbol, final boolean header, final Class<T> clazz) throws IOException {
		this.path = path;
		this.reader = Files.newBufferedReader(path);
		this.csvClient = new CsvClientImpl<>(this.reader, clazz);
		this.csvClient.setQuote(quoteSymbol);
		this.csvClient.setSeparator(separator);
		this.csvClient.setEscape(escapeSymbol);
		this.csvClient.setUseHeader(header);
	}

	@Override
	protected void execute() throws Exception {
		this.csvClient.skipEmptyLines(true);
		try {
			while (!this.csvClient.isFinished()) {
				final T bean = this.csvClient.readBean();
				if (bean != null) {
					this.outputPort.send(bean);
				} else {
					break;
				}
			}

			this.reader.close();
		} catch (final CsvException e) {
			this.logger.error("Error reading csv file in line {} path {}", this.csvClient.getCurrentLine(),
					this.path.toString());
			throw e;
		}
		this.workCompleted();
	}

}
