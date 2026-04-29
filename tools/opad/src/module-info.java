/**
 * This module performs online performance anomaly detection on
 * operation-level time series (e.g., response times), offering
 * pipelines/filters that learn baselines, detect deviations, and
 * emit/store detection results for further analysis and alerting.
 */
module kieker.tools.opad {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;

    //Third-Party Dependencies
    requires org.apache.commons.lang3;
    requires org.slf4j;
}