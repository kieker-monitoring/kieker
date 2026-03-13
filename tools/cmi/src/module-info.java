/**
 * This module performs continuous model inference (CMI) from Kieker
 * monitoring data, reconstructing and continuously updating
 * component/service interaction and operation-call models
 * (e.g., dependency/communication graphs) for architecture discovery,
 * visualization, and drift/change analysis.
 */
module kieker.tools.cmi {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third-Party Dependencies
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.slf4j;
}