/**
 * This module implements a java agent that performs monitoring
 * via Aspect-Oriented Programming (AOP).
 */
module kieker.monitoring.aspectj {
    //Kieker Dependencies
    requires kieker.common;
    requires kieker.monitoring.core;

    //Java Dependency
    requires java.sql;
    requires java.instrument;

    //Third Party Dependencies
    requires org.aspectj.weaver;
    requires org.slf4j;
    requires jakarta.ws.rs;
}