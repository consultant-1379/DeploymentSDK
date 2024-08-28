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
package com.ericsson.component.aia.sdk.deploymentmanager.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.mockito.Mock;

import com.ericsson.aia.metadata.api.MetaDataServiceIfc;
import com.ericsson.component.aia.sdk.deploymentmanager.DeploymentManagerImpl;
import com.ericsson.component.aia.sdk.deploymentmanager.api.DeploymentManager;

/**
 * The <code>DeploymentClientTest</code> is a test client for the DeploymentManager.
 */
public class DeploymentClientTest {

    private static final String CURRENT_PATH = Paths.get(".").toAbsolutePath().normalize().toString();

    @Mock
    private static MetaDataServiceIfc metaDataServiceManager;

    /**
     * DeploymentManager Client test.
     *
     * @param args
     *            - args supplied through command line
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final DeploymentManager deploymentManager = DeploymentManagerImpl.builder().applicationCatalogName("applications")
                .metaDataServiceManager(metaDataServiceManager).build();

        //final boolean result = deploymentManager.deployApplication(readFileAsString(new File(CURRENT_PATH + "/src/test/resources/pba.json")));
        final String result = deploymentManager.deployApplication("simple_User", "kube://IP", "APP_IP");

        System.out.println("Deployment successful: " + result);
    }

    private static String readFileAsString(final File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
    }

}
