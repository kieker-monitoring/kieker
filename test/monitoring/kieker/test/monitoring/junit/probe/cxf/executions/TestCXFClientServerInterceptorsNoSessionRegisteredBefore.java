package kieker.test.monitoring.junit.probe.cxf.executions;

import java.util.List;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestOutInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor;

import org.junit.Assert;

/**
 * Tests the CXF interceptors with no Session ID being registered before.
 * 
 * @author Andre van Hoorn
 *
 */
public class TestCXFClientServerInterceptorsNoSessionRegisteredBefore extends AbstractTestCXFClientServerInterceptors {
	
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
		 * 1. The record produced on the server side (eoi,ess = 1,1)
		 * 1. The record produced on the client side (eoi,ess = 0,0)
		 */
		int curIdx = -1;
		long traceId = OperationExecutionRecord.NO_TRACEID;
		String sessionID = OperationExecutionRecord.NO_SESSION_ID;
		for (final IMonitoringRecord record : records)  {
			final OperationExecutionRecord opRec = (OperationExecutionRecord) record;
			curIdx+=1;
			switch (curIdx) {
			case 0:	Assert.assertEquals("Unexpected eoi", 1, opRec.getEoi());
					Assert.assertEquals("Unexpected ess", 1, opRec.getEss());
					Assert.assertEquals("Unexpected hostname", SERVER_HOSTNAME, opRec.getHostname());
					Assert.assertEquals("Unexpected signature", OperationExecutionSOAPResponseOutInterceptor.SIGNATURE, opRec.getOperationSignature());
					traceId = opRec.getTraceId();
					Assert.assertTrue("Unexpected traceId: " + traceId, traceId != OperationExecutionRecord.NO_TRACEID);
					sessionID = opRec.getSessionId();
					Assert.assertFalse("Unexpected session ID: " + sessionID, OperationExecutionRecord.NO_SESSION_ID.equals(sessionID)); // do not use == here, because of SOAP transformations
					break;
			case 1: Assert.assertEquals("Unexpected eoi", 0, opRec.getEoi());
					Assert.assertEquals("Unexpected ess", 0, opRec.getEss());
					Assert.assertEquals("Unexpected hostname", CLIENT_HOSTNAME, opRec.getHostname());
					Assert.assertEquals("Unexpected signature", OperationExecutionSOAPResponseInInterceptor.SIGNATURE, opRec.getOperationSignature());
					Assert.assertEquals("Unexpected traceId", traceId, opRec.getTraceId());
					Assert.assertEquals("Unexpected session ID", OperationExecutionSOAPRequestOutInterceptor.SESSION_ID_ASYNC_TRACE, opRec.getSessionId());
					break;
			default: Assert.fail("Unexpected record" + opRec);
			}
		}		
	}

}
