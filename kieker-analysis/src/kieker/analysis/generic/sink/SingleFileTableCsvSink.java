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
public class SingleFileTableCsvSink<R, T> extends AbstractConsumerStage<Table<R, T>> {

	public static final char[] LF = { 10 };
	public static final char[] CRLF = { 13, 10 };
	public static final char[] CR = { 13 };

	private final boolean header;
	private final Class<T> clazz;
	private final char[] newline;
	private final Path path;

	/**
	 * Create table sink.
	 *
	 * @param path
	 *            file path
	 * @param clazz
	 *            row data type
	 * @param header
	 *            boolean flag specify whether a header line should be written
	 * @param newline
	 *            end of line marker
	 */
	public SingleFileTableCsvSink(final Path path, final Class<T> clazz, final boolean header, final char[] newline) {
		this.header = header;
		this.path = path;
		this.clazz = clazz;
		this.newline = newline; // NOPMD
	}

	@Override
	protected void execute(final Table<R, T> table) throws IOException {
		try (final BufferedWriter outputStream = Files.newBufferedWriter(this.path, StandardCharsets.UTF_8)) {
			final CsvClient<T> csvClient = new CsvClientImpl<>(outputStream, this.clazz);
			csvClient.setEndOfLine(this.newline);
			csvClient.setUseHeader(this.header);
			csvClient.writeBeans(table.getRows());
			outputStream.close();
		}
	}
}
