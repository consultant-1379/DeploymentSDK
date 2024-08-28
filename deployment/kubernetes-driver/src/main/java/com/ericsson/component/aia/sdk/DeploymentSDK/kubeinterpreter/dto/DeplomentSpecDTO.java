package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.client.internal.SerializationUtils;

import java.util.Map;

/**
 * Creates a DeploymentDescription  spec in kubernetes YAML format.
 *
 * @author elesmac
 */
public class DeplomentSpecDTO {
    final Map<RcDTO, SvcDTO> DeploymentSpec;

    /**
     * Constructs a {@link Namespace} using the name. Clients can use this to create full deployment description on the kubernetes cluster.
     * @param deploymentSpec
     */
    public DeplomentSpecDTO(final Map<RcDTO, SvcDTO> deploymentSpec) {


        DeploymentSpec = deploymentSpec;
    }

    /**
     * Gets the Map of replication controller and service.
     *
     * @return - DeploymentSpecification object.
     */
    public Map<RcDTO, SvcDTO> getDeploymentSpec() {
        return DeploymentSpec;
    }
    public String getRCSpecAsYaml() throws JsonProcessingException {
        return SerializationUtils.dumpAsYaml((HasMetadata) getDeploymentSpec());
    }
}
