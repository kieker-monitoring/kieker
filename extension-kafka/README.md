# Kafka Extension for Kieker

This Kieker subproject contains an extension to write and read monitoring records to/from [Apache Kafka](https://kafka.apache.org) topics.

## Usage

In order to use this extension, a running Kafka server is required.

Kieker has to be configured to use the Kafka writer. Thus, add the following lines to your `kieker.monitoring.properties` file and adjust
the properties for the `bootstrapServers` and the `topicName`.

````properties

#####
#
## Write the data using the chunking collector
kieker.monitoring.writer=kieker.monitoring.writer.collector.ChunkingCollector

#
## The serializer to use for writing records
kieker.monitoring.writer.collector.ChunkingCollector.serializer=kieker.monitoring.writer.serializer.BinarySerializer
kieker.monitoring.writer.collector.ChunkingCollector.writer=kieker.monitoring.writer.kafka.KafkaWriter

#
## The following properties correspond to Kafka producer's configuration paramaters with the same name
## For a more detailed explanation see: https://kafka.apache.org/documentation/#producerconfigs
#
## The address and port of the Kafka bootstrap server(s) ("host1:port1"), multiple servers comma separated
kieker.monitoring.writer.kafka.KafkaWriter.bootstrapServers=127.0.0.1:9092
#
## The topic name to use for the monitoring records
kieker.monitoring.writer.kafka.KafkaWriter.topicName=kiekerRecords
#
## Number of acknowledgments
## Valid values: [all, -1, 0, 1]
kieker.monitoring.writer.kafka.KafkaWriter.acks=all
#
## Batch size
kieker.monitoring.writer.kafka.KafkaWriter.batchSize=16384
#
## Total bytes of buffer memory
kieker.monitoring.writer.kafka.KafkaWriter.bufferMemory=33554432 # = (32 << 20)
#
## Linger interval
kieker.monitoring.writer.kafka.KafkaWriter.lingerMs=1
````

The Kafka writer properties `acks`, `batchSize`, `bufferMemory`, and `lingerMs` are optional. If they are not specified, the
default values declared here are used. See the corresponding configuration parameters in the official
[Kafka producer documentation](https://kafka.apache.org/documentation/#producerconfigs) for a more detailed explanation.

In order to avoid unintended behavior, there should be no other active property with key `kieker.monitoring.writer`.
