package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata;

public enum JobPolicy {

    /**
     * Restarts the container on failure.
     */
    RESTART_ON_FAILURE,

    /**
     * Does not restart the container on failure.
     */
    NEVER_RESTART,

    /**
     * Always Restart
     */
    ALWAYS
}