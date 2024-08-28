package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.JobDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.PodDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.RcDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.SvcDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.DeploymentMetaData;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.JobPolicy;
import com.ericsson.component.aia.sdk.pba.model.Arg;
import com.ericsson.component.aia.sdk.pba.model.Docker;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.JobBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerBuilder;
import io.fabric8.kubernetes.api.model.ReplicationControllerSpec;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServiceSpec;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeBuilder;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;

public class KubernetesManifestBuilder {
    private static final String RC_PREFIX = "rc-";
    private static final String POD_PREFIX = "pod-";
    private static final String SVC_PREFIX = "svc-";
    private static final String JOB_PREFIX = "job-";
    private static final String PORT_PREFIX = "job-";
    private static boolean rcIsCreated = false;
    private static boolean podIsCreated = false;
    private static boolean jobIsCreated = false;
    final com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.DeploymentMetaData metaData;

    /**
     * Constructs the builder object with the supplied {@link DeploymentMetaData}
     *
     * @param mData
     *            - meta data to be used by this builder object.
     */
    public KubernetesManifestBuilder(final DeploymentMetaData mData) {
        metaData = mData;
    }

    private List<EnvVar> getContainerEnvironmentArgs(final List<Arg> envArgs) {
        final List<EnvVar> listEnvVars = new ArrayList<>();
        for (final Arg arg : envArgs) {
            final EnvVarBuilder envBuilder = new EnvVarBuilder();
            envBuilder.withName(arg.getKey());
            envBuilder.withValue(arg.getValue().toString());
            listEnvVars.add(envBuilder.build());
        }
        return listEnvVars;
    }

    private List<VolumeMount> getContainerMountPaths(final Set<String> mountPathSet, final Set<String> mountPathName, final List<Volume> volumes) {
        final List<VolumeMount> mountPathList = new ArrayList<>();

        //set mount volumes
        for (final String mountPath : mountPathSet) {
            for (final String mountName : mountPathName) {
                final VolumeMount mountP = new VolumeMountBuilder().withMountPath(mountPath).withName(mountName).withReadOnly(true).build();
                mountP.setName(String.valueOf(mountPathName));
                mountPathList.add(mountP);
                volumes.add(new VolumeBuilder().withName(mountP.getName()).withNewHostPath(mountPath).build());
            }
        }
        return mountPathList;
    }

    /**
     * Generates a kubernetes Pod Spec.
     *
     * @param pba
     *            - pba instance from which the kubernetes pod specification has to be built.
     * @return - {@link PodTemplateSpec} - the spec.
     */
    PodSpec getPodSpec(final PBAInstance pba) {
        final io.fabric8.kubernetes.api.model.Container container = new io.fabric8.kubernetes.api.model.Container();
        final Docker docker = pba.getPba().getBuildInfo().getContainer().getDocker();
        container.setImage(docker.getRepoBaseUrl() + "/" + docker.getRepoPath() + "/" + docker.getImagePath());
        if (docker.getForcePullImage()) {
            container.setImagePullPolicy("Always");
        }
        container.setName(docker.getName());
        //Adding container ports for exposing
        final Set<String> portList = docker.getPortList();
        final List<ContainerPort> containerPortList = new ArrayList<>();
        for (final String port : portList) {
            final ContainerPort containerPort = new ContainerPort();
            containerPort.setContainerPort(Integer.valueOf(port));
            containerPort.setName(PORT_PREFIX + port);
            containerPortList.add(containerPort);
        }
        //Adding exposed ports from portlist
        container.setPorts(containerPortList);
        //Adding containers to container builder
        final List<io.fabric8.kubernetes.api.model.Container> containers = new ArrayList<>();
        container.setName(pba.getPba().getBuildInfo().getContainer().getDocker().getName());
        containers.add(container);
        //get env args
        final List<EnvVar> listEnvVars = getContainerEnvironmentArgs(pba.getPba().getDeploymentInfo().getEnvArgs());
        // volumes to be mounted from hosts
        final List<Volume> volumes = new ArrayList<>();
        //set container mount paths
        container.setVolumeMounts(getContainerMountPaths(docker.getMountPaths(), docker.getMountName(), volumes));
        container.setEnv(listEnvVars);

        final PodSpec podSpec = new PodSpec();
        //Naming the containers
        podSpec.setContainers(containers);

        //set volumes for the pod that will be shared by the container
        podSpec.setVolumes(volumes);
        if (metaData.getJobPolicy() == JobPolicy.RESTART_ON_FAILURE) {
            podSpec.setRestartPolicy("OnFailure");
        } else if (metaData.getJobPolicy() == JobPolicy.NEVER_RESTART) {
            podSpec.setRestartPolicy("Never");
        } else if (metaData.getJobPolicy() == JobPolicy.ALWAYS) {
            podSpec.setRestartPolicy("Always");
        }
        podIsCreated = true;
        return podSpec;
    }

    /**
     * Generates a kubernetes Replication Controller Spec.
     *
     * @param pba
     *            - pba instance from which the kubernetes rc specification has to be built.
     * @return - {@link ReplicationController} - the spec.
     */
    ReplicationController getRCSpec(final PBAInstance pba) {

        final Map<String, String> labels = new HashMap<>();
        final String label1 = pba.getPba().getApplicationInfo().getName() + pba.getPba().getApplicationInfo().getId();
        final String label2 = pba.getPba().getBuildInfo().getContainer().getDocker().getName();
        labels.put(label1, label2);

        final PodTemplateSpec podTemplateSpec = new PodTemplateSpec();
        podTemplateSpec.setSpec(getPodSpec(pba));
        final ObjectMeta metadata = new ObjectMetaBuilder().withLabels(labels).build();

        podTemplateSpec.setMetadata(metadata);
        final ReplicationControllerSpec Spec = new ReplicationControllerSpec();
        Spec.setTemplate(podTemplateSpec);
        Spec.setReplicas(Integer.valueOf(pba.getPba().getDeploymentInfo().getNoOfInstances()));

        final Map<String, String> selectors = new HashMap<>();
        final String selector1 = pba.getPba().getApplicationInfo().getName() + pba.getPba().getApplicationInfo().getId();
        final String selector2 = pba.getPba().getBuildInfo().getContainer().getDocker().getName();
        selectors.put(selector1, selector2);

        Spec.setSelector(selectors);

        final ReplicationControllerBuilder RCSpecBuilder = new ReplicationControllerBuilder();

        RCSpecBuilder.withNewMetadata().addToLabels(label1, label2).withName(label1).endMetadata().withSpec(Spec).build();
        rcIsCreated = true;
        return RCSpecBuilder.build();
    }

    public String selectorBuilder() {
        String SELECTOR_PREFIX;
        if (rcIsCreated) {
            SELECTOR_PREFIX = RC_PREFIX;
        } else if (jobIsCreated) {
            SELECTOR_PREFIX = JOB_PREFIX;
        } else if (podIsCreated) {
            SELECTOR_PREFIX = POD_PREFIX;
        } else {
            SELECTOR_PREFIX = SVC_PREFIX;
        }
        return SELECTOR_PREFIX;
    }

    /**
     * Generates a kubernetes Service Spec.
     *
     * @param pba
     *            - pba instance from which the kubernetes pod specification has to be built.
     * @return - {@link ServiceSpec} - the spec.
     */
    ServiceSpec getSvcSpec(final PBAInstance pba) {
        final io.fabric8.kubernetes.api.model.ServiceSpec serviceSpec = new io.fabric8.kubernetes.api.model.ServiceSpec();
        final List<com.ericsson.component.aia.sdk.pba.model.ServicePort> servicePortList = pba.getPba().getDeploymentInfo().getServicePorts();
        final List<io.fabric8.kubernetes.api.model.ServicePort> svclist = new ArrayList<>();
        for (final com.ericsson.component.aia.sdk.pba.model.ServicePort serviceDetails : servicePortList) {
            final io.fabric8.kubernetes.api.model.ServicePort svcPort = new io.fabric8.kubernetes.api.model.ServicePort();
            svcPort.setPort(serviceDetails.getSrcPort());
            final IntOrString containerPortObj = new IntOrString();
            containerPortObj.setIntVal(serviceDetails.getTargetPort());
            svcPort.setTargetPort(containerPortObj);
            svcPort.setProtocol(serviceDetails.getProtocol());
            svcPort.setName(SVC_PREFIX + (pba.getPba().getDeploymentInfo().getSvcname()) + "" + (serviceDetails.getTargetPort()));

            svclist.add(svcPort);
        }

        final Map<String, String> Selectors = new HashMap<>();
        final String Selector1 = selectorBuilder() + pba.getPba().getApplicationInfo().getName() + pba.getPba().getApplicationInfo().getId();
        final String Selector2 = selectorBuilder() + pba.getPba().getBuildInfo().getContainer().getDocker().getName();
        Selectors.put(Selector1, Selector2);
        serviceSpec.setSelector(Selectors);
        serviceSpec.setPorts(svclist);
        return serviceSpec;
    }

    /**
     * Generates a kubernetes Pod Data Transfer Object.
     *
     * @param pba
     *            - pba instance from which the kubernetes pod specification has to be built.
     * @return - {@link PodDTO} - object.
     */
    public PodDTO buildPod(final PBAInstance pba) throws JsonProcessingException {
        final PodBuilder podBuilder = new PodBuilder();
        final String Label1 = POD_PREFIX + pba.getPba().getApplicationInfo().getName() + pba.getPba().getApplicationInfo().getId();
        final String Label2 = POD_PREFIX + pba.getPba().getBuildInfo().getContainer().getDocker().getName();
        podBuilder.withNewMetadata().addToLabels(Label1, Label2).endMetadata().withSpec(getPodSpec(pba));
        return new PodDTO(podBuilder.build());
    }

    /**
     * Generates a kubernetes Service Data Transfer Object.
     *
     * @param pba
     *            - pba instance from which the kubernetes service specification has to be built.
     * @return - {@link SvcDTO} - object.
     */
    public SvcDTO buildService(final PBAInstance pba) throws JsonProcessingException {

        final ServiceBuilder serviceBuilder = new ServiceBuilder();
        serviceBuilder.withNewMetadata().withName(pba.getPba().getApplicationInfo().getName()).endMetadata().withSpec(getSvcSpec(pba)).build();
        return new SvcDTO(serviceBuilder.build());

    }

    /**
     * Generates a kubernetes Repplication Controller Data Transfer Object.
     *
     * @param pba
     *            - pba instance from which the kubernetes pod specification has to be built.
     * @return - {@link RcDTO} - object.
     */
    public RcDTO buildRepController(final PBAInstance pba) throws JsonProcessingException {

        ReplicationController rc1 = new ReplicationController();
        rc1 = getRCSpec(pba);
        return new RcDTO(rc1);

    }

    /**
     * Generates a kubernetes Pod Data Transfer Object.
     *
     * @param pba
     *            - pba instance from which the kubernetes Job specification has to be built.
     * @return - {@link JobDTO} - object.
     */
    public JobDTO buildJob(final PBAInstance pba) {
        final JobBuilder jobBuilder = new JobBuilder();
        final PodTemplateSpec podTemplateSpec = new PodTemplateSpec();
        podTemplateSpec.setSpec(getPodSpec(pba));

        jobBuilder.withNewMetadata().withName(JOB_PREFIX + StringUtils.lowerCase(pba.getPba().getApplicationInfo().getName())).endMetadata()
                .withNewSpec().withTemplate(podTemplateSpec).endSpec();
        return new JobDTO(jobBuilder.build());
    }
}