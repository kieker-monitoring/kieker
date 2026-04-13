/**
 * This module implements a java agent that performs monitoring
 * with the help of DiSL, which is inspired by Aspect-Oriented-
 * Programming (AOP).
 */
module kieker.monitoring.disl {
    //Kieker Dependencies
    requires kieker.common;
    requires kieker.monitoring.core;

    //Third Party Dependencies
    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
}