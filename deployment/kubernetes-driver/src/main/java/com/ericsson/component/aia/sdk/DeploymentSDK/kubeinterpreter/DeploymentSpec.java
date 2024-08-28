package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter;

import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.JobDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.PodDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.RcDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.SvcDTO;

public class DeploymentSpec {

    SvcDTO service;
    RcDTO replicationController;
    JobDTO job;
    PodDTO pod;

    public SvcDTO getService() {
        return service;
    }

    public void setService(final SvcDTO service) {
        this.service = service;
    }
    public JobDTO getJob() {
        return job;
    }

    public void setJob(final JobDTO job) {
        this.job = job;
    }
    public PodDTO getPod() {
        return pod;
    }

    public void setPod(final PodDTO pod) {
        this.pod = pod;
    }
    public RcDTO getReplicationController() {
        return replicationController;
    }

    public void setReplicationController(final RcDTO replicationController) {
        this.replicationController = replicationController;
    }

}
