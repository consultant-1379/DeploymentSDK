/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin;

/**
 * Provides the interface that an environment will work towards. Environments will provide a method to deploy and un-deploy an application as well as
 * provide a translation for the common deployment information format to the native environment format.
 */
public interface EnvironmentPlugin {

    /**
     * Basic place holder for now
     *
     * @return status
     *
     */
    String engageDeployment();

    /**
     * Basic place holder for now
     *
     * @return status
     *
     */
    String engageUnDeployment();

    /**
     * Basic place holder for now
     *
     * @return is successful
     *
     */
    boolean engageLocalEnvTranslation();

    /**
     * Basic place holder for now
     *
     * @param environmentMetadata
     *            This is a parameter for env to discover
     * @return is successful
     */
    String discoverClusterInfo(EnvironmentMetadata environmentMetadata);

}
