/**
 * The Cassandra extension lets Kieker persist its monitoring,
 * trace and log data into an Apache Cassandra database
 */
module kieker.extension.cassandra {
    //Kieker dependencies
    requires kieker.common;
    requires kieker.monitoring.core;

    //Third Party dependencies
    requires org.slf4j;
}