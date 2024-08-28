package com.ericsson.component.aia.sdk.environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.sdk.DeploymentSDK.kube.environment.SimpleKubernetesEnvironmentPlugin;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentMetadata;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentPlugin;
import com.ericsson.component.aia.sdk.deploymentmanager.exceptions.SdkDeploymentManagerException;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;

/**
 *
 * A factory for the supported environments
 *
 * @author emilawl
 *
 */
public class EnvironmentFactory {

    private static final Logger Log = LoggerFactory.getLogger(EnvironmentFactory.class);

    /**
     *
     * @param envData
     *            The environment metadata to be used to create the environment pulgin
     * @param pba
     *            The deployment info to be used by the plugin
     * @return EnvironmentPlugin
     */
    public EnvironmentPlugin getEnvironment(final EnvironmentMetadata envData, final PBAInstance pba) {

        switch (envData.getEnvironmentType()) {
            case "K":
                Log.info("Creating a new kubernetes deployer for: {} into environment {}", pba.getPba().getApplicationInfo().getName(),
                        envData.getEnvironmentId());
                return new SimpleKubernetesEnvironmentPlugin(pba, envData);

            case "MM":
                Log.info("Created a new Marathon Mesos deployer for: {} into environment {}", pba.getPba().getApplicationInfo().getName(),
                        envData.getEnvironmentId());
                return new SimpleMarathonMesosEnvironment(pba, envData);

            case "D":
                Log.info("Created a new dummy deployer for: {} into environment {}", pba.getPba().getApplicationInfo().getName(),
                        envData.getEnvironmentId());
                return new DummyEnvironment(pba, envData);

            default:
                throw new SdkDeploymentManagerException("Failed to find a valid enrironment to deploy into");
        }
    }

    /**
     *
     * @param environmentId
     *            The environment to be used to manage an application
     * @return the EnvironmentMetadata
     */
    public EnvironmentMetadata getEnvironmentMetadata(final String environmentId) {

        final int environmentIdPosition = 0;
        final int environmentTypePosition = 2;
        final int iPPosition = 1;
        final int tokenPosition = 3;
        final int userNamePosition = 4;
        final int userCertPosition = 5;
        final int httpsCert = 6;

        final String[] environmentMetadata = environmentId.split(",");

        final EnvironmentMetadata envData = new EnvironmentMetadata();

        envData.setEnvironmentId(environmentMetadata[environmentIdPosition]);
        envData.setEnvironmentType(environmentMetadata[environmentTypePosition]);
        envData.setIP(environmentMetadata[iPPosition]);
        envData.setToken(environmentMetadata[tokenPosition]);
        envData.setUserName(environmentMetadata[userNamePosition]);
        envData.setUserCert(environmentMetadata[userCertPosition]);
        envData.setHttpsCert(environmentMetadata[httpsCert]);

        return envData;
    }

}
