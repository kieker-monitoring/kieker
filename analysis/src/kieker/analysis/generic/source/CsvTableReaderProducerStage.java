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

import kieker.analysis.generic.IPathLabelMapper;
import kieker.analysis.generic.Table;

import teetime.framework.AbstractProducerStage;

/**
 * Reader for a CSV file. Outputs the whole file as a table.
 *
 * @param <R>
 *            label data type
 * @param <T>
 *            record data type
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class CsvTableReaderProducerStage<R, T> extends AbstractProducerStage<Table<R, T>> {

	private final CsvClient<T> csvClient;
	private final BufferedReader reader;
	private final Path path;
	private final Table<R, T> table;

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
	 * @param label
	 *            table label
	 * @throws IOException
	 *             when a stream could not be opened.
	 */
	public CsvTableReaderProducerStage(final Path path, final char separator, final char quoteSymbol,
			final char escapeSymbol, final boolean header, final Class<T> clazz, final R label) throws IOException {
		this.path = path;
		this.reader = Files.newBufferedReader(path);
		this.csvClient = new CsvClientImpl<>(this.reader, clazz);
		this.csvClient.setQuote(quoteSymbol);
		this.csvClient.setSeparator(separator);
		this.csvClient.setEscape(escapeSymbol);
		this.csvClient.setUseHeader(header);
		this.table = new Table<>(label);
	}

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
	 * @param mapper
	 *            table label mapper object
	 * @throws IOException
	 *             when a stream could not be opened.
	 */
	public CsvTableReaderProducerStage(final Path path, final char separator, final char quoteSymbol,
			final char escapeSymbol, final boolean header, final Class<T> clazz, final IPathLabelMapper<R> mapper)
			throws IOException {
		this(path, separator, quoteSymbol, escapeSymbol, header, clazz, mapper.map(path));
	}

	@Override
	protected void execute() throws Exception {
		this.csvClient.skipEmptyLines(true);
		try {
			while (!this.csvClient.isFinished()) {
				final T bean = this.csvClient.readBean();
				if (bean != null) {
					this.table.getRows().add(bean);
				} else {
					break;
				}
			}

			this.outputPort.send(this.table);
			this.reader.close();
		} catch (final CsvException e) {
			this.logger.error("Error reading csv file in line {} path {}", this.csvClient.getCurrentLine(),
					this.path.toString());
			throw e;
		}
		this.workCompleted();
	}

}
