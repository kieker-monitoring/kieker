/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.probe.cxf.executions;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Assert;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestOutInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor;

/**
 * Tests the CXF interceptors with no Session ID being registered before.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public class TestCXFClientServerInterceptorsNoSessionRegisteredBefore extends AbstractTestCXFClientServerInterceptors {

	/**
	 * Default constructor.
	 */
	public TestCXFClientServerInterceptorsNoSessionRegisteredBefore() {
		// empty default constructor
	}

	@Override
	protected void beforeRequest() {
		// do nothing
	}

	@Override
	protected void afterRequest() {
		// do nothing
	}

	@Override
	protected void checkRecordList(final List<IMonitoringRecord> records) {
		Assert.assertFalse("No records in List", records.isEmpty());
		/**
		 * We expect the records in the following order:
		 * <ol>
		 * <li>1. The record produced on the server side (eoi,ess = 1,1)
		 * <li>2. The record produced on the client side (eoi,ess = 0,0)
		 * </ol>
		 */

		// The records were inserted asynchronously.
		// Therefore, the server record (...ResponseOutInterceptor) can either be at index 0 or 1.
		int index;
		final OperationExecutionRecord record = (OperationExecutionRecord) records.get(0);
		if (record.getOperationSignature().equals(OperationExecutionSOAPResponseOutInterceptor.SIGNATURE)) {
			index = 0;
		} else {
			index = 1;
		}

		// first record
		OperationExecutionRecord recordFromServerSide = (OperationExecutionRecord) records.get(index % 2);
		Assert.assertEquals("Unexpected eoi", 1, recordFromServerSide.getEoi());
		Assert.assertEquals("Unexpected ess", 1, recordFromServerSide.getEss());
		Assert.assertEquals("Unexpected hostname", SERVER_HOSTNAME, recordFromServerSide.getHostname());
		assertThat(recordFromServerSide.getOperationSignature(),
				is(equalTo(OperationExecutionSOAPResponseOutInterceptor.SIGNATURE)));
		assertThat(recordFromServerSide.getTraceId(), is(not(equalTo(OperationExecutionRecord.NO_TRACE_ID))));
		assertThat(recordFromServerSide.getSessionId(),
				is(equalTo(OperationExecutionSOAPRequestOutInterceptor.SESSION_ID_ASYNC_TRACE)));

		final long traceId = recordFromServerSide.getTraceId();

		// second record
		OperationExecutionRecord recordFromClientSide = (OperationExecutionRecord) records.get((index + 1) % 2);
		Assert.assertEquals("Unexpected eoi", 0, recordFromClientSide.getEoi());
		Assert.assertEquals("Unexpected ess", 0, recordFromClientSide.getEss());
		Assert.assertEquals("Unexpected hostname", CLIENT_HOSTNAME, recordFromClientSide.getHostname());
		Assert.assertEquals("Unexpected signature", OperationExecutionSOAPResponseInInterceptor.SIGNATURE,
				recordFromClientSide.getOperationSignature());
		Assert.assertEquals("Unexpected traceId", traceId, recordFromClientSide.getTraceId());
		assertThat(recordFromClientSide.getSessionId(),
				is(equalTo(OperationExecutionSOAPRequestOutInterceptor.SESSION_ID_ASYNC_TRACE)));

		assertThat(records.size(), is(2));
	}

	@Override
	protected int getPortDigit() {
		return 1;
	}

}
