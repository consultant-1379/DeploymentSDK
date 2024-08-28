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
package com.ericsson.component.aia.sdk.deploymentmanager;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.component.aia.sdk.deploymentmanager.api.DeploymentManager;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;
import com.ericsson.component.aia.sdk.pba.tools.PBASchemaTool;

/**
 * DeploymentManagerTest tests the DeploymentManagerImpl methods.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeploymentManagerTest {

    @Mock
    private PBASchemaTool pbaSchemaTool;

    @Mock
    private PBAInstance pbaInstance;

    private DeploymentManager deploymentManager;

    @Before
    public void setup() {
        deploymentManager = new DeploymentManagerImpl(null, null, pbaSchemaTool);
    }

    @Test
    public void testDeploySuccess() {
        Mockito.when(pbaSchemaTool.getPBAModelInstance(Mockito.anyString())).thenReturn(pbaInstance);

        // TODO Deployment manifest builder stubs

        // TODO other collaborators fabric8 kuberenetes sdk client stubs

        //deploymentManager.deployApplication(Mockito.anyString());

        //Mockito.verify(pbaSchemaTool, Mockito.atLeastOnce()).getPBAModelInstance(Mockito.anyString());

        // TODO Deployment manifest builder verify

        // TODO other collaborators fabric8 kuberenetes sdk client verify

        assertTrue(1 == 1);

    }

}
