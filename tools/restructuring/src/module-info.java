/**
 * This module restructures Kieker architecture models (components
 * and operations) via a TeeTime-based CLI pipeline—loading models,
 * applying configurable transformations (e.g., merge, split,
 * regroup), and exporting the refined models for downstream
 * analysis and visualization.
 */
module kieker.tools.restructuring {

    //Provided Packages
    exports kieker.tools.restructuring.restructuremodel;


    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third Party Dependencies
    requires com.github.hazendaz.parent;
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.eclipse.emf.ecore.xmi;
    requires org.jgrapht.core;
    requires org.slf4j;
}