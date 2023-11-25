/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic.sink;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;

import kieker.analysis.generic.Table;

import teetime.framework.AbstractConsumerStage;

/**
 * Save tables with a specific row type as a csv files based on a path function.
 *
 * @param <R>
 *            label type
 * @param <T>
 *            row type
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class TableCsvSink<R, T> extends AbstractConsumerStage<Table<R, T>> {

	public static final char[] LF = { 10 };
	public static final char[] CRLF = { 13, 10 };
	public static final char[] CR = { 13 };

	private final Function<String, Path> filePathFunction;
	private final boolean header;
	private Class<T> clazz;
	private char[] newline;

	/**
	 * Create table sink.
	 *
	 * @param filePathFunction
	 *            function to map string to path
	 * @param clazz
	 *            row data type
	 * @param header
	 *            boolean flag specify whether a header line should be written
	 * @param newline
	 *            end of line marker
	 */
	public TableCsvSink(final Function<String, Path> filePathFunction, final Class<T> clazz, final boolean header,
			final char[] newline) {
		this.header = header;
		this.filePathFunction = filePathFunction;
		this.clazz = clazz;
		this.newline = newline; // NOPMD
	}

	/**
	 * Create table sink.
	 *
	 * @param filePath
	 *            directory path where the output files are placed in.
	 * @param filename
	 *            filename suffix
	 * @param clazz
	 *            row data type
	 * @param header
	 *            boolean flag specify whether a header line should be written
	 * @param newline
	 *            end of line marker
	 */
	public TableCsvSink(final Path filePath, final String filename, final Class<T> clazz, final boolean header,
			final char[] newline) {
		this(new Function<String, Path>() {

			@Override
			public Path apply(final String name) {
				return filePath.resolve(String.format("%s-%s", name, filename));
			}
		}, clazz, header, newline);
	}

	/**
	 * Create table sink.
	 *
	 * @param filePath
	 *            directory path where the output files are placed in.
	 * @param clazz
	 *            row data type
	 * @param header
	 *            boolean flag specify whether a header line should be written
	 * @param newline
	 *            end of line marker
	 */
	public TableCsvSink(final Path filePath, final Class<T> clazz, final boolean header, final char[] newline) {
		this(new Function<String, Path>() {

			@Override
			public Path apply(final String name) {
				return filePath.resolve(String.format("%s.csv", name));
			}
		}, clazz, header, newline);
	}

	@Override
	protected void execute(final Table<R, T> table) throws IOException {
		try (final BufferedWriter outputStream = Files
				.newBufferedWriter(this.filePathFunction.apply(table.getLabel().toString()), StandardCharsets.UTF_8)) {
			final CsvClient<T> csvClient = new CsvClientImpl<>(outputStream, this.clazz);
			csvClient.setEndOfLine(this.newline);
			csvClient.setUseHeader(this.header);
			csvClient.writeBeans(table.getRows());
			outputStream.close();
		}
	}
}
