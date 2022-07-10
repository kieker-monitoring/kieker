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

package kieker.test.tools.junit.trace.analysis.filter.traceWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.analysis.util.StringUtils;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.trace.analysis.systemModel.ExecutionTrace;
import kieker.tools.trace.analysis.systemModel.InvalidExecutionTrace;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.bookstore.BookstoreExecutionFactory;

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public abstract class AbstractTraceWriterFilterTest extends AbstractKiekerTest {

	/** This constant contains the line separator for the current system. */
	protected static final String SYSTEM_NEWLINE_STRING = System.getProperty("line.separator");

	private static final String OUTPUT_BASE_FN = "NLdQ3wsS.out"; // the name doesn't matter

	private static final String SESSION_ID = "Kzx7Gd5zMF"; // the value doesn't matter
	private static final String HOSTNAME = "srv-FtfN0uwban"; // the value doesn't matter

	private static final long TRACE_ID_VALID_EXEC_TRACE = 2342;
	private static final long TRACE_ID_INVALID_EXEC_TRACE = 2342;
	private static final long TRACE_ID_VALID_MSG_TRACE = 2342;

	private static final int INITIAL_TIMESTAMP_VALID_EXEC_TRACE = 300;
	private static final int INITIAL_TIMESTAMP_INVALID_EXEC_TRACE = AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_EXEC_TRACE + 300;
	private static final int INITIAL_TIMESTAMP_VALID_MESSAGE_TRACE = AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_INVALID_EXEC_TRACE + 300;

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private final AnalysisController analysisController = new AnalysisController();
	private final SystemModelRepository modelRepo = new SystemModelRepository(new Configuration(), this.analysisController);

	private final BookstoreExecutionFactory execFactory = new BookstoreExecutionFactory(this.modelRepo);

	private volatile File outputFile = null; // NOPMD (init for fb)

	/**
	 * Initializes the test setup.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.tmpFolder.create();
		this.outputFile = this.tmpFolder.newFile(AbstractTraceWriterFilterTest.OUTPUT_BASE_FN);
	}

	protected abstract AbstractTraceProcessingFilter provideWriterFilter(String filename, AnalysisController ctrl) throws IOException;

	protected abstract String provideFilterInputName();

	protected abstract String provideExpectedFileContent(List<Object> loggedEvents);

	private ExecutionTrace createValidExecutionTrace() throws InvalidTraceException {
		final ExecutionTrace execTrace = new ExecutionTrace(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE);
		execTrace.add(this.execFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE,
				AbstractTraceWriterFilterTest.SESSION_ID, AbstractTraceWriterFilterTest.HOSTNAME,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				false)); // assumed
		execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION); // just to make sure this trace is really valid
		return execTrace;
	}

	private ExecutionTrace createExecutionTraceForValidMessageTrace() throws InvalidTraceException {
		final ExecutionTrace execTrace = new ExecutionTrace(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE);
		execTrace.add(this.execFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(AbstractTraceWriterFilterTest.TRACE_ID_VALID_MSG_TRACE,
				AbstractTraceWriterFilterTest.SESSION_ID, AbstractTraceWriterFilterTest.HOSTNAME,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_MESSAGE_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_MESSAGE_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				false)); // assumed
		execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION); // just to make sure this trace is really valid
		return execTrace;
	}

	private InvalidExecutionTrace createInvalidExecutionTrace() throws InvalidTraceException {
		final ExecutionTrace execTrace = new ExecutionTrace(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE);
		execTrace.add(this.execFactory.createBookstoreExecution_exec1_1__catalog_getBook(AbstractTraceWriterFilterTest.TRACE_ID_INVALID_EXEC_TRACE,
				AbstractTraceWriterFilterTest.SESSION_ID, AbstractTraceWriterFilterTest.HOSTNAME,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_INVALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_INVALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook,
				false)); // assumed

		try {
			execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
			Assert.fail("Test invalid: wanted to create an *invalid* trace");
		} catch (final InvalidTraceException e) { // NOPMD (EmptyCatchBlock)
			// that's what we expect here
		}
		return new InvalidExecutionTrace(execTrace);
	}

	private List<Object> createTraces() throws InvalidTraceException {
		final List<Object> traces = new ArrayList<>(3);
		traces.add(this.createValidExecutionTrace());
		traces.add(this.createInvalidExecutionTrace());
		traces.add(this.createExecutionTraceForValidMessageTrace().toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
		return traces;
	}

	@Test
	public void testIt() throws Exception {
		final AbstractTraceProcessingFilter filter = this.provideWriterFilter(this.outputFile.getAbsolutePath(), this.analysisController);

		final ListReader<Object> reader = new ListReader<>(new Configuration(), this.analysisController);
		final List<Object> eventList = this.createTraces();
		reader.addAllObjects(eventList);

		this.analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, this.provideFilterInputName());
		this.analysisController.connect(filter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.modelRepo);
		this.analysisController.run();

		final String actualFileContent = StringUtils.readOutputFileAsString(this.outputFile);
		final String expectedFileContent = this.provideExpectedFileContent(eventList);
		Assert.assertEquals("Unexpected file content", expectedFileContent, actualFileContent);
	}

	@After
	public void tearDown() throws Exception {
		this.tmpFolder.delete();
	}
}
