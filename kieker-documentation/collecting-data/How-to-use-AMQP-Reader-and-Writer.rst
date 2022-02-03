.. _collecting-data-amqp-reader-writer:

How to use AMQP Writer and Reader
=================================

This chapter gives a brief description on how to use the **AmqpWriter**
and **AMQPReaderStage** classes, which allow to use Kieker with
AMQP-based queue implementations such as `RabbitMQ <http://www.rabbitmq.com>`.
The directory ``examples/userguide/appendix-AMQP/`` contains the
sources, gradle scripts and other sources used in this example. It is
based on the Bookstore application.

The following paragraphs provide step-by-step instructions for the
popular AMQP implementation RabbitMQ.

Preparation
-----------

Download and Install RabbitMQ
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Download the RabbitMQ distribution from http://www.rabbitmq.com/download.html
and follow the installation instructions for your OS. Since RabbitMQ
requires Erlang, additional software packages may have to be installed
on your machine.

In order to use RabbitMQ's integrated management UI, you may have to
enable the appropriate plugin first. This is done by issuing the
following command from the command line. 

- ``rabbitmq−plugins enable rabbitmq management`` Enable the management UI under UNIX-like systems
- ``rabbitmq−plugins enable rabbitmq management`` Enable the management UI under Windows]

Once the UI is enabled, you may access it at port 15672 by default.

Configure RabbitMQ
~~~~~~~~~~~~~~~~~~

Once the RabbitMQ server is installed and started, create a queue for
Kieker to use. This can be done easily using RabbitMQ's management UI.
It is accessible via http://localhost:15672 (the default credentials are
`guest:guest`) We will assume a queue named ``kieker`` for the remainder
of this example. Please note the following caveats when configuring the
server:

#. If you choose to create a transient queue, the entire queue (not just
   the queued messages) is destroyed on server shutdown and must be
   re-created manually.
#. The RabbitMQ server's default permissions grant access only from 
   ``localhost``. If your RabbitMQ server runs on a remote machine, you
   have to set the permissions accordingly.

Kieker Monitoring Configuration for RabbitMQ
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The file ``src-resources/META-INF/kieker.monitoring.properties``
is already configured to use the **AmqpWriter**. The important
properties are the server URI and the queue name.

.. code-block:: shell
  
  kieker.monitoring.writer.amqp.AmqpWriter.uri=amqp://guest:guest@127.0.0.1
  kieker.monitoring.writer.amqp.AmqpWriter.queuename=kieker


Running the Example
-------------------

The execution of the example is performed by the following three steps:

#. Ensure that the RabbitMQ server is started and the configured queue is accessible.
#. Start the analysis part (in a new terminal):
   - ``# ./gradlew runAnalysisAMQP`` Start the analysis part under UNIX-like systems
   - ``# gradlew.bat runAnalysisAMQP`` Start the analysis part under Windows]
#. Start the instrumented Bookstore (in a new terminal):
   - ``# ./gradlew runMonitoringAMQP`` Start the analysis part under UNIX-like systems
   - ``# gradlew.bat runMonitoringAMQP`` Start the analysis part under Windows

