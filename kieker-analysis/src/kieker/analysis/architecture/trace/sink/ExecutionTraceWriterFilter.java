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
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;

import kieker.analysis.trace.AbstractTraceProcessingStage;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.ExecutionTrace;

/**
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.2
 */
public class ExecutionTraceWriterFilter extends AbstractTraceProcessingStage<ExecutionTrace> {

	private static final String ENCODING = "UTF-8";

	private final String outputFilename;
	private final PrintStream printStream;

	private final OutputStream stream;

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
	public ExecutionTraceWriterFilter(final SystemModelRepository repository, final File outputFile) throws IOException {
		super(repository);

		this.outputFilename = outputFile.getCanonicalPath();
		this.stream = Files.newOutputStream(outputFile.toPath());
		this.printStream = new PrintStream(this.stream, false, ENCODING);
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numTraces = this.getSuccessCount();
		this.logger.debug("Wrote {} execution trace{} to file '{}'", numTraces, (numTraces > 1 ? "s" : ""), this.outputFilename); // NOCS
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminating {}", this.getClass().getCanonicalName());
		if (this.printStream != null) {
			try {
				this.printStream.flush();
				this.stream.flush();
				this.printStream.close();
				this.stream.close();
			} catch (final IOException e) {
				this.logger.error("Cannot write execution trace: {}", e.getLocalizedMessage());
			}
		}
		super.onTerminating();
	}

	@Override
	protected void execute(final ExecutionTrace executionTrace) {
		this.logger.debug("size {}", executionTrace.getLength());
		final String data = executionTrace.toString();
		this.printStream.println(data);
		this.printStream.flush();
		this.reportSuccess(executionTrace.getTraceId());
	}

}
