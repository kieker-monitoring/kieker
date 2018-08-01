/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.bridge.connector.http;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.jvm.UptimeRecord;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.http.HTTPConnector;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.11
 */
public class HTTPConnectorTest extends AbstractKiekerTest {

	private static final String CONTEXT = "";
	private static final String PORT = "8585";
	private static final String REST_URL = "/kieker/rest/record";
	private static final String FULL_URL = "http://localhost:" + PORT + CONTEXT + REST_URL;

	private HTTPConnector connector;
	private HttpClient client;

	public HTTPConnectorTest() {
		// No code necessary
	}

	@Before
	public void setUp() throws Exception {
		// Initialize connector
		final Configuration configuration = new Configuration();
		configuration.setProperty(HTTPConnector.CONTEXT, CONTEXT);
		configuration.setProperty(HTTPConnector.PORT, PORT);
		configuration.setProperty(HTTPConnector.REST_URL, REST_URL);

		this.connector = new HTTPConnector(configuration, new ConcurrentHashMap<Integer, LookupEntity>());
		this.connector.initialize();

		// Initialize client
		this.client = new HttpClient();
		this.client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		this.client.start();
	}

	@After
	public void tearDown() throws ConnectorDataTransmissionException {
		this.connector.close();
	}

	@Test
	public void testValidRecord() throws IOException, InterruptedException, ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// Send the record and make sure that the returned codes are as expected
		final String jsonString = "{\"class\" : \"kieker.common.record.jvm.UptimeRecord\", \"timestamp\" : \"42\", \"values\" : [\"1\", \"SE\", \"VM\", \"50\"]}";

		final ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(FULL_URL);
		exchange.setRequestContent(new ByteArrayBuffer(jsonString.getBytes("UTF-8")));

		this.client.send(exchange);

		final int exchangeState = exchange.waitForDone();

		Assert.assertEquals(HttpExchange.STATUS_COMPLETED, exchangeState);
		Assert.assertEquals(200, exchange.getResponseStatus());

		// Now get the received record and verify it
		final IMonitoringRecord receivedRecord = this.connector.deserializeNextRecord();
		Assert.assertTrue(receivedRecord instanceof UptimeRecord);
		Assert.assertEquals(42, ((UptimeRecord) receivedRecord).getLoggingTimestamp());
		Assert.assertEquals(1, ((UptimeRecord) receivedRecord).getTimestamp());
		Assert.assertEquals(50, ((UptimeRecord) receivedRecord).getUptimeMS());
		Assert.assertEquals("SE", ((UptimeRecord) receivedRecord).getHostname());
		Assert.assertEquals("VM", ((UptimeRecord) receivedRecord).getVmName());
	}

	@Test
	public void testNonExistingClass() throws IOException, InterruptedException, ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// Send the record and make sure that the returned codes are as expected
		final String jsonString = "{\"class\" : \"nonExistingClass\", \"timestamp\" : \"42\", \"values\" : [\"42\"]}";

		final ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(FULL_URL);
		exchange.setRequestContent(new ByteArrayBuffer(jsonString.getBytes("UTF-8")));

		this.client.send(exchange);

		final int exchangeState = exchange.waitForDone();

		Assert.assertEquals(HttpExchange.STATUS_COMPLETED, exchangeState);
		Assert.assertEquals(400, exchange.getResponseStatus());
	}

	@Test
	public void testMalformedJSON() throws IOException, InterruptedException, ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// Send the record and make sure that the returned codes are as expected
		final String jsonString = "{\"class\" : \"kieker.common.record.jvm.UptimeRecord\"";

		final ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(FULL_URL);
		exchange.setRequestContent(new ByteArrayBuffer(jsonString.getBytes("UTF-8")));

		this.client.send(exchange);

		final int exchangeState = exchange.waitForDone();

		Assert.assertEquals(HttpExchange.STATUS_COMPLETED, exchangeState);
		Assert.assertEquals(400, exchange.getResponseStatus());
	}

	@Test
	public void testInsufficientValues() throws IOException, InterruptedException, ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// Send the record and make sure that the returned codes are as expected
		final String jsonString = "{\"class\" : \"kieker.common.record.jvm.UptimeRecord\", \"timestamp\" : \"42\", \"values\" : [\"1\", \"SE\", \"VM\"]}";

		final ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(FULL_URL);
		exchange.setRequestContent(new ByteArrayBuffer(jsonString.getBytes("UTF-8")));

		this.client.send(exchange);

		final int exchangeState = exchange.waitForDone();

		Assert.assertEquals(HttpExchange.STATUS_COMPLETED, exchangeState);
		Assert.assertEquals(400, exchange.getResponseStatus());
	}

	@Test
	public void testMissingTimestamp() throws IOException, InterruptedException, ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// Send the record and make sure that the returned codes are as expected
		final String jsonString = "{\"class\" : \"kieker.common.record.jvm.UptimeRecord\", \"values\" : [\"1\", \"SE\", \"VM\", \"50\"]}";

		final ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(FULL_URL);
		exchange.setRequestContent(new ByteArrayBuffer(jsonString.getBytes("UTF-8")));

		this.client.send(exchange);

		final int exchangeState = exchange.waitForDone();

		Assert.assertEquals(HttpExchange.STATUS_COMPLETED, exchangeState);
		Assert.assertEquals(400, exchange.getResponseStatus());
	}

	@Test
	public void testMissingValues() throws IOException, InterruptedException, ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// Send the record and make sure that the returned codes are as expected
		final String jsonString = "{\"class\" : \"kieker.common.record.jvm.UptimeRecord\", \"timestamp\" : \"42\"}";

		final ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(FULL_URL);
		exchange.setRequestContent(new ByteArrayBuffer(jsonString.getBytes("UTF-8")));

		this.client.send(exchange);

		final int exchangeState = exchange.waitForDone();

		Assert.assertEquals(HttpExchange.STATUS_COMPLETED, exchangeState);
		Assert.assertEquals(400, exchange.getResponseStatus());
	}

	@Test
	public void testMissingClass() throws IOException, InterruptedException, ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// Send the record and make sure that the returned codes are as expected
		final String jsonString = "{\"timestamp\" : \"42\", \"values\" : [\"1\", \"SE\", \"VM\", \"50\"]}";

		final ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(FULL_URL);
		exchange.setRequestContent(new ByteArrayBuffer(jsonString.getBytes("UTF-8")));

		this.client.send(exchange);

		final int exchangeState = exchange.waitForDone();

		Assert.assertEquals(HttpExchange.STATUS_COMPLETED, exchangeState);
		Assert.assertEquals(400, exchange.getResponseStatus());
	}

}
