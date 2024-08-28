FROM armdocker.rnd.ericsson.se/aia/base/java
 
COPY deployment-services-${project.version}.jar /opt/ericsson/deployment_manager/
COPY config/external_application.properties /opt/ericsson/deployment_manager/config/
COPY config/metadataservice-config.properties /opt/ericsson/deployment_manager/config/
COPY scripts/inject.sh /opt/ericsson/deployment_manager/scripts/
COPY scripts/entrypoint.sh /opt/ericsson/deployment_manager/scripts/

run chmod +x /opt/ericsson/deployment_manager/scripts/inject.sh && chmod +x /opt/ericsson/deployment_manager/scripts/entrypoint.sh

cmd ["/opt/ericsson/deployment_manager/scripts/entrypoint.sh"]