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
package com.ericsson.component.aia.sdk.DeploymentSDK.kube.environment;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.DeploymentSpec;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.KubeApplicationModel;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.discoverer.KubeDiscovery;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.DeploymentMetaData;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.DeploymentType;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.JobPolicy;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.JobType;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentMetadata;
import com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin.EnvironmentPlugin;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.ServiceAccountBuilder;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.utils.Objects;

/**
 *
 */
public class SimpleKubernetesEnvironmentPlugin implements EnvironmentPlugin {

    private static final Logger Log = LoggerFactory.getLogger(SimpleKubernetesEnvironmentPlugin.class);
    private static final String DEFAULT_NAMESPACE = "default";

    private final EnvironmentMetadata envData;
    private final PBAInstance instance;

    //private Pod pod;
    private Service service;
    private ReplicationController repController;

    /**
     *
     * @param pba
     *            The pba
     * @param envData
     *            THe environment metadata
     */
    public SimpleKubernetesEnvironmentPlugin(final PBAInstance pba, final EnvironmentMetadata envData) {
        this.instance = pba;
        this.envData = envData;
    }

    /**
     *
     * simple translation method
     *
     * @return isTranslated
     */
    @Override
    public boolean engageLocalEnvTranslation() {
        final DeploymentMetaData deploymentMetaData = new DeploymentMetaData(JobType.BATCH, DeploymentType.DEPLOYMENT, JobPolicy.ALWAYS);
        final KubeApplicationModel appModel = new KubeApplicationModel();
        DeploymentSpec appToDeploy = null;
        try {
            appToDeploy = appModel.getApplication(instance, deploymentMetaData);
            Log.info("Translated PBA : \n{}", appModel.getApplicationAsYaml(instance, deploymentMetaData));
        } catch (IOException | ProcessingException e) {
            e.printStackTrace();
        }
        service = appToDeploy.getService().getSvc();
        Log.info("The translated service is:\n{}", service.toString());
        repController = appToDeploy.getReplicationController().getReplicationController();
        Log.info("The translated repcontroller is:\n{}", repController.toString());
        return true;
    }

    /**
     * simple deploment method
     *
     * @return isDeployed
     */
    public String engageDeployment() {

        final KubernetesClient client = createClient();

        try {

            Log.info("Deploying: {}", instance.getPba().getApplicationInfo().getName());

            client.services().inNamespace(DEFAULT_NAMESPACE).createOrReplace(service);
            Log.info("Created the service: {}. Current status is {}", service.getMetadata().getName(), getOrCreatetStatus(service).toString());

            client.replicationControllers().inNamespace(DEFAULT_NAMESPACE).createOrReplace(repController);
            Log.info("Created the repController: {}. Current status is {}", repController.getMetadata().getName(),
                    getOrCreatetStatus(repController).toString());

        } finally {
            client.close();
        }

        return "Service Status:" + getOrCreatetStatus(service).toString() + "RepController Status:" + getOrCreatetStatus(repController).toString();

    }

    /**
     * simple deploment method
     *
     * @return isDeployed
     */
    public String engageUnDeployment() {

        final KubernetesClient client = createClient();

        try {

            Log.info("Un-Deploying: {}", instance.getPba().getApplicationInfo().getName());

            Log.info("The service to delete is: {}.", service.getMetadata().getName(), getOrCreatetStatus(service).toString());
            client.services().inNamespace(DEFAULT_NAMESPACE).delete(service);

            Log.info("The repController to delete is: {}.", repController.getMetadata().getName(), getOrCreatetStatus(repController).toString());
            client.replicationControllers().inNamespace(DEFAULT_NAMESPACE).delete(repController);

        } finally {
            client.close();
        }

        return "Service Status:" + getOrCreatetStatus(service).toString() + "RepController Status:" + getOrCreatetStatus(repController).toString();

    }
    //
    //    public String discoverClusterInfo(final EnvironmentMetadata environmentMetadata) {
    //
    //
    //
    //    }

    public static ReplicationControllerStatus getOrCreatetStatus(final ReplicationController replicationController) {
        Objects.notNull(replicationController, "replicationController");
        ReplicationControllerStatus currentState = replicationController.getStatus();
        if (currentState == null) {
            currentState = new ReplicationControllerStatus();
            replicationController.setStatus(currentState);
        }
        return currentState;
    }

    public static ServiceStatus getOrCreatetStatus(final Service service) {
        Objects.notNull(service, "service");
        ServiceStatus currentState = service.getStatus();
        if (currentState == null) {
            currentState = new ServiceStatus();
            service.setStatus(currentState);
        }
        return currentState;
    }

    public KubernetesClient createClient() {
        final String master = envData.getIP();
        final Config config = new ConfigBuilder().withMasterUrl(master).withCaCertData(envData.getHttpsCert()).withUsername(envData.getUserName())
                .withClientCertData(envData.getUserCert()).withClientKeyData(envData.getToken()).build();

        Log.info("Creating a client for Kubernetes, namespace is currently set as default");
        final KubernetesClient client = new DefaultKubernetesClient(config);

        final Namespace namespace = new NamespaceBuilder().withNewMetadata().withName(DEFAULT_NAMESPACE).endMetadata().build();

        client.namespaces().createOrReplace(namespace);

        final ServiceAccount deploymentServiceAccount = new ServiceAccountBuilder().withNewMetadata().withName("deployment").endMetadata().build();
        client.serviceAccounts().inNamespace(DEFAULT_NAMESPACE).createOrReplace(deploymentServiceAccount);
        return client;
    }

    @Override
    public String discoverClusterInfo(final EnvironmentMetadata environmentMetadata) {

        final KubernetesClient client = createClient();

        try {

            final KubeDiscovery kubeDiscovery = new KubeDiscovery(client);
            kubeDiscovery.getNodes();
            kubeDiscovery.getNodesSpec();
            kubeDiscovery.listPods();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        return "Discover completed";
    }

    public DefaultKubernetesClient createsimpleclient() {
        final Config config = new ConfigBuilder().withMasterUrl("192.168.99.100:8443").withNamespace(DEFAULT_NAMESPACE).withUsername("default")
                .build();
        final DefaultKubernetesClient client = new DefaultKubernetesClient(config);
        try {
            client.getHttpClient().connectionSpecs().isEmpty();
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
        Log.info("Connection created");
        Log.info("Creating a client for Kubernetes, namespace is currently set as default");
        final Namespace namespace = new NamespaceBuilder().withNewMetadata().withName(DEFAULT_NAMESPACE).endMetadata().build();

        client.namespaces().createOrReplace(namespace);

        final ServiceAccount deploymentServiceAccount = new ServiceAccountBuilder().withNewMetadata().withName("deployment").endMetadata().build();
        client.serviceAccounts().inNamespace(DEFAULT_NAMESPACE).createOrReplace(deploymentServiceAccount);
        return client;
    }
}
