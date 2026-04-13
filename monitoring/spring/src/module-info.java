/**
 * This module contains interceptors and filters for monitoring Spring-based
 * applications. It captures execution of Spring-based applications and forwards
 * the collected data to the kieker monitoring infrastructure.
 */
module kieker.monitoring.spring {
    //Kieker Dependencies
    requires kieker.common;
    requires kieker.monitoring.core;

    //Third Party Dependencies
    requires org.slf4j;
    requires spring.aop;
    requires spring.context;
    requires spring.web;
}