/**
 * This module is used to mine and analyze runtime behavior from execution
 * traces and monitoring logs. It reconstructs user/session flows and derives
 * behavior models (e.g., Markov chains or branching user-behavior graphs) to
 * characterize how a system is actually used.
 */
module kieker.tools.behavior.analysis {

    //Kieker Dependencies
    requires kieker.tools;
    requires kieker.analysis;
    requires kieker.common;

    //Third-Party Dependencies
    requires org.slf4j;
    requires com.google.common;
}