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
package kieker.analysis.sink;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.util.bookstore.BookstoreExecutionFactory;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.exceptions.InvalidTraceException;

import teetime.framework.test.StageTester;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class EquivalenceClassWriterTest { // NOCS test do not need constructors

	@Test
	public void test() throws IOException, InvalidTraceException {
		final File outputFile = File.createTempFile("equivalence-class-writer", "txt");
		final EquivalenceClassWriter writer = new EquivalenceClassWriter(outputFile);

		final SystemModelRepository repository = new SystemModelRepository();
		final BookstoreExecutionFactory factory = new BookstoreExecutionFactory(repository);

		final ExecutionTrace trace = new ExecutionTrace(1);
		trace.add(factory.createBookstoreExecution_exec0_0__bookstore_searchBook(1, "SESSION", "HOST", 1, 100, false));

		final Map<ExecutionTrace, Integer> traceMap = new HashMap<>();
		traceMap.put(trace, 2);

		StageTester.test(writer).and()
				.send(traceMap).to(writer.getInputPort()).and()
				.start();

		Assert.assertTrue("File does not exist", outputFile.exists());
	}

}
