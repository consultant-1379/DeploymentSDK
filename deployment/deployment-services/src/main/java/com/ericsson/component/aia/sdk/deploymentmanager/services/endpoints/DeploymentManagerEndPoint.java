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

package com.ericsson.component.aia.sdk.deploymentmanager.services.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ericsson.component.aia.sdk.deploymentmanager.api.DeploymentManager;

/**
 *
 * End-points Definition for the core Deployment services Module.
 *
 * @author EMILAWL
 *
 */
@CrossOrigin
@Controller
public class DeploymentManagerEndPoint {

    @Autowired
    private DeploymentManager deploymentManager;

    /**
     * The rest end point for deploying an application.
     *
     *
     * @param userID
     *            The user who is requesting to activate an application
     * @param environmentID
     *            The environment that the application will be instantiated in
     * @param applicationID
     *            The application to be brought online
     * @return response
     */
    @RequestMapping(value = "/dm/deploy", method = RequestMethod.POST)
    public ResponseEntity<String> deployApplication(@RequestParam("userID") final String userID,
                                                    @RequestParam("environmentID") final String environmentID,
                                                    @RequestParam("applicationID") final String applicationID) {
        final String status = deploymentManager.deployApplication(userID, environmentID, applicationID);
        final ResponseEntity<String> response = new ResponseEntity<String>(status, HttpStatus.OK);
        return response;
    }

    /**
     * This is the end point for un-deploying the application.
     *
     * @param userID
     *            The user who is requesting to remove an application
     * @param environmentID
     *            The environment that the application will be removed from
     * @param applicationID
     *            The application to be removed
     * @return response
     */
    @RequestMapping(value = "/dm/undeploy", method = RequestMethod.POST)
    public ResponseEntity<String> undeployApplication(@RequestParam("userID") final String userID,
                                                      @RequestParam("environmentID") final String environmentID,
                                                      @RequestParam("applicationID") final String applicationID) {
        final String status = deploymentManager.unDeployApplication(userID, environmentID, applicationID);
        final ResponseEntity<String> response = new ResponseEntity<String>(status, HttpStatus.OK);
        return response;
    }
    /**
     * This is the end point for un-discover a cluster spec.
     *
     * @param userID
     *            The user who is requesting to remove an application
     * @param environmentID
     *            The environment that the application will be removed from
     * @return response
     */
    @RequestMapping(value = "/dm/discover", method = RequestMethod.POST)
    public ResponseEntity<String> discoverClusterInfo(@RequestParam("userID") final String userID,
                                                      @RequestParam("environmentID") final String environmentID) {
        final String status = deploymentManager.discoverClusterInfo(userID, environmentID);
        final ResponseEntity<String> response = new ResponseEntity<String>(status, HttpStatus.OK);
        return response;
    }
}
