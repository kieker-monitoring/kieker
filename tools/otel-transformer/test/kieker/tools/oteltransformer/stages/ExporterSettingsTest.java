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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.tools.oteltransformer.stages.ExporterSettings.Protocol;

import io.opentelemetry.api.common.AttributeKey;

/**
 * Tests how {@link ExporterSettings} are resolved from the Kieker configuration and the environment.
 *
 * @author Malte Hansen
 */
public class ExporterSettingsTest {

	private static final String HTTP_PROTOBUF = "http/protobuf";
	private static final String TRACES_ENDPOINT = "http://collector:4318/v1/traces";
	private static final String FROM_CONFIGURATION = "configuration";

	private final Configuration configuration = new Configuration();
	private final Map<String, String> environment = new ConcurrentHashMap<>();

	public ExporterSettingsTest() {
		// default constructor
	}

	private ExporterSettings resolve() {
		return ExporterSettings.create(this.configuration, this.environment::get);
	}

	@Test
	public void defaultsToGrpcOnPort4317() {
		final ExporterSettings settings = this.resolve();

		Assert.assertEquals(Protocol.GRPC, settings.getProtocol());
		Assert.assertEquals("http://localhost:4317", settings.getEndpoint());
		Assert.assertEquals(TimeUnit.NANOSECONDS, settings.getTimeUnit());
		Assert.assertEquals("", settings.getTraceIdNamespace());
		Assert.assertNull(settings.getServiceName());
		Assert.assertTrue(settings.getHeaders().isEmpty());
		Assert.assertTrue(settings.getResourceAttributes().isEmpty());
	}

	@Test
	public void httpDefaultsToTracesPathOnPort4318() {
		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_TYPE, HTTP_PROTOBUF);

		final ExporterSettings settings = this.resolve();

		Assert.assertEquals(Protocol.HTTP_PROTOBUF, settings.getProtocol());
		Assert.assertEquals("http://localhost:4318/v1/traces", settings.getEndpoint());
	}

	@Test
	public void exportTypeIsCaseInsensitive() {
		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_TYPE, "GRPC");

		Assert.assertEquals(Protocol.GRPC, this.resolve().getProtocol());
	}

	@Test
	public void exportUrlIsBaseUrl() {
		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_URL, "http://collector:4317");
		Assert.assertEquals("http://collector:4317", this.resolve().getEndpoint());

		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_TYPE, HTTP_PROTOBUF);
		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_URL, "http://collector:4318/");
		Assert.assertEquals(TRACES_ENDPOINT, this.resolve().getEndpoint());

		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_URL, TRACES_ENDPOINT);
		Assert.assertEquals(TRACES_ENDPOINT, this.resolve().getEndpoint());
	}

	@Test
	public void environmentIsUsedForUnsetKeys() {
		this.environment.put(ExporterSettings.ENV_PROTOCOL, HTTP_PROTOBUF);
		this.environment.put(ExporterSettings.ENV_ENDPOINT, "http://collector:4318");
		this.environment.put(ExporterSettings.ENV_HEADERS, "authorization=Bearer%20abc");
		this.environment.put(ExporterSettings.ENV_RESOURCE_ATTRIBUTES, "deployment.environment.name=test");
		this.environment.put(ExporterSettings.ENV_SERVICE_NAME, "shop");

		final ExporterSettings settings = this.resolve();

		Assert.assertEquals(Protocol.HTTP_PROTOBUF, settings.getProtocol());
		Assert.assertEquals(TRACES_ENDPOINT, settings.getEndpoint());
		Assert.assertEquals(Map.of("authorization", "Bearer abc"), settings.getHeaders());
		Assert.assertEquals("test", settings.getResourceAttributes().get(AttributeKey.stringKey("deployment.environment.name")));
		Assert.assertEquals("shop", settings.getServiceName());
	}

	@Test
	public void tracesEndpointVariableIsUsedAsGiven() {
		this.environment.put(ExporterSettings.ENV_PROTOCOL, HTTP_PROTOBUF);
		this.environment.put(ExporterSettings.ENV_ENDPOINT, "http://ignored:4318");
		this.environment.put(ExporterSettings.ENV_TRACES_ENDPOINT, "http://collector:4318/custom/path");

		Assert.assertEquals("http://collector:4318/custom/path", this.resolve().getEndpoint());
	}

	@Test
	public void configurationKeysTakePrecedenceOverEnvironment() {
		this.environment.put(ExporterSettings.ENV_PROTOCOL, HTTP_PROTOBUF);
		this.environment.put(ExporterSettings.ENV_ENDPOINT, "http://environment:4318");
		this.environment.put(ExporterSettings.ENV_HEADERS, "source=environment");
		this.environment.put(ExporterSettings.ENV_RESOURCE_ATTRIBUTES, "source=environment");
		this.environment.put(ExporterSettings.ENV_SERVICE_NAME, "environment");
		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_TYPE, "grpc");
		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_URL, "http://configuration:4317");
		this.configuration.setProperty(OpenTelemetryExporterStage.HEADERS, "source=configuration");
		this.configuration.setProperty(OpenTelemetryExporterStage.RESOURCE_ATTRIBUTES, "source=configuration");
		this.configuration.setProperty(OpenTelemetryExporterStage.SERVICE_NAME, FROM_CONFIGURATION);

		final ExporterSettings settings = this.resolve();

		Assert.assertEquals(Protocol.GRPC, settings.getProtocol());
		Assert.assertEquals("http://configuration:4317", settings.getEndpoint());
		Assert.assertEquals(Map.of("source", FROM_CONFIGURATION), settings.getHeaders());
		Assert.assertEquals(FROM_CONFIGURATION, settings.getResourceAttributes().get(AttributeKey.stringKey("source")));
		Assert.assertEquals(FROM_CONFIGURATION, settings.getServiceName());
	}

	@Test
	public void serviceNameFallsBackToResourceAttributes() {
		this.configuration.setProperty(OpenTelemetryExporterStage.RESOURCE_ATTRIBUTES, "service.name=from-attributes, team = a+b ");
		Assert.assertEquals("from-attributes", this.resolve().getServiceName());
		Assert.assertEquals("a+b", this.resolve().getResourceAttributes().get(AttributeKey.stringKey("team")));

		this.environment.put(ExporterSettings.ENV_SERVICE_NAME, "from-variable");
		Assert.assertEquals("from-variable", this.resolve().getServiceName());
	}

	@Test
	public void timeUnitAndNamespaceAreRead() {
		this.configuration.setProperty(OpenTelemetryExporterStage.TIME_UNIT, "milliseconds");
		this.configuration.setProperty(OpenTelemetryExporterStage.TRACE_ID_NAMESPACE, "run-1");

		final ExporterSettings settings = this.resolve();

		Assert.assertEquals(TimeUnit.MILLISECONDS, settings.getTimeUnit());
		Assert.assertEquals("run-1", settings.getTraceIdNamespace());
	}

	@Test
	public void zipkinIsRejectedWithPointerToOtlp() {
		this.configuration.setProperty(OpenTelemetryExporterStage.EXPORT_TYPE, "zipkin");

		final IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, this::resolve);
		Assert.assertTrue(exception.getMessage(), exception.getMessage().contains("no longer supported"));
		Assert.assertTrue(exception.getMessage(), exception.getMessage().contains(HTTP_PROTOBUF));
	}

	@Test
	public void unknownProtocolIsRejected() {
		this.environment.put(ExporterSettings.ENV_PROTOCOL, "http/json");

		final IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, this::resolve);
		Assert.assertTrue(exception.getMessage(), exception.getMessage().contains(ExporterSettings.ENV_PROTOCOL));
	}

	@Test
	public void unknownTimeUnitIsRejected() {
		this.configuration.setProperty(OpenTelemetryExporterStage.TIME_UNIT, "fortnights");

		Assert.assertThrows(IllegalArgumentException.class, this::resolve);
	}

	@Test
	public void malformedHeaderIsRejectedWithoutPrintingIt() {
		this.configuration.setProperty(OpenTelemetryExporterStage.HEADERS, "a=b,secret-without-key");

		final IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, this::resolve);
		Assert.assertTrue(exception.getMessage(), exception.getMessage().contains("Entry 2"));
		Assert.assertFalse(exception.getMessage(), exception.getMessage().contains("secret-without-key"));
	}
}
