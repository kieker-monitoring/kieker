/**
 * This module acts as a central receiver for Kieker monitoring data,
 * collecting records from distributed sources and persisting or
 * forwarding them to standardized Kieker logs or downstream analysis
 * pipelines for centralized monitoring.
 */
module kieker.tools.collector {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.tools;
}