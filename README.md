Framework to Manage Streaming Data
==================================

This framework consists of the following inter-connected building blocks.

Interface to the Source of the Streaming Data
---------------------------------------------
The source of the streaming data can be any external independent system producing for example a log file which is
continuously appended and rotated periodically, a JDBC connection to a database or an emitter of key-value tuples. 

Producer
--------
The producer input is the source of the streaming data. The output of the producer is a streaming
data carrier.

Carrier
-------
The carrier delivers the streaming data from the producer to the consumer. e.g. AWS Kinesis, Kafka.
The carrier decouples the producers from the consumers. As such there is an N:M mapping between the
producers and the consumers even on the same stream.

Consumer
--------
The input of the consumer is the same streaming data carrier that the producer(s) publish to and its output
is passed on to the streaming data processor.

Interface to the Streaming Data Processor
-----------------------------------------
The streaming data processor is the target destination of this framework.  e.g. A continuous loader to a database like
the AWS Redshift, the copier to AWS S3, or a streaming data analytics application and any such external independent
system.

Build Instructions
===================
Use maven to build and install all the streaming data modules.

    $ mvn clean install

The producer and consumer modules can be built and installed separately using the default maven goals.
See their respective README.md files.

Link and Execution Instructions
===============================
Both the producer and consumer can be either an independent executable application or used as a library. For detailed
instructions see their respective README.md files.

How to Add or Modify Code in this Framework
===========================================
* The open source coding standard which is reflected in the present code must be obeyed.

* It is mandatory to provide proper unit tests with maximum practical coverage.

* JavaDOC annotation and documentation must be provided for AT LEAST the public interfaces, classes,
fields and methods.

* Peer review is required. Such review must ensure adequate unit test coverage not only for the added
or modified code but also for all the affected code, proper JavaDOC documentation and above all sound
semantics and performance requirements.
