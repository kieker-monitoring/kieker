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
package kieker.analysis.trace.sink;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import kieker.analysis.trace.AbstractTraceProcessingFilter;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.InvalidExecutionTrace;

/**
 * TODO can be merged with the other two TraceWriteFilters using generics
 * Writes invalid execution traces to a specified file.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.2
 */
public class InvalidExecutionTraceWriterSink extends AbstractTraceProcessingFilter<InvalidExecutionTrace> {

	private static final String ENCODING = "UTF-8";

	private final PrintStream printStream;
	private final String outputFilename;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 *            system model repository
	 * @param outputFile
	 *            file handle for the output file
	 *
	 * @throws IOException
	 *             If the write stream could not be prepared.
	 */
	public InvalidExecutionTraceWriterSink(final SystemModelRepository repository, final File outputFile)
			throws IOException {
		super(repository);

		this.printStream = new PrintStream(Files.newOutputStream(outputFile.toPath()), false,
				InvalidExecutionTraceWriterSink.ENCODING);
		this.outputFilename = outputFile.getCanonicalPath();
	}

	@Override
	protected void execute(final InvalidExecutionTrace element) throws Exception {
		this.printStream.println(element.getInvalidExecutionTraceArtifacts().toString());
		this.reportSuccess(element.getInvalidExecutionTraceArtifacts().getTraceId());
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		this.logger.debug("Wrote {} execution trace artifact{} to file '{}'", numTraces, (numTraces > 1 ? "s" : ""), // NOCS
				this.outputFilename);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTerminating() {
		if (this.printStream != null) {
			this.printStream.close();
		}
		super.onTerminating();
	}

}
