/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.tools.junit.traceAnalysis.filter.traceWriter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.test.analysis.junit.plugin.ListReader;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreExecutionFactory;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractTraceWriterFilterTest {

	protected static final String SYSTEM_NEWLINE_STRING = System.getProperty("line.separator");

	private static final String OUTPUT_BASE_FN = "NLdQ3wsS.out"; // the name doesn't matter
	private static final String ENCODING = "UTF-8";

	private static final String SESSION_ID = "Kzx7Gd5zMF"; // the value doesn't matter
	private static final String HOSTNAME = "srv-FtfN0uwban"; // the value doesn't matter

	private static final long TRACE_ID_VALID_EXEC_TRACE = 2342;
	private static final long TRACE_ID_INVALID_EXEC_TRACE = 2342;
	private static final long TRACE_ID_VALID_MSG_TRACE = 2342;

	private static final int INITIAL_TIMESTAMP_VALID_EXEC_TRACE = 300;
	private static final int INITIAL_TIMESTAMP_INVALID_EXEC_TRACE = AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_EXEC_TRACE + 300;
	private static final int INITIAL_TIMESTAMP_VALID_MESSAGE_TRACE = AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_INVALID_EXEC_TRACE + 300;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private final SystemModelRepository modelRepo = new SystemModelRepository(new Configuration());

	private final BookstoreExecutionFactory execFactory = new BookstoreExecutionFactory(this.modelRepo);

	private volatile File outputFile = null;

	@Before
	public void setUp() throws Exception {
		this.tmpFolder.create();
		this.outputFile = this.tmpFolder.newFile(AbstractTraceWriterFilterTest.OUTPUT_BASE_FN);
	}

	protected abstract AbstractTraceProcessingFilter provideWriterFilter(String filename) throws IOException;

	protected abstract String provideFilterInputName();

	protected abstract String provideExpectedFileContent(List<Object> loggedEvents);

	private ExecutionTrace createValidExecutionTrace() throws InvalidTraceException {
		final ExecutionTrace execTrace = new ExecutionTrace(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE);
		execTrace.add(this.execFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE,
				AbstractTraceWriterFilterTest.SESSION_ID, AbstractTraceWriterFilterTest.HOSTNAME,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));
		execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION); // just to make sure this trace is really valid
		return execTrace;
	}

	private ExecutionTrace createExecutionTraceForValidMessageTrace() throws InvalidTraceException {
		final ExecutionTrace execTrace = new ExecutionTrace(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE);
		execTrace.add(this.execFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(AbstractTraceWriterFilterTest.TRACE_ID_VALID_MSG_TRACE,
				AbstractTraceWriterFilterTest.SESSION_ID, AbstractTraceWriterFilterTest.HOSTNAME,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_MESSAGE_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_VALID_MESSAGE_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));
		execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION); // just to make sure this trace is really valid
		return execTrace;
	}

	private InvalidExecutionTrace createInvalidExecutionTrace() {
		final ExecutionTrace execTrace = new ExecutionTrace(AbstractTraceWriterFilterTest.TRACE_ID_VALID_EXEC_TRACE);
		try {
			execTrace.add(this.execFactory.createBookstoreExecution_exec1_1__catalog_getBook(AbstractTraceWriterFilterTest.TRACE_ID_INVALID_EXEC_TRACE,
					AbstractTraceWriterFilterTest.SESSION_ID, AbstractTraceWriterFilterTest.HOSTNAME,
					AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_INVALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
					AbstractTraceWriterFilterTest.INITIAL_TIMESTAMP_INVALID_EXEC_TRACE + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook));
		} catch (final InvalidTraceException e) { // this must not happen here
			Assert.fail("Test invalid: failed to add record");
		}

		try {
			execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
			Assert.fail("Test invalid: wanted to create an *invalid* trace");
		} catch (final InvalidTraceException e) { // NOPMD (EmptyCatchBlock)
			/* that's what we expect here */
		}
		return new InvalidExecutionTrace(execTrace);
	}

	private List<Object> createTraces() throws InvalidTraceException {
		final List<Object> traces = new ArrayList<Object>(3);
		traces.add(this.createValidExecutionTrace());
		traces.add(this.createInvalidExecutionTrace());
		traces.add(this.createExecutionTraceForValidMessageTrace().toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
		return traces;
	}

	@Test
	public void testIt() throws Exception {
		final AbstractTraceProcessingFilter filter = this.provideWriterFilter(this.outputFile.getAbsolutePath());

		final ListReader reader = new ListReader(new Configuration());
		final List<Object> eventList = this.createTraces();
		reader.addAllObjects(eventList);

		final AnalysisController analysisController = new AnalysisController();
		analysisController.registerFilter(filter);
		analysisController.registerReader(reader);
		analysisController.registerRepository(this.modelRepo);
		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, this.provideFilterInputName());
		analysisController.connect(filter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.modelRepo);
		analysisController.run();

		final String actualFileContent = this.readOutputFileAsString();
		final String expectedFileContent = this.provideExpectedFileContent(eventList);
		Assert.assertEquals("Unexpected file content", expectedFileContent, actualFileContent);
	}

	private String readOutputFileAsString() throws java.io.IOException {
		final byte[] buffer = new byte[(int) this.outputFile.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(this.outputFile));
			if (f.read(buffer) == -1) {
				Assert.fail("Failed to read file into buffer: " + this.outputFile.getAbsolutePath());
			}
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (final IOException ignored) {
					Assert.fail("Failed to close stream for file " + this.outputFile.getAbsolutePath());
				}
			}
		}
		return new String(buffer, AbstractTraceWriterFilterTest.ENCODING);
	}

	@After
	public void tearDown() throws Exception {
		this.tmpFolder.delete();
	}
}
