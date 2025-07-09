/**
 * This module offers the packages {@code kieker.common.configuration},
 * {@code kieker.common.exception}, {@code kieker.common.namedRecordPipe},
 * {@code kieker.common.record}, {@code kieker.common.registry},
 * {@code kieker.common.util} and all the subpackages.
 *<p>
 * This module is used to provide a common code base, used by various other modules and packages.
 * For example, various records are defined here, which are used and created by other parts,
 * such as multiple classes within in the monitoring or analysis modules.
 *
 **/
module kieker.common {
    //Provided Packages
    exports kieker.common.configuration;
    exports kieker.common.exception;
    exports kieker.common.namedRecordPipe;
    exports kieker.common.record;
    exports kieker.common.record.misc;
    exports kieker.common.record.io;
    exports kieker.common.record.factory;
    exports kieker.common.record.database;
    exports kieker.common.record.session;
    exports kieker.common.record.controlflow;
    exports kieker.common.record.flow;
    exports kieker.common.record.flow.trace;
    exports kieker.common.record.flow.trace.operation;
    exports kieker.common.record.flow.trace.operation.constructor;
    exports kieker.common.record.flow.trace.operation.constructor.object;
    exports kieker.common.record.flow.trace.operation.object;
    exports kieker.common.record.flow.trace.concurrency;
    exports kieker.common.record.flow.trace.concurrency.monitor;
    exports kieker.common.record.flow.thread;
    exports kieker.common.record.jvm;
    exports kieker.common.record.remotecontrol;
    exports kieker.common.record.system;
    exports kieker.common.registry;
    exports kieker.common.registry.writer;
    exports kieker.common.registry.reader;
    exports kieker.common.util;
    exports kieker.common.util.classpath;
    exports kieker.common.util.dataformat;
    exports kieker.common.util.filesystem;
    exports kieker.common.util.map;
    exports kieker.common.util.signature;
    exports kieker.common.util.thread;

    //Java Dependencies
    requires java.logging;

    //Third-Party Dependencies
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
}