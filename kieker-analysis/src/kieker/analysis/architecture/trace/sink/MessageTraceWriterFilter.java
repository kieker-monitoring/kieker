/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.trace.sink;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import kieker.analysis.trace.AbstractTraceProcessingStage;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.MessageTrace;

/**
 * A filter allowing to write the incoming MessageTraces into a configured file.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.2
 */
public class MessageTraceWriterFilter extends AbstractTraceProcessingStage<MessageTrace> {

	private static final String ENCODING = "UTF-8";

	private final String outputFilename;
	private final PrintStream printStream;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 *            system model repository
	 * @param outputFile
	 *            output file
	 *
	 * @throws IOException
	 *             If the write stream could not be prepared.
	 */
	public MessageTraceWriterFilter(final SystemModelRepository repository, final File outputFile) throws IOException {
		super(repository);

		this.outputFilename = outputFile.getCanonicalPath();
		this.printStream = new PrintStream(Files.newOutputStream(outputFile.toPath()), false, ENCODING);
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		this.logger.debug("Wrote {} trace{} to file '{}'", numTraces, (numTraces > 1 ? "s" : ""), this.outputFilename); // NOCS
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminatiing {}", this.getClass().getCanonicalName());
		if (this.printStream != null) {
			this.printStream.close();
		}
		super.onTerminating();
	}

	@Override
	protected void execute(final MessageTrace messageTrace) throws Exception {
		MessageTraceWriterFilter.this.printStream.println(messageTrace.toString());
		MessageTraceWriterFilter.this.reportSuccess(messageTrace.getTraceId());
	}
}
