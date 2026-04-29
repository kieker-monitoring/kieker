/**
 * This module reads Kieker monitoring logs and rewrites their
 * entries according to user-defined rules—e.g., normalizing or
 * anonymizing attributes, correcting/retiming timestamps,
 * remapping identifiers, and filtering records—and outputs an
 * adjusted log for consistent downstream analysis.
 */
module kieker.log.rewriter {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.monitoring.core;
    requires kieker.tools;

    //Third Party Dependencies
    requires org.slf4j;
    requires org.jctools.core;

}