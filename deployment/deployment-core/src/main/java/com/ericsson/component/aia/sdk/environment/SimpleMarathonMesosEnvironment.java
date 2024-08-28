package com.ericsson.component.aia.sdk.environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentMetadata;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentPlugin;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;

/**
 *
 * Generated to expand the managerImpl to provide an exampl of including an alternitive Deployer should be implemented in the Marathon Mesos Driver
 *
 * @author emilawl
 *
 */
public class SimpleMarathonMesosEnvironment implements EnvironmentPlugin {

    private static final Logger Log = LoggerFactory.getLogger(SimpleMarathonMesosEnvironment.class);
    private static final String message = "No implementition yet supplied for the Marathon Mesos Environment, will always fail";
    private final PBAInstance pba;
    private final EnvironmentMetadata envData;

    /**
     *
     * @param pba
     *            The PBA
     * @param envData
     *            The environment Metadata
     */
    public SimpleMarathonMesosEnvironment(final PBAInstance pba, final EnvironmentMetadata envData) {
        this.pba = pba;
        this.envData = envData;
        Log.error(message + ", pba {}, envData {}", this.pba, this.envData);
    }

    @Override
    public String engageDeployment() {
        Log.error(message);
        return message;
    }

    @Override
    public boolean engageLocalEnvTranslation() {
        Log.error(message);
        return false;
    }

    @Override
    public String engageUnDeployment() {

        Log.error(message);
        return message;
    }

    @Override
    public String discoverClusterInfo(final EnvironmentMetadata environmentMetadata) {

        Log.error(message);
        return message;
    }
}
