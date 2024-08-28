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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.aia.metadata.api.MetaDataServiceIfc;
import com.ericsson.aia.metadata.exception.MetaDataServiceException;
import com.ericsson.component.aia.sdk.deploymentmanager.api.DeploymentManager;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentMetadata;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentPlugin;
import com.ericsson.component.aia.sdk.deploymentmanager.exceptions.SdkDeploymentManagerException;
import com.ericsson.component.aia.sdk.environment.EnvironmentFactory;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;
import com.ericsson.component.aia.sdk.pba.tools.PBASchemaTool;

/**
 * The <code>DeploymentManagerImpl</code> is an implementation class for all the DeploymentManager's related operations.
 */
public class DeploymentManagerImpl implements DeploymentManager {

    /** Logger for DeploymentManagerImpl. */
    private static final Logger Log = LoggerFactory.getLogger(DeploymentManagerImpl.class);

    /** The meta data service manager. */
    private final MetaDataServiceIfc metaDataServiceManager;
    private final String applicationCatalogName;

    /** The pba schema tool. */
    private final PBASchemaTool pbaSchemaTool;

    /**
     * Create a new Deployment manage
     *
     * @param metaDataServiceManager
     *            The meta data service to be used to resolve applications and dependencies
     * @param applicationCatalogName
     *            The catalog to use in the meta data service
     * @param pbaSchemaTool
     *            The pba schema tool
     */
    public DeploymentManagerImpl(final MetaDataServiceIfc metaDataServiceManager, final String applicationCatalogName,
                                 final PBASchemaTool pbaSchemaTool) {
        this.metaDataServiceManager = metaDataServiceManager;
        this.applicationCatalogName = applicationCatalogName;
        this.pbaSchemaTool = pbaSchemaTool;
    }

    @Override
    public String deployApplication(final String userID, final String environmentId, final String applicationID) {

        Log.info("\n\n A deployment for: {}, {}, {}  has been requested. Proccessing of request will now start.\n\n", userID, environmentId,
                applicationID);

        //TODO connect to the metastore and get the resolved Deps for the application.

        final PBAInstance pba = lookupPbaUsingApplicationId(applicationID);
        final String status = translateAndDeployForSpecifiedEnvironment(environmentId, pba);

        return status;
    }

    private String translateAndDeployForSpecifiedEnvironment(final String environmentId, final PBAInstance pba) {
        final EnvironmentPlugin deploymentEnvironment = getEnvironment(environmentId, pba);

        Log.info("Creating local config files for the required environment");
        String status = "";
        if (deploymentEnvironment.engageLocalEnvTranslation()) {
            Log.info("Environment translation complete now deploying application");
            status = deploymentEnvironment.engageDeployment();
        } else {
            throw new SdkDeploymentManagerException("Failed to translate to local environment configuration");
        }
        return status;
    }

    @Override
    public String unDeployApplication(final String userID, final String environmentId, final String applicationID) {

        Log.info("\n\n An action to undeploy for: {}, {}, {}  has been requested. Proccessing of request will now start.\n\n", userID, environmentId,
                applicationID);

        //TODO connect to the metastore and get the resolved Deps for the application.

        final PBAInstance pba = lookupPbaUsingApplicationId(applicationID);
        final String status = translateAndUnDeployForSpecifiedEnvironment(environmentId, pba);

        return status;
    }
    @Override
    public String discoverClusterInfo(final String userID, final String environmentId)  {

        Log.info("\n\n Discovery of cluster for ---------: {}, {}, {} \n\n", userID, environmentId);
        Log.info("\n\n An action to discover for: {}, {}, {}  has been requested. Proccessing of request will now start.\n\n", userID, environmentId);

        //TODO connect to the metastore and get the resolved Deps for the application.

        final EnvironmentPlugin deploymentEnvironment = getEnvironment(environmentId, null);
        final EnvironmentMetadata environmentMetadata = new EnvironmentMetadata();
        environmentMetadata.setEnvironmentId(environmentId);

        Log.info("Creating local config files for the required environment");

        final String status = deploymentEnvironment.discoverClusterInfo(environmentMetadata);
        return status;
    }

    private String translateAndUnDeployForSpecifiedEnvironment(final String environmentId, final PBAInstance pba) {

        final EnvironmentPlugin deploymentEnvironment = getEnvironment(environmentId, pba);

        Log.info("Creating local config files for the required environment");
        String status = "";
        if (deploymentEnvironment.engageLocalEnvTranslation()) {
            Log.info("Environment translation complete now removing application");
            status = deploymentEnvironment.engageUnDeployment();
        } else {
            throw new SdkDeploymentManagerException("Failed to translate to local environment configuration");
        }
        return status;
    }

    private PBAInstance lookupPbaUsingApplicationId(final String applicationID) {
        try {
            Log.info("Getting the PBA from the application catalog {}", applicationCatalogName);
            final String pbaAsString = metaDataServiceManager.get(applicationCatalogName, applicationID);
            Log.info("\nGenerating the PBA instance using {}\n", pbaAsString);
            return pbaSchemaTool.getPBAModelInstance(pbaAsString);
        } catch (MetaDataServiceException e) {
            throw new SdkDeploymentManagerException("Something happened to the PBA conversion", e);
        }
    }

    private EnvironmentPlugin getEnvironment(final String environmentId, final PBAInstance pba) {
        final EnvironmentFactory env = new EnvironmentFactory();
        final EnvironmentMetadata envData = env.getEnvironmentMetadata(environmentId);
        return env.getEnvironment(envData, pba);

    }

    /**
     * This method will return a {@link DeploymentManagerImplBuilder} instance.
     *
     * @return a {@link DeploymentManagerImplBuilder} instance.
     */
    public static DeploymentManagerImplBuilder builder() {
        return new DeploymentManagerImplBuilder();
    }

    /**
     * Builder class for creating {@link DeploymentManagerImpl}
     *
     * @author emilawl
     *
     */
    public static class DeploymentManagerImplBuilder {

        /** The meta data service manager. */
        private MetaDataServiceIfc metaDataServiceManager;
        private String applicationCatalogName;

        /** The pba schema tool. */
        private PBASchemaTool pbaSchemaTool;

        private DeploymentManagerImplBuilder() {

        }

        /**
         * This method will set the {@link MetaDataServiceIfc} reference.
         *
         * @param metaDataServiceManager
         *            {@link MetaDataServiceIfc} reference.
         * @return current {@link DeploymentManagerImplBuilder} reference
         */
        public DeploymentManagerImplBuilder metaDataServiceManager(final MetaDataServiceIfc metaDataServiceManager) {
            this.metaDataServiceManager = metaDataServiceManager;
            return this;
        }

        /**
         * This method will set the {@link applicationCatalogName} reference.
         *
         * @param applicationCatalogName
         *            applicationCatalogName.
         * @return current {@link DeploymentManagerImplBuilder} reference
         */
        public DeploymentManagerImplBuilder applicationCatalogName(final String applicationCatalogName) {
            this.applicationCatalogName = applicationCatalogName;
            return this;
        }

        /**
         * This method will set the {@link PBASchemaTool} reference.
         *
         * @param pbaSchemaTool
         *            {@link PBASchemaTool} reference.
         * @return current {@link DeploymentManagerImplBuilder} reference
         */
        public DeploymentManagerImplBuilder pbaSchemaTool(final PBASchemaTool pbaSchemaTool) {
            this.pbaSchemaTool = pbaSchemaTool;
            return this;
        }

        /**
         * This method will create new instance of {@link DeploymentManagerImpl}.
         *
         * @return new instance of {@link DeploymentManagerImpl}.
         */
        public DeploymentManagerImpl build() {

            if (metaDataServiceManager == null) {
                throw new SdkDeploymentManagerException("MetaDataServiceIfc is null");
            }
            if (applicationCatalogName == null) {
                throw new SdkDeploymentManagerException("applicationCatalogName is null");
            }
            if (pbaSchemaTool == null) {
                throw new SdkDeploymentManagerException("PBASchemaTool is null");
            }
            return new DeploymentManagerImpl(metaDataServiceManager, applicationCatalogName, pbaSchemaTool);
        }

    }

}
