/**
 * This module provides a command-line utility to convert Kieker logging
 * timestamps (nanoseconds since the Unix epoch) into human-readable
 * date/time strings, printing both UTC and local-time representations
 * for quick inspection, troubleshooting, and reporting.
 */
module kieker.tools.logging.timestamp.converter {

    //Kieker Dependencies
    requires kieker.common;
    requires kieker.tools;

    //Third-Party Dependencies
    requires org.slf4j;
}