/**
 * This module offers the packages {@code kieker.analysis.analysisComponent},
 * {@code kieker.analysis.annotation}, {@code kieker.analysis.architecture},
 * {@code kieker.analysis.behaviour}, {@code kieker.analysis.code},
 * {@code kieker.analysis.exception}, {@code kieker.analysis.generic},
 * {@code kieker.analysis.metrics}, {@code kieker.analysis.plugin},
 * {@code kieker.analysis.repository},
 * {@code kieker.analysis.util} and all the subpackages.
 * <p>
 * This module is the code basis for the analysis functions:
 * <ul>
 *   <li>trace reconstruction and visualization</li>
 *   <li>anomaly detection</li>
 *   <li>architecture discovery and visualization</li>
 * </ul>
 */
module kieker.analysis {

    //Provided Packages
    exports kieker.analysis.behavior;
    exports kieker.analysis.behavior.acceptance.matcher;
    exports kieker.analysis.behavior.clustering;
    exports kieker.analysis.behavior.signature.processor;
    exports kieker.analysis.behavior.model;
    exports kieker.analysis.behavior.events;
    exports kieker.analysis.metrics.graph.entropy;
    exports kieker.analysis.plugin;
    exports kieker.analysis.plugin.filter;
    exports kieker.analysis.plugin.filter.flow;
    exports kieker.analysis.plugin.filter.record;
    exports kieker.analysis.plugin.filter.select;
    exports kieker.analysis.plugin.reader;
    exports kieker.analysis.plugin.reader.filesystem;
    exports kieker.analysis.plugin.reader.newio;
    exports kieker.analysis.plugin.annotation;
    exports kieker.analysis.plugin.trace;
    exports kieker.analysis.repository;
    exports kieker.analysis.repository.annotation;
    exports kieker.analysis.analysisComponent;
    exports kieker.analysis.statistics;
    exports kieker.analysis.statistics.calculating;
    exports kieker.analysis.util;
    exports kieker.analysis.util.stage;
    exports kieker.analysis.util.stage.trigger;
    exports kieker.analysis.architecture;
    exports kieker.analysis.architecture.dependency;
    exports kieker.analysis.architecture.trace;
    exports kieker.analysis.architecture.trace.reconstruction;
    exports kieker.analysis.architecture.trace.execution;
    exports kieker.analysis.architecture.trace.sink;
    exports kieker.analysis.architecture.trace.flow;
    exports kieker.analysis.architecture.recovery;
    exports kieker.analysis.architecture.recovery.assembler;
    exports kieker.analysis.architecture.recovery.events;
    exports kieker.analysis.architecture.recovery.signature;
    exports kieker.analysis.architecture.repository;
    exports kieker.analysis.generic;
    exports kieker.analysis.generic.sink;
    exports kieker.analysis.generic.sink.graph;
    exports kieker.analysis.generic.sink.graph.dot;
    exports kieker.analysis.generic.sink.graph.dot.attributes;
    exports kieker.analysis.generic.sink.graph.graphml;
    exports kieker.analysis.generic.source;
    exports kieker.analysis.generic.source.rest;
    exports kieker.analysis.generic.source.file;
    exports kieker.analysis.generic.source.tcp;
    exports kieker.analysis.generic.source.rewriter;
    exports kieker.analysis.generic.data;
    exports kieker.analysis.generic.time;
    exports kieker.analysis.generic.graph;
    exports kieker.analysis.generic.graph.clustering;
    exports kieker.analysis.generic.graph.selector;
    exports kieker.analysis.generic.clustering;
    exports kieker.analysis.generic.clustering.optics;
    exports kieker.analysis.generic.clustering.mtree;
    exports kieker.analysis.generic.clustering.mtree.utils;
    exports kieker.analysis.code;
    exports kieker.analysis.code.data;
    exports kieker.analysis.exception;
    exports kieker.analysis;


    // Kieker dependencies
    requires kieker.model;
    requires kieker.common;

    // Java Dependencies
    requires java.sql;
    requires java.management;
    requires java.naming;
    requires java.xml;
    requires java.xml.bind;

    // Third-Party Dependencies
    requires com.google.common;
    requires org.apache.commons.compress;
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.eclipse.emf.ecore.xmi;
    requires org.slf4j;
    requires org.tukaani.xz;
    requires org.yaml.snakeyaml;
    requires kieker.monitoring.core;


}