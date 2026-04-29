/**
 * This module performs model-based architecture analysis over
 * Kieker EMF/XMI models—loading architecture snapshots, computing
 * structural/metric evaluations and consistency checks, and exporting
 * results (e.g., CSV/JSON) for quality assessment and reporting.
 */
module kieker.tools.maa {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third-Party Dependencies
    requires com.github.hazendaz.parent;
    requires org.eclipse.emf.common;
    requires org.slf4j;
}