/***************************************************************************
 * Copyright (C) 2026 Kieker Project (https://kieker-monitoring.net)
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

package kieker.tools.oteltransformer.stages;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import kieker.common.configuration.Configuration;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.semconv.ServiceAttributes;

/**
 * The resolved settings of the {@link OpenTelemetryExporterStage}.
 *
 * @author Malte Hansen
 */
public final class ExporterSettings {

	/** Environment variable holding the base URL for all OTLP signals. */
	public static final String ENV_ENDPOINT = "OTEL_EXPORTER_OTLP_ENDPOINT";
	/** Environment variable holding the complete URL for OTLP traces. */
	public static final String ENV_TRACES_ENDPOINT = "OTEL_EXPORTER_OTLP_TRACES_ENDPOINT";
	/** Environment variable holding the OTLP transport protocol. */
	public static final String ENV_PROTOCOL = "OTEL_EXPORTER_OTLP_PROTOCOL";
	/** Environment variable holding the headers sent with each OTLP request. */
	public static final String ENV_HEADERS = "OTEL_EXPORTER_OTLP_HEADERS";
	/** Environment variable holding additional resource attributes. */
	public static final String ENV_RESOURCE_ATTRIBUTES = "OTEL_RESOURCE_ATTRIBUTES";
	/** Environment variable holding the service name. */
	public static final String ENV_SERVICE_NAME = "OTEL_SERVICE_NAME";

	private static final String HTTP_TRACES_PATH = "/v1/traces";

	private final Protocol protocol;
	private final String endpoint;
	private final Map<String, String> headers;
	private final String serviceName;
	private final Attributes resourceAttributes;
	private final String traceIdNamespace;
	private final TimeUnit timeUnit;

	/**
	 * Creates new settings from already resolved values.
	 *
	 * @param protocol
	 *            the OTLP transport protocol
	 * @param endpoint
	 *            the complete URL the exporter sends to
	 * @param headers
	 *            headers sent with each export request
	 * @param serviceName
	 *            the service name used for all hosts, or null to use the Kieker hostname
	 * @param resourceAttributes
	 *            attributes added to every resource
	 * @param traceIdNamespace
	 *            the namespace that is hashed into every trace ID
	 * @param timeUnit
	 *            the unit of the Kieker timestamps
	 */
	public ExporterSettings(final Protocol protocol, final String endpoint, final Map<String, String> headers, final String serviceName,
			final Attributes resourceAttributes, final String traceIdNamespace, final TimeUnit timeUnit) {
		this.protocol = protocol;
		this.endpoint = endpoint;
		this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(headers));
		this.serviceName = serviceName;
		this.resourceAttributes = resourceAttributes;
		this.traceIdNamespace = traceIdNamespace;
		this.timeUnit = timeUnit;
	}

	/**
	 * Resolves the settings from the given Kieker configuration and the environment of this process.
	 *
	 * @param configuration
	 *            the Kieker configuration
	 *
	 * @return the resolved settings
	 */
	public static ExporterSettings create(final Configuration configuration) {
		return ExporterSettings.create(configuration, System::getenv);
	}

	/**
	 * Resolves the settings from the given Kieker configuration and the given environment.
	 *
	 * @param configuration
	 *            the Kieker configuration
	 * @param environment
	 *            a lookup for environment variables, returning null for unset variables
	 *
	 * @return the resolved settings
	 */
	public static ExporterSettings create(final Configuration configuration, final UnaryOperator<String> environment) {
		final Protocol protocol = ExporterSettings.resolveProtocol(configuration, environment);
		final String endpoint = ExporterSettings.resolveEndpoint(protocol, configuration, environment);

		final Setting headers = Setting.resolve(configuration, OpenTelemetryExporterStage.HEADERS, environment, ENV_HEADERS);
		final Setting attributes = Setting.resolve(configuration, OpenTelemetryExporterStage.RESOURCE_ATTRIBUTES, environment, ENV_RESOURCE_ATTRIBUTES);

		final AttributesBuilder attributesBuilder = Attributes.builder();
		ExporterSettings.parseKeyValueList(attributes).forEach(attributesBuilder::put);
		final Attributes resourceAttributes = attributesBuilder.build();

		String serviceName = Setting.resolve(configuration, OpenTelemetryExporterStage.SERVICE_NAME, environment, ENV_SERVICE_NAME).value;
		if (serviceName.isEmpty()) {
			serviceName = resourceAttributes.get(ServiceAttributes.SERVICE_NAME);
		}

		return new ExporterSettings(protocol, endpoint, ExporterSettings.parseKeyValueList(headers), serviceName, resourceAttributes,
				configuration.getStringProperty(OpenTelemetryExporterStage.TRACE_ID_NAMESPACE),
				ExporterSettings.resolveTimeUnit(configuration.getStringProperty(OpenTelemetryExporterStage.TIME_UNIT)));
	}

	private static Protocol resolveProtocol(final Configuration configuration, final UnaryOperator<String> environment) {
		final Setting setting = Setting.resolve(configuration, OpenTelemetryExporterStage.EXPORT_TYPE, environment, ENV_PROTOCOL);
		if (setting.value.isEmpty()) {
			return Protocol.GRPC;
		}
		final String name = setting.value.toLowerCase(Locale.ROOT);
		for (final Protocol protocol : Protocol.values()) {
			if (protocol.getName().equals(name)) {
				return protocol;
			}
		}
		if ("zipkin".equals(name)) {
			throw new IllegalArgumentException(setting.source + " is 'zipkin', but Zipkin export is no longer supported. Use OTLP ('"
					+ Protocol.GRPC.getName() + "' or '" + Protocol.HTTP_PROTOBUF.getName()
					+ "'); an OpenTelemetry Collector can forward OTLP to Zipkin.");
		}
		throw new IllegalArgumentException(setting.source + " must be '" + Protocol.GRPC.getName() + "' or '" + Protocol.HTTP_PROTOBUF.getName()
				+ "', but was '" + setting.value + "'");
	}

	private static String resolveEndpoint(final Protocol protocol, final Configuration configuration, final UnaryOperator<String> environment) {
		final String configured = configuration.getStringProperty(OpenTelemetryExporterStage.EXPORT_URL);
		if (!configured.isEmpty()) {
			return ExporterSettings.toTracesEndpoint(protocol, configured);
		}
		// the signal-specific variable is used as given, the general one is a base URL
		final String tracesEndpoint = ExporterSettings.readEnvironment(environment, ENV_TRACES_ENDPOINT);
		if (!tracesEndpoint.isEmpty()) {
			return tracesEndpoint;
		}
		final String baseEndpoint = ExporterSettings.readEnvironment(environment, ENV_ENDPOINT);
		if (!baseEndpoint.isEmpty()) {
			return ExporterSettings.toTracesEndpoint(protocol, baseEndpoint);
		}
		return ExporterSettings.toTracesEndpoint(protocol, protocol.getDefaultBaseUrl());
	}

	/**
	 * Turns a base URL into the URL traces are sent to. gRPC uses the base URL itself, HTTP uses the path
	 * <code>/v1/traces</code> below it.
	 */
	private static String toTracesEndpoint(final Protocol protocol, final String baseUrl) {
		if (protocol == Protocol.GRPC) {
			return baseUrl;
		}
		String endpoint = baseUrl;
		if (endpoint.endsWith("/")) {
			endpoint = endpoint.substring(0, endpoint.length() - 1);
		}
		if (!endpoint.endsWith(HTTP_TRACES_PATH)) {
			endpoint += HTTP_TRACES_PATH;
		}
		return endpoint;
	}

	private static TimeUnit resolveTimeUnit(final String value) {
		if (value.isEmpty()) {
			return TimeUnit.NANOSECONDS;
		}
		try {
			return TimeUnit.valueOf(value.toUpperCase(Locale.ROOT));
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException(OpenTelemetryExporterStage.TIME_UNIT + " must be one of " + Arrays.toString(TimeUnit.values())
					+ ", but was '" + value + "'", e);
		}
	}

	/**
	 * Parses a list of the form <code>key1=value1,key2=value2</code>, as used by <code>OTEL_RESOURCE_ATTRIBUTES</code>
	 * and <code>OTEL_EXPORTER_OTLP_HEADERS</code>. Values may be percent-encoded.
	 */
	private static Map<String, String> parseKeyValueList(final Setting setting) {
		final Map<String, String> result = new ConcurrentHashMap<>();
		int position = 0;
		for (final String entry : setting.value.split(",")) {
			position++;
			if (entry.isBlank()) {
				continue;
			}
			final int separator = entry.indexOf('=');
			// without a separator, the key is empty
			final String key = entry.substring(0, Math.max(separator, 0)).trim();
			if (key.isEmpty()) {
				// the entry itself is not printed, as headers may carry credentials
				throw new IllegalArgumentException("Entry " + position + " of " + setting.source + " is not of the form key=value");
			}
			result.put(key, ExporterSettings.percentDecode(entry.substring(separator + 1).trim()));
		}
		return result;
	}

	private static String percentDecode(final String value) {
		if (value.indexOf('%') < 0) {
			return value;
		}
		try {
			// URLDecoder would turn a literal plus sign into a space
			return URLDecoder.decode(value.replace("+", "%2B"), StandardCharsets.UTF_8);
		} catch (final IllegalArgumentException e) {
			return value;
		}
	}

	private static String readEnvironment(final UnaryOperator<String> environment, final String name) {
		return Objects.requireNonNullElse(environment.apply(name), "").trim();
	}

	/**
	 * @return the OTLP transport protocol
	 */
	public Protocol getProtocol() {
		return this.protocol;
	}

	/**
	 * @return the complete URL the exporter sends to
	 */
	public String getEndpoint() {
		return this.endpoint;
	}

	/**
	 * @return the headers sent with each export request
	 */
	public Map<String, String> getHeaders() {
		return this.headers;
	}

	/**
	 * @return the service name used for all hosts, or null if the Kieker hostname is used
	 */
	public String getServiceName() {
		return this.serviceName;
	}

	/**
	 * @return the attributes added to every resource
	 */
	public Attributes getResourceAttributes() {
		return this.resourceAttributes;
	}

	/**
	 * @return the namespace that is hashed into every trace ID
	 */
	public String getTraceIdNamespace() {
		return this.traceIdNamespace;
	}

	/**
	 * @return the unit of the Kieker timestamps
	 */
	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

	/**
	 * The OTLP transport protocols supported by the exporter stage.
	 */
	public enum Protocol {
		/** OTLP over gRPC. */
		GRPC("grpc", "http://localhost:4317"),
		/** OTLP over HTTP with protobuf payloads. */
		HTTP_PROTOBUF("http/protobuf", "http://localhost:4318");

		private final String name;
		private final String defaultBaseUrl;

		Protocol(final String name, final String defaultBaseUrl) {
			this.name = name;
			this.defaultBaseUrl = defaultBaseUrl;
		}

		/**
		 * @return the name of the protocol, as used by <code>OTEL_EXPORTER_OTLP_PROTOCOL</code>
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * @return the base URL used if none is configured
		 */
		public String getDefaultBaseUrl() {
			return this.defaultBaseUrl;
		}
	}

	/**
	 * A raw setting value together with the name of the place it was read from, for error messages.
	 */
	private static final class Setting {
		private final String value;
		private final String source;

		private Setting(final String value, final String source) {
			this.value = value;
			this.source = source;
		}

		private static Setting resolve(final Configuration configuration, final String key, final UnaryOperator<String> environment,
				final String variable) {
			final String configured = configuration.getStringProperty(key);
			if (!configured.isEmpty()) {
				return new Setting(configured, key);
			}
			return new Setting(ExporterSettings.readEnvironment(environment, variable), variable);
		}
	}
}
