/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.source.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import kieker.analysis.source.IAccessHandler;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.JsonArrayValueDeserializer;

import fi.iki.elonen.NanoHTTPD;

/**
 * Rest service handler for Kieker based on NanoHTTPD.
 *
 * @author Reiner Jung
 *
 * @sincd 1.15
 */
public class RestService extends NanoHTTPD {

	private static final String KIEKER_PATH = "kieker";

	private static Logger LOGGER = LoggerFactory.getLogger(RestService.class.getCanonicalName());

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final RestServiceStage stage;

	private final IAccessHandler accessRestrictionHandler;

	/**
	 * Create a new rest service.
	 *
	 * @param stage
	 *            stage handling the deserialized event.
	 * @param hostname
	 *            hostname to listen for when accessed (aka virtual host name); can be null
	 * @param port
	 *            port to listen on
	 * @param accessRestrictionHandler
	 */
	public RestService(final RestServiceStage stage, final String hostname, final int port, final IAccessHandler accessRestrictionHandler) {
		super(hostname, port);
		this.stage = stage;
		this.accessRestrictionHandler = accessRestrictionHandler;
	}

	@Override
	public Response serve(final IHTTPSession session) {
		if (this.accessRestrictionHandler.acceptRemoteIpAddress(session.getRemoteIpAddress())) {
			final Method method = session.getMethod();

			if (Method.PUT.equals(method) || Method.POST.equals(method)) {
				/** handle put and post requests. */
				final String path = URI.create(session.getUri()).getPath();
				if (KIEKER_PATH.equals(path)) {
					return this.handlePutRequest(session);
				} else {
					return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "URI path '" + path + "'not recognized");
				}
			} else {
				return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "PUT request expected");
			}
		} else {
			return NanoHTTPD.newFixedLengthResponse(Response.Status.UNAUTHORIZED, MIME_PLAINTEXT, "Access denied");
		}
	}

	/**
	 * Handle record put request.
	 *
	 * @param session
	 *            http session
	 * @return returns a response object
	 */
	private Response handlePutRequest(final IHTTPSession session) {
		try {
			final Integer size = Integer.valueOf(session.getHeaders().get("content-length"));

			if (size != null) {
				final byte[] buffer = new byte[size];
				final InputStream stream = session.getInputStream();
				stream.read(buffer, 0, size);

				final String contentType = session.getHeaders().get("content-type");

				if ("application/json".equals(contentType)) {
					return this.processJsonRequest(buffer);
				} else {
					return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "JSON data expected");
				}
			} else {
				return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "JSON data expected");
			}
		} catch (final IOException e) {
			return NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "IO exception");
		}
	}

	private Response processJsonRequest(final byte[] buffer) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(buffer);
			final JsonNode object = mapper.readTree(buffer);
			if (object.getNodeType() == JsonNodeType.ARRAY) {
				return this.processJsonArray((ArrayNode) object);
			} else {
				return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "JSON array expected");
			}
		} catch (final IOException e) {
			LOGGER.error("Parsing error for JSON message: {}", new String(buffer));
			return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Malformed JSON data");
		}
	}

	private Response processJsonArray(final ArrayNode arrayNode) {
		final JsonArrayValueDeserializer deserializer = JsonArrayValueDeserializer.create(arrayNode);
		final String eventClassName = deserializer.getString();

		/** read class type. */
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(eventClassName);
		try {
			final IMonitoringRecord event = recordFactory.create(deserializer);
			this.stage.getOutputPort().send(event);
			return NanoHTTPD.newFixedLengthResponse(Response.Status.ACCEPTED, MIME_PLAINTEXT, "");
		} catch (final RecordInstantiationException ex) {
			LOGGER.error("Failed to create {}: {}", eventClassName, ex.getLocalizedMessage());
			return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Malformed data");
		}
	}
}
