/**
 * This module is used to convert OpenTelemetry telemetry — primarily traces/spans
 * (and, in some setups, metrics/logs) — into Kieker-compatible data so you can
 * leverage Kieker’s analysis and visualization tools.
 */
module kieker.tools.oteltransformer {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.monitoring.core;
    requires kieker.tools;

    //Third-Party Dependencies
    requires io.opentelemetry.api;
    requires io.opentelemetry.context;
    requires io.opentelemetry.exporter.otlp;
    requires io.opentelemetry.exporter.zipkin;
    requires io.opentelemetry.sdk;
    requires io.opentelemetry.sdk.common;
    requires io.opentelemetry.sdk.trace;
    requires org.slf4j;
}