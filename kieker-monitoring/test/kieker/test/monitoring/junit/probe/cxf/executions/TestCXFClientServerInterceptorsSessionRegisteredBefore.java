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

import java.util.List;

import org.junit.Assert;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor;

/**
 * Tests the CXF interceptors with a previously registered Session ID.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public class TestCXFClientServerInterceptorsSessionRegisteredBefore extends AbstractTestCXFClientServerInterceptors {
	private static final String SESSION_ID = "SESSION-KQ8wFM0U";

	/**
	 * Default constructor.
	 */
	public TestCXFClientServerInterceptorsSessionRegisteredBefore() {
		// empty default constructor
	}

	@Override
	protected void beforeRequest() {
		SESSION_REGISTRY.storeThreadLocalSessionId(SESSION_ID);
	}

	@Override
	protected void afterRequest() {
		SESSION_REGISTRY.unsetThreadLocalSessionId();
	}

	@Override
	protected void checkRecordList(final List<IMonitoringRecord> records) {
		Assert.assertFalse("No records in List", records.isEmpty());
		/**
		 * We expect the records in the following order:
		 * 1. The record produced on the server side (eoi,ess = 1,1)
		 * 1. The record produced on the client side (eoi,ess = 0,0)
		 */
		int curIdx = -1;
		long traceId = OperationExecutionRecord.NO_TRACE_ID;
		String sessionID = OperationExecutionRecord.NO_SESSION_ID;
		for (final IMonitoringRecord record : records) {
			final OperationExecutionRecord opRec = (OperationExecutionRecord) record;
			curIdx += 1;
			switch (curIdx) {
			case 0:
				Assert.assertEquals("Unexpected eoi", 1, opRec.getEoi());
				Assert.assertEquals("Unexpected ess", 1, opRec.getEss());
				Assert.assertEquals("Unexpected hostname", SERVER_HOSTNAME, opRec.getHostname());
				Assert.assertEquals("Unexpected signature", OperationExecutionSOAPResponseOutInterceptor.SIGNATURE, opRec.getOperationSignature());
				traceId = opRec.getTraceId();
				Assert.assertTrue("Unexpected traceId: " + traceId, traceId != OperationExecutionRecord.NO_TRACE_ID);
				sessionID = opRec.getSessionId();
				Assert.assertFalse("Unexpected session ID: " + sessionID, OperationExecutionRecord.NO_SESSION_ID.equals(sessionID)); // do not use == here, because
																																		// of SOAP transformations
				break;
			case 1:
				Assert.assertEquals("Unexpected eoi", 0, opRec.getEoi());
				Assert.assertEquals("Unexpected ess", 0, opRec.getEss());
				Assert.assertEquals("Unexpected hostname", CLIENT_HOSTNAME, opRec.getHostname());
				Assert.assertEquals("Unexpected signature", OperationExecutionSOAPResponseInInterceptor.SIGNATURE, opRec.getOperationSignature());
				Assert.assertEquals("Unexpected traceId", traceId, opRec.getTraceId());
				Assert.assertEquals("Unexpected session ID", SESSION_ID, opRec.getSessionId());
				break;
			default:
				Assert.fail("Unexpected record" + opRec);
			}
		}
	}

	@Override
	protected int getPortDigit() {
		return 2;
	}

}
