Streaming Data Producer Module
==============================
The input of the streaming data producer is the source of the streaming data.
The output of the producer is the streaming data carrier.

Unit Tests Requirements
=======================
The following are required to run unit tests successfully:

* A valid AWS Kinesis security token is needed.

* /var/log/StreamingDataManager/ directory should exist with proper read/write permissions for the user running the tests.

Build Instructions
===================
The producer can be either an independent executable application or used as a library using the default maven goals.
e.g.

    $ mvn clean install

If the above unit test requirements cannot be met pass -DskipTests as an argument to mvn.

Creates the producer library jar and the independent producer application as
<b>target/StreamingDataProducer-\<version\>.tar.gz</b>.

If the producer library or application uses the Amazon Web Services the proper credentials are required in the environment.
From the sources <b>Producer/src/main/resources/AwsCredentials.properties</b> resource file can be provided with valid
<b>accessKey</b> and <b>secretKey</b>.

<u>This file must NOT be added to GIT or shared under any circumstances.</u>

Execution Instructions
======================

Using the Producer Library
--------------------------
The <b>package</b> goal of maven creates the <b>target/Producer-\<version\>.jar</b>.  This library requires JDK 1.7 or later.

Add the following to the application pom.xml to use the producer library as a dependency in maven.

    <dependency>
        <groupId>com.eksiir.StreamingDataManager</groupId>
        <artifactId>Producer</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

Producer API
------------
The producer API are reflected in the following interfaces and classes:

    StreamingDataControllerFactory and ProducerControllerFactory
    StreamingDataController        and ProducerController

See the sample code TODO-1

Using the Producer as an Independent Application
------------------------------------------------
The <b>package</b> goal of maven creates the target/StreamingDataProducer-\<version\>.tar.gz which can be installed
on a target host. On that host JAVA_HOME environment variable should be set to use JDK 1.7 or later.

After un-taring, bin/StreamingDataProducer.sh is the script to run the producer. The usage syntax is

    $ ./StreamingDataProducer.sh -help | -config <producer-config>.xml -action [start|stop|restart]

            -config is optional as without it the defaults will be used
            -action is required

Producer Configuration
======================
The default configurations can be overwritten by an XML file passed by the -config CLI option.

The <b>StreamingDataProducer</b> XML configuration consists of the following elements:

StreamingData
-------------
The <u>context</u> attribute of the <u>StreamingData</u> tag is <b>producer</b>.
The corresponding tags have the following content:

<b>Source</b>
The input source of the streaming data.
<br>Default: LOG_FILE

<b>Carrier</b>
The streaming data carrier.
<br>Default: KINESIS

LogTailer
---------
This element is used only when StreamingData.Source is LOG_FILE. The corresponding tags have the following content:

<b>Filename</b>
Full pathname of the log file to tail.
<br>Default: /tmp/log.out

<b>MillisecDelay</b>
The delay between checks of the file for new content in milliseconds.
<br>Default: 0

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

<u>This file must NOT be added to GIT or shared under any circumstances.</u>

<b>StreamName</b>
The Kinesis stream name.
<br>Default: Orders
