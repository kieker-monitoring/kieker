/**
 * This module provides a sar-like command-line reporter for
 * Kieker resource-monitoring data—reading live streams or logs
 * to summarize CPU, memory, load, network, and disk utilization
 * over time, with options to filter intervals/metrics and export
 * results for analysis.
 */
module kieker.tools.sar {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third Party Dependencies
    requires com.github.hazendaz.parent;
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.slf4j;
}