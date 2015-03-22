/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.bridge.connector.http;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ajax.JSON;

import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.AbstractConnector;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.ConnectorProperty;

/**
 * A connector receiving records via a REST URL.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.11
 */
@ConnectorProperty(cmdName = "http-rest", name = "REST/HTTP Connector", description = "HTTP Client to receive records via a REST URL.")
public final class HTTPConnector extends AbstractConnector {

	public static final String PORT = HTTPConnector.class.getCanonicalName() + ".port";
	public static final String CONTEXT = HTTPConnector.class.getCanonicalName() + ".context";
	public static final String REST_URL = HTTPConnector.class.getCanonicalName() + ".restURL";

	private final BlockingQueue<IMonitoringRecord> recordQueue = new LinkedBlockingQueue<IMonitoringRecord>();

	private final int port;
	private final String context;
	private final String restURL;

	private Server server;

	public HTTPConnector(final Configuration configuration, final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
		super(configuration, lookupEntityMap);

		this.port = configuration.getIntProperty(PORT);
		this.context = configuration.getStringProperty(CONTEXT);
		this.restURL = configuration.getStringProperty(REST_URL);
	}

	@Override
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			return this.recordQueue.take();
		} catch (final InterruptedException ex) {
			throw new ConnectorDataTransmissionException("Connector has been interrupted", ex);
		}
	}

	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		try {
			this.server = new Server(this.port);

			final Servlet servlet = new MonitoringReceiverServlet(this.recordQueue);
			final ServletHolder servletHolder = new ServletHolder(servlet);

			final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			contextHandler.setContextPath(this.context);
			contextHandler.addServlet(servletHolder, this.restURL);

			this.server.setHandler(contextHandler);
			this.server.start();
		} catch (final Exception ex) { // NOPMD NOCS (Jetty does that)
			throw new ConnectorDataTransmissionException("Could not start server", ex);
		}
	}

	@Override
	public void close() throws ConnectorDataTransmissionException {
		try {
			this.server.stop();
			this.server.join();
		} catch (final Exception ex) { // NOPMD NOCS (Jetty does that)
			throw new ConnectorDataTransmissionException("Could not stop server", ex);
		}
	}

	/**
	 * A helper class which is responsible for the actual monitoring record receiving via HTTP.
	 *
	 * @author Nils Christian Ehmke
	 *
	 * @since 1.11
	 */
	private static class MonitoringReceiverServlet extends HttpServlet {

		private static final Log LOG = LogFactory.getLog(MonitoringReceiverServlet.class);
		private static final long serialVersionUID = 1L;

		private final BlockingQueue<IMonitoringRecord> recordQueue;

		public MonitoringReceiverServlet(final BlockingQueue<IMonitoringRecord> recordQueue) {
			this.recordQueue = recordQueue;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
			try {
				// Read the JSON object from the request
				final Reader requestReader = request.getReader();

				final Map<String, Object> jsonObject = (Map<String, Object>) JSON.parse(requestReader);

				// Extract the delivered values
				final String classname = (String) jsonObject.get("class");
				final String rawTimestamp = (String) jsonObject.get("timestamp");
				final Object[] rawValues = (Object[]) jsonObject.get("values");

				if ((classname == null) || (rawTimestamp == null) || (rawValues == null)) {
					LOG.warn("Invalid data received");
					response.sendError(400, "Invalid data received");
					return;
				}

				final long timestamp = Integer.parseInt(rawTimestamp);
				final String[] values = Arrays.copyOf(rawValues, rawValues.length, String[].class);

				// Try to deserialize a monitoring record from the given values
				final Class<? extends IMonitoringRecord> clazz = AbstractMonitoringRecord.classForName(classname);

				final IMonitoringRecord record = AbstractMonitoringRecord.createFromStringArray(clazz, values);
				record.setLoggingTimestamp(timestamp);
				this.recordQueue.add(record);
			} catch (final ClassCastException ex) {
				LOG.warn("Invalid data received", ex);
				response.sendError(400, "Invalid data received");
			} catch (final ArrayStoreException ex) {
				LOG.warn("Invalid data received", ex);
				response.sendError(400, "Invalid data received");
			} catch (final NumberFormatException ex) {
				LOG.warn("Invalid data received", ex);
				response.sendError(400, "Invalid data received");
			} catch (final MonitoringRecordException ex) {
				LOG.warn("Could not deserialize monitoring record", ex);
				response.sendError(400, "Could not deserialize monitoring record");
			} catch (final IllegalStateException ex) {
				LOG.warn("Invalid data received", ex);
				response.sendError(400, "Invalid data received");
			}
		}
	}

}
