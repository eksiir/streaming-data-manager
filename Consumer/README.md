Streaming Data Consumer Module
==============================
The input of the streaming data consumer is the same streaming data carrier that the producer(s) publish to
and its output is passed on to the streaming data processor.

Unit Tests Requirements
=======================
The following are required to run unit tests successfully:

* A valid AWS Kinesis security token is needed.

* /var/log/StreamingDataManager/ directory should exist with proper read/write permissions for the user running the tests.

Build Instructions
===================
The consumer can be either an independent executable application or used as a library using the default maven goals.
e.g.

    $ mvn clean install

If the above unit test requirements cannot be met pass -DskipTests as an argument to mvn.

Creates the consumer library jar and the independent consumer application as
<b>target/StreamingDataConsumer-\<version\>.tar.gz</b>.

If the consumer library or application uses the Amazon Web Services the proper credentials are required in the environment.
From the sources <b>Consumer/src/main/resources/AwsCredentials.properties</b> resource file can be provided with valid
<b>accessKey</b> and <b>secretKey</b>. If any Redshift Connector is used, from the
<b>Consumer/src/main/resources/RedshiftCredentials.properties</b> resource file must be provided with valid
<b>redshiftURL</b>, <b>redshiftUsername</b> and <b>redshiftPassword</b>.

<u>These files must NOT be added to GIT or shared under any circumstances.</u>

Execution Instructions
======================

Using the Consumer Library
--------------------------
The <b>package</b> goal of maven creates the <b>target/Consumer-\<version\>.jar</b>.  This library requires JDK 1.7 or later.

Add the following to the application pom.xml to use the consumer library as a dependency in maven.

    <dependency>
        <groupId>com.eksiir.StreamingDataManager</groupId>
        <artifactId>Consumer</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

Consumer API
------------
The consumer API are reflected in the following interfaces and classes:

    StreamingDataControllerFactory and ConsumerControllerFactory
    StreamingDataController        and ConsumerController

See the sample code TODO-1

Using the Consumer as an Independent Application
------------------------------------------------
The <b>package</b> goal of maven creates the <b>target/StreamingDataConsumer-\<version\>.tar.gz</b> which can be installed
on a target host. On that host JAVA_HOME environment variable should be set to use JDK 1.7 or later.

After un-taring, bin/StreamingDataConsumer.sh is the script to run the consumer. The usage syntax is

    $ ./StreamingDataConsumer.sh -help | -config <consumer-config>.xml -action [start|stop|restart]

            -config is optional as without it the defaults will be used
            -action is required

Consumer Configuration
======================
The default configurations can be overwritten by an XML file passed by the -config CLI option.

The <b>StreamingDataConsumer</b> XML configuration consists of the following elements:

StreamingData
-------------
The <u>context</u> attribute of the <u>StreamingData</u> tag is <b>consumer</b>.
The corresponding tags have the following content:

<b>Carrier</b>
The streaming data carrier.
<br>Default: KINESIS

<b>Model</b>
The data model of the streaming data.
<br>Default: ORDER

<b>Delimiter</b>
The delimiter character used in the data model.
<br>Default is the pipe character: |

<b>ConsumerProcessor</b>
The streaming data consumer processor.
<br>Default: KINESIS_REDSHIFT_CONNECTOR

Kinesis
-------
This element is used only when StreamingData.Carrier is KINESIS. The corresponding tags have the following content:

<b>Region</b>
The AWS region of the Kinesis service.
<br>Default: us-east-1

<b>EndPoint</b>
The AWS Kinesis endpoint.
<br>Default: https://kinesis.us-east-1.amazonaws.com

<b>CredentialsPropertiesFileName</b>
The AWS credential filename required for the Kinesis service.
<br>Default: AwsCredentials.properties
<br><u>This file must NOT be added to GIT or shared under any circumstances.</u>

<b>StreamName</b>
The Kinesis stream name.
<br>Default: Orders

<b>Consumer.CharacterSet</b>
Character set of the streaming data.
<br>Default: UTF-8

<b>Consumer.UniqueApplicationName</b>
It is the Kinesis requirement that every consumer application has to have a unique name. This name is used internally by
 AWS as that application's check-pointing DynamoDB table name.  It is the responsibility of the consumer application to
 ensure the uniqueness of its name.
<br>Default: Orders-KINESIS_REDSHIFT_CONNECTOR-LATEST

<b>Consumer.InitialPositionInStream</b>
The position in the Kinesis stream to start consuming data. The possible values are: TRIM_HORIZON, AT_SEQUENCE_NUMBER,
AFTER_SEQUENCE_NUMBER, LATEST.
<br>Default: LATEST

<b>Consumer.MinRecordsPerBuffer</b>
The Kinesis shard data is made available to the consumer either if the MinRecordsPerBuffer or the MinBytesPerBuffer is
received in the shard.
<br>Default: 1000

<b>Consumer.MinBytesPerBuffer</b>
The Kinesis shard data is made available to the consumer either if the MinRecordsPerBuffer or the MinBytesPerBuffer is
received in the shard.
<br>Default: 1048576

<b>Consumer.MaxRecordsNotCheckPointed</b>
Kinesis shard data is check pointed either if the MaxRecordsNotCheckPointed is reached or the
CheckpointIntervalMillis is elapsed.
<br>Default: 1000

<b>Consumer.CheckpointIntervalMillis</b>
Kinesis shard data is check pointed either if the MaxRecordsNotCheckPointed is reached or the
CheckpointIntervalMillis is elapsed.
<br>Default: 60000

<b>Consumer.NumRetries</b>
Max number of retries to get data in case of recoverable errors.
<br>Default: 10

<b>Consumer.BackOffTimeMillis</b>
Back off time in milliseconds between retries to get data in case of recoverable errors.
<br>Default: 3000

<b>Connector.Type</b>
Kinesis connector type. Possible values are DynamoDB, Redshift, S3.
<br>Default: Redshift

<b>Connector.S3Bucket</b>
The S3 bucket name used internally by the Kinesis connector.
<br>Default: streaming-data

Redshift
--------
This element is used only when StreamingData.Connector.Type is Redshift. It is assumed and it must be the case that the
AWS region is the same as that of the <b>Kinesis</b> element.

The corresponding tags have the following content:

<b>EndPoint</b>
The AWS Redshift endpoint.
<br>Default: https://redshift.us-east-1.amazonaws.com

<b>CredentialsFileName</b>
The AWS credential filename required for the Redshift service.
<br>Default: RedshiftCredentials.properties
<br><u>This file must NOT be added to GIT or shared under any circumstances.</u>

<b>TableName</b>
The name of the Redshift table.
<br>Default: kinesis_test

S3
---
This element is used only when StreamingData.Connector.Type is Redshift. It is assumed and it must be the case that the
AWS region is the same as that of the <b>Kinesis</b> element.

The corresponding tags have the following content:

<b>EndPoint</b>
The AWS S3 endpoint.
<br>Default: https://s3.amazonaws.com
