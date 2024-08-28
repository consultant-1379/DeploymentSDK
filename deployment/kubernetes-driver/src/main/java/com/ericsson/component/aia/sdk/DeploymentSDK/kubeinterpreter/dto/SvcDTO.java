/**
 *
 */
package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto;

/**
 * @author Elesmac
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import io.fabric8.kubernetes.api.model.Job;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.internal.SerializationUtils;

/**
 * Class encompasses a {@link Job}
 */
public class SvcDTO {

    final Service svc;

    /**
     * Constructs a Job that encompasses a {@link Job}.
     *
     * @param svc
     */
    public SvcDTO(final Service svc) {
        this.svc = svc;

    }

    /**
     * Gets the Job that was generated from the pba.
     *
     * @return - {@link Service}
     */
    public Service getSvc() {
        return svc;
    }

    /**
     * Gets this Job as kubernetes Yaml file.
     *
     * @return - kubernetes yaml manifest as String.
     * @throws JsonProcessingException - if the processing of the pba instance fails.
     */
    public String getSvcSpecAsYaml() throws JsonProcessingException {
        return SerializationUtils.dumpAsYaml(svc);
    }

}
