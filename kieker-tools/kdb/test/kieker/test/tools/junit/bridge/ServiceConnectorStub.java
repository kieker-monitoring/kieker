/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.tools.junit.bridge;

import org.junit.Assert;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.IServiceConnector;

/**
 * Simulates a connector to test the use of the API by the {@link kieker.tools.bridge.ServiceContainer}.
 * 
 * @author Pascale Brandt
 * @since 1.8
 */

public class ServiceConnectorStub implements IServiceConnector {

	private int count = 0; // NOPMD

	private boolean initialize = false; // NOPMD
	private boolean close = false; // NOPMD

	/**
	 * Construct the test connector.
	 */
	public ServiceConnectorStub() {} // NOPMD

	/**
	 * The assertions check whether the method is called after initialize() and beforE() close.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             never, this is just API compatibility
	 * @throws ConnectorEndOfDataException
	 *             when called more than END_NUMBER_OF_RECORDS times.
	 * @return Returns an IMontoringRecord.
	 */
	@Override
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		Assert.assertTrue("Connector's deserializeNextRecord() method called before initialize() was called.", this.initialize);
		Assert.assertFalse("Connector's deserializeNextRecord() method called after close() was called.", this.close);
		if (this.count < ConfigurationParameters.SEND_NUMBER_OF_RECORDS) {
			this.count++;
			return new OperationExecutionRecord("kieker.bridge", OperationExecutionRecord.NO_SESSION_ID, 1, 0, 0,
					OperationExecutionRecord.NO_HOSTNAME, OperationExecutionRecord.NO_EOI_ESS,
					OperationExecutionRecord.NO_EOI_ESS);
		} else {
			throw new ConnectorEndOfDataException("End of data reached");
		}
	}

	/**
	 * Initialize the connector. The assertions checks whether initialize() is called in the right order.
	 * Meaning it is called first, or after a close() call.
	 * If not, the assertion throws an AssertionError.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             never, this is just API compatibility
	 */
	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		// assert that initialize is false
		Assert.assertFalse("Connector's initialize() method was called more than once.", this.initialize);
		this.initialize = true;
		this.close = false;
	}

	/**
	 * Close the connector. The assertions checks whether close() is called in the right order.
	 * Meaning it is called after initialize() and then only once.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             never, this is just API compatibility
	 */
	@Override
	public void close() throws ConnectorDataTransmissionException {
		Assert.assertTrue("Connector's close() method was called before initialite() was called.", this.initialize);
		Assert.assertTrue("Connector's close() method was called more than once.", !this.close);
	}

}
