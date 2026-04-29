/**
 * This module provides the shared tooling layer and several
 * CLI applications for Kieker—offering common parsers,
 * converters, I/O and reporting utilities, plus entry points
 * for tasks like model/architecture analysis and table/report
 * generation—to process Kieker monitoring data and produce
 * derived artifacts.
 */
module kieker.tools {

    //Provided Packages
    exports kieker.visualization.trace.dependency.graph;
    exports kieker.visualization.trace.call.tree;
    exports kieker.tools.source;
    exports kieker.tools.common;
    exports kieker.tools.settings;
    exports kieker.tools.settings.converters;
    exports kieker.tools.settings.validators;
    exports kieker.tools.trace.analysis.filter;
    exports kieker.tools.trace.analysis.repository;
    exports kieker.tools.trace.analysis.systemModel;
    exports kieker.tools.trace.analysis.filter.visualization;
    exports kieker.visualization.trace;
    exports kieker.tools.trace.analysis.systemModel.repository;
    exports kieker.tools.trace.analysis.filter.executionRecordTransformation;
    exports kieker.tools.trace.analysis.filter.flow;
    exports kieker.tools.trace.analysis.filter.traceFilter;
    exports kieker.tools.trace.analysis.filter.traceReconstruction;
    exports kieker.tools.trace.analysis.filter.traceWriter;
    exports kieker.tools.trace.analysis.filter.visualization.callTree;
    exports kieker.tools.trace.analysis.filter.visualization.dependencyGraph;
    exports kieker.tools.trace.analysis.filter.visualization.sequenceDiagram;
    exports kieker.tools.trace.analysis.filter.visualization.descriptions;
    exports kieker.tools.trace.analysis.filter.visualization.traceColoring;


    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.monitoring.core;

    //Java Dependencies
    requires java.logging;

    //Third Party Dependencies
    requires org.slf4j;
}