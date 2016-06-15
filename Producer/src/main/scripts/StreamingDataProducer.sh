#!/bin/bash

#
# This script invokes the streaming data producer application.
#
# Requirements:
#     Sun JDK 1.7
#

create_classpath() {
    CLASS_PATH=
    for LIB in $(find $LIB_PATH -name "*.jar" -print)
    do
        CLASS_PATH=$LIB:$CLASS_PATH
    done
}

PATH=/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin
set +f						# Turn on shell globbing

PROGRAM_ARGUMENTS=$@
LIB_PATH=$(dirname $0)/../lib
JAVA=$JAVA_HOME/bin/java
MAIN_CLASS=com.eksiir.StreamingDataManager.Producer.ProducerController

# For remote debugging
#VM_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
VM_OPTIONS=

create_classpath

$JAVA $VM_OPTIONS -cp $CLASS_PATH $MAIN_CLASS $PROGRAM_ARGUMENTS

exit 0
