/**
 * This module relabels identifiers in Kieker models and analysis
 * artifacts — applying user-defined mapping rules to rename
 * components/operations/types/hosts for normalization or anonymization —
 * while loading/saving EMF-based models to ensure all references remain
 * consistent for downstream tools.
 */
module kieker.tools.relabel {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third Party Dependencies
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.slf4j;

}