#!bin/bash

/opt/ericsson/deployment_manager/scripts/inject.sh
cat /opt/ericsson/deployment_manager/config/metadataservice-config.properties
java -jar /opt/ericsson/deployment_manager/deployment-services-*.jar
