# otel-transformer

The OTel Transformer receives Kieker records via TCP, reconstructs the execution traces, and exports them as
OpenTelemetry spans via OTLP.
The attribute names follow the OpenTelemetry semantic conventions 1.45.0.

## Usage

```
otel-transformer -lp <port> [-c <configuration file>]
```

- `-lp`, `--listenPort`:   the port on which Kieker records are received, e.g., from the `SingleSocketTcpWriter`.
- `-c`, `--configuration`: an optional Kieker configuration file. See `resources/kieker.monitoring.properties`
for an example.

Only `OperationExecutionRecord`s are processed.
Traces that Kieker cannot reconstruct completely are invalid and are not exported.

## Configuration

All keys are prefixed with `kieker.tools.oteltransformer.stages.OpenTelemetryExporterStage.` and are optional.
A key that is not set falls back to the listed standard OpenTelemetry environment variable, and otherwise to the default.

| Key                  | Environment variable | Default | Description |
|----------------------|---|---|---|
| `ExportType`         | `OTEL_EXPORTER_OTLP_PROTOCOL` | `grpc` | The OTLP transport protocol, `grpc` or `http/protobuf`. |
| `ExportURL`          | `OTEL_EXPORTER_OTLP_TRACES_ENDPOINT`, `OTEL_EXPORTER_OTLP_ENDPOINT` | `http://localhost:4317` (`grpc`), `http://localhost:4318` (`http/protobuf`) | The base URL of the OTLP endpoint. |
| `ServiceName`        | `OTEL_SERVICE_NAME` | the Kieker hostname | The `service.name` used for the spans of all hosts. |
| `ResourceAttributes` | `OTEL_RESOURCE_ATTRIBUTES` | none | Additional resource attributes as `key1=value1,key2=value2`. |
| `Headers`            | `OTEL_EXPORTER_OTLP_HEADERS` | none | Headers sent with every export request as `key1=value1,key2=value2`. |
| `TraceIdNamespace`   | | empty | A namespace that is hashed into the trace IDs. |
| `TimeUnit`           | | `NANOSECONDS` | The unit of the timestamps in the Kieker records, as name of a `java.util.concurrent.TimeUnit`. |

Notes:

- `ExportURL` and `OTEL_EXPORTER_OTLP_ENDPOINT` are base URLs: for `http/protobuf`, the path `/v1/traces` is
appended. `OTEL_EXPORTER_OTLP_TRACES_ENDPOINT` is used as given.
- If neither `ServiceName` nor `OTEL_SERVICE_NAME` is set, a `service.name` contained in the resource attributes
is used, and otherwise the Kieker hostname.
- Values in `ResourceAttributes` and `Headers` may be percent-encoded.
- Other OpenTelemetry environment variables, e.g., for timeouts, compression, or TLS certificates, are not
evaluated.
- Zipkin export is no longer available, as the OpenTelemetry Java SDK discontinued its Zipkin exporter. An
OpenTelemetry Collector can receive OTLP and forward to Zipkin.

Backends that require further information, e.g., an access token, can be served with `ResourceAttributes` or
`Headers`.

## Mapping

Every Kieker monitoring record is mapped to an OpenTelemetry span.
The following table gives an overview on how core span properties are computed from Kieker records.

| Span          | Value                                                                    |
|---------------|--------------------------------------------------------------------------|
| Name          | `<simple class name>.<method>`, e.g., `Catalog.getItem`                  |
| Kind          | `INTERNAL`                                                               |
| Status        | Unset, as records carry no error information                             |
| Start and end | `tin` and `tout`, converted from `TimeUnit`                              |
| Trace ID      | Hash of `TraceIdNamespace` and the Kieker trace ID                       |
| Span ID       | Hash of the trace ID and the execution order index                       |
| Parent        | The calling execution, derived from execution order index and stack size |

Trace and span IDs are derived hashes: exporting the same Kieker trace again results in the same
OpenTelemetry trace.
Logs of unrelated runs can contain equal Kieker trace IDs; use a different `TraceIdNamespace`
per run to keep their traces apart.

| Span attribute               | Value                                                                                   |
|------------------------------|-----------------------------------------------------------------------------------------|
| `code.function.name`         | Fully qualified class name and the method name, e.g., `org.shop.Catalog.getItem`        |
| `session.id`                 | Kieker session ID, if the execution belongs to a session                                |
| `kieker.operation.signature` | Complete signature including modifiers, return type, and parameter types                |
| `kieker.trace.id`            | Kieker trace ID                                                                         |
| `kieker.eoi`                 | Kieker execution order index                                                            |
| `kieker.ess`                 | Kieker execution stack size                                                             |
| `kieker.assumed`             | `true` for executions that were not measured but assumed by Kieker, otherwise absent    |

All executions of one Kieker hostname share one resource.

| Resource attribute | Value                                                      |
|--------------------|------------------------------------------------------------|
| `service.name`     | `ServiceName` if configured, otherwise the Kieker hostname |
| `host.name`        | Kieker hostname                                            |
| `telemetry.sdk.*`  | OpenTelemetry SDK used by the otel-transformer             |
| Further attributes | As configured with `ResourceAttributes`                    |

`service.instance.id` is not set, as Kieker records do not identify the instance of a service.
The instrumentation scope is `kieker` with the Kieker version.
Resource and scope use the schema URL `https://opentelemetry.io/schemas/1.45.0`.
