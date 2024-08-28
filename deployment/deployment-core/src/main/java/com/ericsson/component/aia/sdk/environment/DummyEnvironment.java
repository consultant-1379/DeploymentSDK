package com.ericsson.component.aia.sdk.environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentMetadata;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentPlugin;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;

/**
 * For testing and demo
 *
 * TODO move to a dummy-driver to have a more complete tutorial
 *
 * @author emilawl
 *
 */
public class DummyEnvironment implements EnvironmentPlugin {

    private static final Logger Log = LoggerFactory.getLogger(DummyEnvironment.class);

    private final PBAInstance pba;
    private final EnvironmentMetadata envData;

    /**
     *
     * @param pba
     *            The pab
     * @param envData
     *            The envData
     */
    public DummyEnvironment(final PBAInstance pba, final EnvironmentMetadata envData) {
        this.pba = pba;
        this.envData = envData;
        Log.error("No implementition yet supplied for the Marathon Mesos Deployer, will always fail", this.pba, this.envData);
    }

    @Override
    public String engageDeployment() {
        final String message = "---Hello I am the dummy deplorer I am a happy day case which always returns true, "
                + "so congrats you have deployed an Application---";
        Log.info(message);
        return message;
    }

    @Override
    public boolean engageLocalEnvTranslation() {
        Log.info("---Hello I am the dummy deplorer I am a happy day case which always returns true,"
                + "so congrats you have translated an application---");
        return true;
    }

    @Override
    public String engageUnDeployment() {
        final String message = "---Hello I am the dummy deplorer I am a happy day case which always returns true, "
                + "so congrats you have Un-deployed an Application---";
        Log.info(message);
        return message;
    }

    @Override
    public String discoverClusterInfo(final EnvironmentMetadata environmentMetadata) {
        final String message = "---Hello I am the dummy deplorer I am a happy day case which always returns true, "
                + "so congrats you have Un-deployed an Application---";
        Log.error(message);
        return message;
    }

}
