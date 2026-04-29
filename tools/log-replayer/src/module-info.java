/**
 * This module replays Kieker monitoring logs as a live event
 * stream—reading recorded monitoring records and emitting them
 * with original or configurable timing—to reproduce runs, drive
 * and validate analysis pipelines, and test integrations.
 */
module kieker.tools.log.replayer {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.monitoring.core;
    requires kieker.tools;

    //Third-Party Dependencies
    requires org.slf4j;
}