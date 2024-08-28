/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.sdk.deploymentmanager.api;

/**
 * The <code>DeploymentManager</code> interface is a generic interface providing all applications related operations like deploying ..etc.
 */
public interface DeploymentManager {

    /**
     * <p>
     * This method will deploy an application.
     * </p>
     *
     * @param userID
     *            <p>
     *            user identification for application deployment.
     *            </p>
     * @param environmentID
     *            <p>
     *            target environment identification where application will be deployed.
     *            </p>
     * @param applicationID
     *            <p>
     *            identification for the application that should be deployed.
     *            </p>
     * @return
     *         <p>
     *         status of the deployment.
     *         </p>
     */
    String deployApplication(String userID, String environmentID, String applicationID);

    /**
     * <p>
     * This method will deploy an application.
     * </p>
     *
     * @param userID
     *            <p>
     *            user identification for application removal.
     *            </p>
     * @param environmentID
     *            <p>
     *            target environment identification where application will be removed.
     *            </p>
     * @param applicationID
     *            <p>
     *            identification for the application that should be removed.
     *            </p>
     * @return
     *         <p>
     *         status of the deployment.
     *         </p>
     */
    String unDeployApplication(String userID, String environmentID, String applicationID);
    /**
     * <p>
     * This method will discover cluster spec
     * </p>
     *
     * @param userID
     *            <p>
     *            user identification for cluster connection.
     *            </p>
     * @param environmentID
     *            <p>
     *            target environment for discovery.
     *            </p>
     * @return
     *         <p>
     *         status of discovery.
     *         </p>
     */
    String discoverClusterInfo(String userID, String environmentID);

}
