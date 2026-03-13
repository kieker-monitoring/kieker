/**
 * This module analyzes Kieker monitoring logs to reconstruct
 * end-to-end execution traces, derive call trees/graphs and
 * service/operation dependencies, compute performance metrics
 * (e.g., response times, trace statistics), and export
 * reports/visualizations (e.g., sequence diagrams, dependency
 * graphs) via both a CLI and a GUI-driven wizard.
 */
module kieker.tools.trace.analysis {

    //Provided Packages
    exports kieker.tools.trace.analysis;
    exports kieker.tools.trace.analysis.filter.systemModel;


    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third Party Dependencies
    requires org.slf4j;
}