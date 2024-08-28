package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata;

import io.fabric8.kubernetes.api.model.ObjectMeta;

public class DeploymentMetaData extends ObjectMeta {

    final JobType jobType;

    final DeploymentType deployType;

    final JobPolicy jobPolicy;

    /**
     * Constucts a Meta data object that describes the needed manifest.
     *
     * @param jType   - {@link JobType} type of the job.
     * @param dType   - {@link DeploymentType} type of the deployment.
     * @param jPolicy - {@link JobPolicy} job restart policy.
     */
    public DeploymentMetaData(final JobType jType, final DeploymentType dType, final JobPolicy jPolicy/* , final SharedVolumesDTO volsDto */) {
        jobType = jType;
        deployType = dType;
        jobPolicy = jPolicy;
    }

    /**
     * Gets the deployment type.
     *
     * @return - {@code DeploymentType}
     */
    public DeploymentType getDeployType() {
        return deployType;
    }

    /**
     * Gets the job Policy
     *
     * @return - {@code JobPolicy}
     */
    public JobPolicy getJobPolicy() {
        return jobPolicy;
    }

    /**
     * Gets the job type.
     *
     * @return - {@code JobType}
     */
    public JobType getJobType() {
        return jobType;
    }

}

