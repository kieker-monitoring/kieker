/**
 * This module performs delta analysis of Kieker-derived models and logs,
 * comparing two snapshots/baselines to detect added/removed/changed
 * components, operations, interactions, and metric shifts, and produces
 * reports (e.g., CSV/JSON) for architecture drift and regression assessment.
 */
module kieker.tools.dar {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third-Party Dependencies
    requires org.slf4j;
}