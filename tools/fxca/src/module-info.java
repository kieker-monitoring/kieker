/**
 * This module analyzes Fortran code by processing fxtran-generated XML,
 * producing a call table, operation definitions, and a “not-found” report
 * to support code comprehension and downstream tooling.
 */
module kieker.tools.fxca {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Java Dependencies
    requires java.xml;

    //Third-Party Dependencies
    requires com.github.hazendaz.parent;
    requires org.apache.commons.lang3;
    requires org.slf4j;
}