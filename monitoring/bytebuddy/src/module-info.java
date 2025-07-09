/**
 * This module implements a Java agent that performs
 * code generation and manipulation with the help of bytebuddy.
 */
module kieker.monitoring.bytebuddy
{
    //Kieker Dependencies
    requires kieker.common;
    requires kieker.monitoring.core;

    //Java Dependencies
    requires java.instrument;

    //Third Party Dependencies
    requires net.bytebuddy;
    requires org.slf4j;
}