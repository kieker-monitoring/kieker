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
package kieker.analysis.generic.sink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import kieker.model.system.model.ExecutionTrace;

import teetime.framework.AbstractConsumerStage;

/**
 * Write trace equivalence class map to a file.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class EquivalenceClassWriter extends AbstractConsumerStage<Map<ExecutionTrace, Integer>> {

	private static final String ENCODING = "UTF-8";

	private final File outputFile;

	public EquivalenceClassWriter(final File outputFile) {
		this.outputFile = outputFile;
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminatiing {}", this.getClass().getCanonicalName());
		super.onTerminating();
	}

	@Override
	protected void execute(final Map<ExecutionTrace, Integer> element) throws Exception {
		try (PrintStream ps = new PrintStream(Files.newOutputStream(this.outputFile.toPath()), false, ENCODING)) {
			int numClasses = 0;
			for (final Entry<ExecutionTrace, Integer> e : element.entrySet()) {
				final ExecutionTrace t = e.getKey();
				ps.printf("Class %d; cardinality: %d; # executions: %d; representative: %d; max. stack depth: %d\n",
						numClasses++, e.getValue(), t.getLength(), t.getTraceId(), t.getMaxEss());
			}
			this.logger.debug("");
			this.logger.debug("#");
			this.logger.debug("# Plugin: Trace equivalence report");
			this.logger.debug("Wrote {} equivalence class{} to file '{}'", numClasses,
					(numClasses > 1 ? "es" : ""), this.outputFile.getCanonicalFile()); // NOCS
			ps.close();
		} catch (final FileNotFoundException e) {
			this.logger.error("File not found: {}", this.outputFile.toPath().toString());
		}
	}

}
