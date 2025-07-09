/**
 * This module processes monitored-operation profiles: it reads and
 * validates operation patterns/definitions, merges and filters them,
 * and exports configuration files to drive Kieker instrumentation
 * and downstream analyses.
 */
module kieker.tools.mop {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third-Party Dependencies
    requires com.github.hazendaz.parent;
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.slf4j;
}