/**
 * This module offers the packages {@code kieker.monitoring.probe},
 * {@code kieker.monitoring.sampler}, {@code kieker.monitoring.util},
 * {@code kieker.monitoring.core}, {@code kieker.monitoring.writer},
 * {@code kieker.monitoring.timer} and most of their subpackages.
 *
 * This module is responsible for the instrumentation of applications,
 * generates runtime data in form of records (--> Analysis) and
 * provides an API for implementing your own writers.
 */
module kieker.monitoring.core {
    //Provided Packages
    exports kieker.monitoring;
    exports kieker.monitoring.probe;
    exports kieker.monitoring.probe.utilities;
    exports kieker.monitoring.sampler.oshi;
    exports kieker.monitoring.util;
    exports kieker.monitoring.core.registry;
    exports kieker.monitoring.core.controller;
    exports kieker.monitoring.core.sampler;
    exports kieker.monitoring.core.configuration;
    exports kieker.monitoring.writer;
    exports kieker.monitoring.writer.filesystem;
    exports kieker.monitoring.writer.tcp;
    exports kieker.monitoring.writer.compression;
    exports kieker.monitoring.writer.raw;
    exports kieker.monitoring.timer;

    //Kieker Dependencies
    requires kieker.common;

    //Java Dependencies
    requires java.management;
    requires java.naming;
    requires java.xml;

    //Third Party Dependencies
    requires org.apache.commons.compress;
    requires org.apache.commons.io;
    requires org.slf4j;
    requires com.github.oshi;
    requires org.tukaani.xz;

}