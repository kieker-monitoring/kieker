/**
 * This module monitors host-level system resources (CPU,
 * memory/swap, load average, network utilization, disk I/O)
 * at configurable intervals and duration, using Kieker’s
 * monitoring controller and OSHI-based samplers, providing
 * a CLI tool to start/stop sampling and emit the gathered
 * metrics for recording and analysis.
 */
module kieker.tools.resource.monitor {

    //Kieker Dependencies
    requires kieker.common;
    requires kieker.monitoring.core;
    requires kieker.tools;

    //Third Party Dependencies
    requires org.slf4j;
}