package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto;



/**
 * @author elesmac
 */
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.internal.SerializationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Class encompasses a {@link Pod}
 */
public class PodDTO {

    final Pod pod;

    /**
     * Constructs a Job that encompasses a {@link Pod}.
     *
     * @param pod - the pod
     */
    public PodDTO(final Pod pod) {
        this.pod = pod;

    }

    /**
     * Gets the Job that was generated from the pba.
     *
     * @return - {@link Pod}
     */
    public Pod getPod() {
        return pod;
    }

    /**
     * Gets this Pod definition as kubernetes Yaml file.
     *
     * @return - kubernetes yaml manifest as String.
     * @throws JsonProcessingException - if the processing of the pba instance fails.
     */
    public String getPodSpecAsYaml() throws JsonProcessingException {
        return SerializationUtils.dumpAsYaml(pod);
    }

}


