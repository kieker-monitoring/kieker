/**
 * This module offers the packages {@code kieker.model.repository},
 * {@code kieker.model.system.model}, {@code kieker.system.model.util} and
 * {@code kieker.system.model.exceptions}
 *
 * Model holds and manages the monitoring data model that represents the
 * observed runtime behavior of a software system.
 **/
module kieker.model {
    //Provided Packages
    exports kieker.model.repository;
    exports kieker.model.system.model;
    exports kieker.model.system.model.util;
    exports kieker.model.system.model.exceptions;
    exports kieker.model.collection;
    exports kieker.model.collection.util;
    exports kieker.model.collection.impl;
    exports kieker.model.analysismodel;
    exports kieker.model.analysismodel.assembly;
    exports kieker.model.analysismodel.assembly.util;
    exports kieker.model.analysismodel.assembly.impl;
    exports kieker.model.analysismodel.deployment;
    exports kieker.model.analysismodel.deployment.util;
    exports kieker.model.analysismodel.deployment.impl;
    exports kieker.model.analysismodel.execution;
    exports kieker.model.analysismodel.execution.util;
    exports kieker.model.analysismodel.execution.impl;
    exports kieker.model.analysismodel.impl;
    exports kieker.model.analysismodel.source;
    exports kieker.model.analysismodel.source.util;
    exports kieker.model.analysismodel.source.impl;
    exports kieker.model.analysismodel.statistics;
    exports kieker.model.analysismodel.statistics.util;
    exports kieker.model.analysismodel.statistics.impl;
    exports kieker.model.analysismodel.trace;
    exports kieker.model.analysismodel.trace.util;
    exports kieker.model.analysismodel.trace.impl;
    exports kieker.model.analysismodel.type;
    exports kieker.model.analysismodel.type.util;
    exports kieker.model.analysismodel.type.impl;
    //Kieker Dependencies
    requires kieker.common;

    //Third-Party Dependencies -- from src-gen
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
}