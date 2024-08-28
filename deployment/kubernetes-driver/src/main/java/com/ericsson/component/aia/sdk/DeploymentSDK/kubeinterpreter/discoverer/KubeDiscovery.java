package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.discoverer;

import static io.fabric8.utils.Lists.notNullList;
import static java.lang.String.valueOf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Created by devep on 10.07.2017.
 */
public class KubeDiscovery {
    private static final Logger Log = LoggerFactory.getLogger(KubeDiscovery.class);
    public static String outputtxt = null;
    private final KubernetesClient client;

    public KubeDiscovery(final KubernetesClient client) {
        this.client = client;
    }

    public void listPods() {

        /* This is to override problem with null instead of pba and as createclient return boolean instead of client handler */
        Log.info("\n\nLooking up pods");
        final PodList pods = client.pods().list();
        final List<Pod> items = pods.getItems();
        for (final Pod item : items) {
            Log.info("Pod " + KubernetesHelper.getName(item) + " with ip: " + item.getStatus().getPodIP() + " created: "
                    + item.getMetadata().getCreationTimestamp());
            final PodSpec spec = item.getSpec();
            if (spec != null) {
                final List<Container> containers = spec.getContainers();
                if (containers != null) {
                    for (final Container container : containers) {
                        Log.info("Container " + container.getImage() + " " + container.getCommand() + " ports: " + container.getPorts());
                    }
                }
            }
            final Map<String, ContainerStatus> currentContainers = KubernetesHelper.getCurrentContainers(item);
            System.out.println("Has " + currentContainers.size() + " container(s)");
            final Set<Map.Entry<String, ContainerStatus>> entries = currentContainers.entrySet();
            for (final Map.Entry<String, ContainerStatus> entry : entries) {
                final String containerid = entry.getKey();
                final ContainerStatus info = entry.getValue();
                System.out.println("Current container: " + containerid + " info: " + info);
            }
        }
    }

    public String getNodes() {

        /* This is to override problem with null instead of pba and as createclient return boolean instead of client handler */
        final NodeList nodeList = client.nodes().list();
        if (nodeList != null) {
            final List<Node> items = notNullList(nodeList.getItems());
            for (final Node item : items) {
                outputtxt = valueOf(item.getStatus().getNodeInfo());
                convertoutput(outputtxt);
                Log.info("\n\nNode info is: " + "\n\n" + outputtxt);
            }

        }
        return outputtxt;
    }

    public String getNodesSpec() {

        /* This is to override problem with null instead of pba and as createclient return boolean instead of client handler */

        final NodeList nodeList = client.nodes().list();
        if (nodeList != null) {
            final List<Node> items2 = nodeList.getItems();
            for (final Node nodes : items2) {
                outputtxt = valueOf(nodes.getStatus().getCapacity());
                convertoutput(outputtxt);
                Log.info("\n\nNodespec info is: " + "\n\n" + outputtxt);
            }
        }
        return outputtxt;
    }

    public void convertoutput(final String inputtext) {

        final HashMap<String, String> map = new HashMap<String, String>();

        final String[] arr = inputtext.split(",");
        map.put(arr[0], arr[1]);
        for (final String text : arr) {
            System.out.println(text);
        }
    }

    //    public static String listServices() {
    //        final SimpleKubernetesEnvironmentPlugin skep = new SimpleKubernetesEnvironmentPlugin(null, environmentMetadata);
    //        final KubernetesClient client = skep.createsimpleclient();
    //        /* This is to override problem with null instead of pba and as createclient return boolean instead of client handler */
    //
    //        Log.info("\n\nLooking up services");
    //        final ServiceList services = client.services().list();
    //        final List<Service> serviceItems = services.getItems();
    //        for (final Service service : serviceItems) {
    //            outputtxt = ("Service " + KubernetesHelper.getName(service) + " labels: " + service.getMetadata().getLabels() + " selector: "
    //                    + getSelector(service) + " ports: " + getPorts(service));
    //        }
    //        Log.info(outputtxt);
    //        return outputtxt;
    //    }
    //
    //    public static void listReplicationControllers(final EnvironmentMetadata environmentMetadata) {
    //        final SimpleKubernetesEnvironmentPlugin skep = new SimpleKubernetesEnvironmentPlugin(null, environmentMetadata);
    //        final KubernetesClient client = skep.createsimpleclient();
    //        /* This is to override problem with null instead of pba and as createclient return boolean instead of client handler */
    //
    //        Log.info("\n\nLooking up replicationControllers");
    //        final ReplicationControllerList replicationControllers = client.replicationControllers().list();
    //        final List<ReplicationController> items = replicationControllers.getItems();
    //        for (final ReplicationController item : items) {
    //            final ReplicationControllerSpec replicationControllerSpec = item.getSpec();
    //            if (replicationControllerSpec != null) {
    //                outputtxt = ("ReplicationController " + KubernetesHelper.getName(item) + " labels: " + item.getMetadata().getLabels() + " replicas: "
    //                        + replicationControllerSpec.getReplicas() + " replicatorSelector: " + replicationControllerSpec.getSelector()
    //                        + " podTemplate: " + replicationControllerSpec.getTemplate());
    //            } else {
    //                outputtxt = ("ReplicationController " + KubernetesHelper.getName(item) + " labels: " + item.getMetadata().getLabels()
    //                        + " no replicationControllerSpec");
    //            }
    //        }
    //        Log.info(outputtxt);
    //    }
    //
    //    public static void listServiceAccounts(final EnvironmentMetadata environmentMetadata) {
    //        final SimpleKubernetesEnvironmentPlugin skep = new SimpleKubernetesEnvironmentPlugin(null, environmentMetadata);
    //        final KubernetesClient client = skep.createsimpleclient();
    //        /* This is to override problem with null instead of pba and as createclient return boolean instead of client handler */
    //
    //        Log.info("\n\nLooking up service accounts");
    //        final ServiceAccountList serviceAccounts = client.serviceAccounts().list();
    //        final List<ServiceAccount> serviceAccountItems = serviceAccounts.getItems();
    //        for (final ServiceAccount serviceAccount : serviceAccountItems) {
    //            outputtxt = ("Service Account " + KubernetesHelper.getName(serviceAccount) + " labels: " + serviceAccount.getMetadata().getLabels());
    //        }
    //        Log.info(outputtxt);
    //    }
    //
    //    public static void listEndpoints(final EnvironmentMetadata environmentMetadata) {
    //        final SimpleKubernetesEnvironmentPlugin skep = new SimpleKubernetesEnvironmentPlugin(null, environmentMetadata);
    //        final KubernetesClient client = skep.createsimpleclient();
    //        /* This is to override problem with null instead of pba and as createclient return boolean instead of client handler */
    //        Log.info("\n\nLooking up endpoints");
    //        final EndpointsList endpoints = client.endpoints().list();
    //        final List<Endpoints> endpointItems = endpoints.getItems();
    //        for (final Endpoints endpoint : endpointItems) {
    //            outputtxt = ("Endpoint " + KubernetesHelper.getName(endpoint) + " labels: " + endpoint.getMetadata().getLabels());
    //        }
    //        Log.info(outputtxt);
    //    }
}