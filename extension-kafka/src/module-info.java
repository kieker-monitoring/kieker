/**
 * The Kafka extension lets Kieker read and write monitoring records
 * to and from Apache kafka topics.
 */
module kieker.extension.kafka {
    //Kieker dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.monitoring.core;
}