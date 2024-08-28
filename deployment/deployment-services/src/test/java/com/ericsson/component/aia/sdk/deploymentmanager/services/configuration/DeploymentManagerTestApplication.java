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
package com.ericsson.component.aia.sdk.deploymentmanager.services.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.ericsson.aia.metadata.api.MetaDataServiceIfc;
import com.ericsson.aia.metadata.exception.MetaDataServiceException;
import com.ericsson.aia.metadata.lifecycle.MetaDataServiceLifecycleManagerIfc;
import com.ericsson.aia.metadata.lifecycle.impl.MetaDataServiceLifecycleManagerImpl;
import com.ericsson.component.aia.sdk.deploymentmanager.DeploymentManagerImpl;
import com.ericsson.component.aia.sdk.deploymentmanager.api.DeploymentManager;
import com.ericsson.component.aia.sdk.deploymentmanager.service.configuration.DeploymentManagerApplication;
import com.ericsson.component.aia.sdk.pba.tools.PBASchemaTool;

/**
 * This will define beans needed for this application to run.
 *
 * @author echchik
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class })
@ComponentScan("com.ericsson.component.aia.sdk.deploymentmanager.services")
public class DeploymentManagerTestApplication {

    private static final Logger Log = LoggerFactory.getLogger(DeploymentManagerTestApplication.class);

    @Value("${meta.data.service.config.file}")
    private String metaDataServiceConfigFile;

    @Value("${application.catalog.name}")
    private String applicationCatalogName;

    /**
     * This method creates the {@link DeploymentManager} bean.
     *
     * @return {@link DeploymentManager} bean
     * @throws IOException
     *             if {@link MetaDataServiceIfc} bean creation fails.
     * @throws MetaDataServiceException
     *             if {@link MetaDataServiceIfc} bean creation fails.
     */
    @Bean
    public DeploymentManager deploymentManager() throws MetaDataServiceException, IOException {
        return DeploymentManagerImpl.builder().applicationCatalogName(applicationCatalogName).metaDataServiceManager(metaDataServiceIfc())
                .pbaSchemaTool(pbaSchemaTool()).build();
    }

    /**
     * This method creates {@link MetaDataServiceIfc} bean.
     *
     * @return {@link MetaDataServiceIfc} bean.
     * @throws MetaDataServiceException
     *             if {@link MetaDataServiceIfc} bean creation fails.
     * @throws IOException
     *             if {@link MetaDataServiceIfc} bean creation fails.
     */
    @Bean
    public MetaDataServiceIfc metaDataServiceIfc() throws MetaDataServiceException, IOException {
        final MetaDataServiceLifecycleManagerIfc serviceLifecycleManager = new MetaDataServiceLifecycleManagerImpl();
        final Properties properties = new Properties();
        try (final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(metaDataServiceConfigFile)) {
            properties.load(stream);
        }
        serviceLifecycleManager.provisionService(properties);
        final MetaDataServiceIfc metaDataService = serviceLifecycleManager.getServiceReference();
        if (!metaDataService.schemaExists(applicationCatalogName)) {
            metaDataService.createSchema(applicationCatalogName);
        }
        return metaDataService;
    }

    /**
     * This method creates {@link PBASchemaTool} bean.
     *
     * @return {@link PBASchemaTool} bean.
     */
    @Bean
    public PBASchemaTool pbaSchemaTool() {
        return new PBASchemaTool();
    }

    /**
     * Starting point of the application
     *
     * @param args
     *            arguments for the application.
     */
    public static void main(final String[] args) {
        SpringApplication.run(DeploymentManagerApplication.class, args);
    }
}
