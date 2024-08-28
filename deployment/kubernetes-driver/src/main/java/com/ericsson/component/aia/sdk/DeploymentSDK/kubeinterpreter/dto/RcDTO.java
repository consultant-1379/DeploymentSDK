package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.internal.SerializationUtils;

/**
 * @author elesmac
 */

/**
 * Class encompasses a {@link ReplicationController}
 */
public class RcDTO {

    ReplicationController replicationController;

    /**
     * Constructs a ReplicationController that encompasses a {@link ReplicationController}.
     *
     * @param replicationController - the replication controller
     */
    public RcDTO(final ReplicationController replicationController) {
        this.replicationController = replicationController;

    }

    /**
     * Gets the Job that was generated from the pba.
     *
     * @return - {@link ReplicationController}
     */
    public ReplicationController getReplicationController() {
        return replicationController;
    }

    /**
     * Gets this Pod definition as kubernetes Yaml file.
     *
     * @return - kubernetes yaml manifest as String.
     * @throws JsonProcessingException - if the processing of the pba instance fails.
     */
    public String getRCSpecAsYaml() throws JsonProcessingException {
        return SerializationUtils.dumpAsYaml(replicationController);
    }

}


