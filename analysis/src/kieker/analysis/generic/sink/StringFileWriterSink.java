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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import teetime.framework.AbstractConsumerStage;

/**
 * Writes all received strings in a file. Each string is put on a new line.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class StringFileWriterSink extends AbstractConsumerStage<String> {

	private final PrintWriter outputStream;

	public StringFileWriterSink(final Path path) throws IOException {
		this.outputStream = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8));
	}

	@Override
	protected void execute(final String element) throws Exception {
		this.outputStream.println(element);
	}

	@Override
	protected void onTerminating() {
		this.outputStream.close();
		super.onTerminating();
	}

}
