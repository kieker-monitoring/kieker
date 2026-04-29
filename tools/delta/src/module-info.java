/**
 * This module compares two Kieker-derived snapshots or models
 * to compute deltas—identifying added/removed/changed components,
 * operations, interactions, and metrics—and outputs reports for
 * architecture drift and regression analysis.
 */
module kieker.tools.delta {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.tools;
    requires kieker.tools.restructuring;

    //Third-Party Dependencies
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.eclipse.emf.ecore.xmi;
    requires org.slf4j;
}