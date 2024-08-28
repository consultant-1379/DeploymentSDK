package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.discoverer;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;

class KubeDiscoveryTest {
    @Test
    public void getNodesStatus() throws Exception {

        final Config config = new ConfigBuilder().withMasterUrl("192.168.99.100:8443").withNamespace("Default").withUsername("default").build();

        DefaultKubernetesClient client = new DefaultKubernetesClient(config);

        KubeDiscovery testing = new KubeDiscovery(client);

        testing.getNodes();
        testing.getNodesSpec();
        testing.listPods();
        //        testing.listServices(environmentMetadata);
        //        testing.listReplicationControllers(environmentMetadata);
        //        testing.listServiceAccounts(environmentMetadata);
        //        testing.listEndpoints(environmentMetadata);

    }

}