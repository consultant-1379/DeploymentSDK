package com.ericsson.component.aia.sdk.deploymentmanager.exceptions;

/**
 *
 * Exception class for the deployment manager
 *
 * @author emilawl
 *
 */
public class SdkDeploymentManagerException extends RuntimeException {

    /**
     * Generated SerialVersionUID for SkdDeploymentManagerException
     */
    private static final long serialVersionUID = 963460696589791026L;

    /**
     * Instantiates a new deployment manager exception.
     */
    public SdkDeploymentManagerException() {
        super();
    }

    /**
     * Instantiates a new deployment manager exception.
     *
     * @param throwable
     *            the throwable
     */
    public SdkDeploymentManagerException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Instantiates a new deployment manager exception.
     *
     * @param message
     *            the message
     */
    public SdkDeploymentManagerException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new deployment manager exception.
     *
     * @param message
     *            the message
     * @param throwable
     *            the throwable
     */
    public SdkDeploymentManagerException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
