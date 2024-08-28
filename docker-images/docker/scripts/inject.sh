#!/bin/bash
# Look for deployment manager config settings in the environment.
for c in `printenv | grep DEP_MAN_MONGO | awk -F "=" '{print $1}'`; do
    var=${c}
    value=${!var}
    echo "Setting deployment manager property $var=$value"
    sed -i -e "s/${var}/${value}/g" /opt/ericsson/deployment_manager/config/metadataservice-config.properties
done
