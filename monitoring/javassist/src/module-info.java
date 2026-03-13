/**
 * This module extends Kieker’s monitoring abilities by leveraging
 * Javassist for dynamic bytecode manipulation and the Java
 * Instrumentation API for integrating those changes at runtime.
 */
module kieker.monitoring.javassist {

    //Kieker Dependencies
    requires kieker.monitoring.core;

    //Java Dependencies
    requires java.instrument;

    //Third-Party Dependencies
    requires org.javassist;
}