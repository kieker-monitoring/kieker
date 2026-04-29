/**
 * This module visualizes Kieker-derived architecture/models—loading
 * EMF/XMI models, constructing graph-based views of components and
 * relations, and exporting visualization artifacts for documentation
 * and analysis.
 */
module kieker.tools.mvis {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third-Party Dependencies
    requires com.github.hazendaz.parent;
    requires com.google.common;
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.slf4j;
}