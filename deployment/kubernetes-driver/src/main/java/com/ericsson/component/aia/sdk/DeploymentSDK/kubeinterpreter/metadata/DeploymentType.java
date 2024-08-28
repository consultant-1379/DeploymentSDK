package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata;

public enum DeploymentType {

    /**
     * Standcluster signifies that the manifest built will be used to deploy the cluster & the application as a standalone application.
     */
    DEPLOYMENT,

    /**
     * Schedule signifies that the manifest build will be used to deploy the container on an existing cluster.
     */
    SCHEDULE,
    /**
     * Single Pod creation specification
     */
    SINGLE;
}
