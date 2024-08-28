/**
 *
 */
package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter;

import java.io.IOException;

import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.JobDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.PodDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.RcDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.SvcDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.DeploymentMetaData;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;



/**
 * @author elesmac
 */
public class KubeApplicationModel {
    private static final Logger Log = LoggerFactory.getLogger(KubeApplicationModel.class);

    public String getApplicationAsYaml(final PBAInstance pba, final DeploymentMetaData metaData) throws IOException, ProcessingException {
        final KubernetesManifestBuilder kubernetesManifestBuilder = new KubernetesManifestBuilder(metaData);
        final String yamlspec;
        switch (metaData.getDeployType()){
            case DEPLOYMENT:
                final RcDTO repControllerc = kubernetesManifestBuilder.buildRepController(pba);
                final SvcDTO svc = kubernetesManifestBuilder.buildService(pba);
                yamlspec = repControllerc.getRCSpecAsYaml() + svc.getSvcSpecAsYaml();
                System.out.println(yamlspec);
                Log.info("Creating yaml Deployment Specification for " + pba.getPba().getApplicationInfo().getName());
                Log.debug("Created yaml Deployment Specification", yamlspec);
                return yamlspec;
            case SCHEDULE:
                final JobDTO job = kubernetesManifestBuilder.buildJob(pba);
                yamlspec = job.getJobSpecAsYaml();
                Log.info("Creating yaml Job Specification for " + pba.getPba().getApplicationInfo().getName());
                Log.debug("Created yaml Job Specification", yamlspec);
            return yamlspec;
            case SINGLE:
                final PodDTO pod = kubernetesManifestBuilder.buildPod(pba);
                yamlspec =  pod.getPodSpecAsYaml();
                System.out.print(yamlspec);
                Log.info("Creating Pod Specification for " + pba.getPba().getApplicationInfo().getName());
                Log.debug("Created Pod Specification", yamlspec);
            return yamlspec;
            default: throw new UnsupportedOperationException("Not supported environment parameter");
        }
    }

    public DeploymentSpec getApplication(final PBAInstance pba, final DeploymentMetaData metaData) throws IOException, ProcessingException {
        final KubernetesManifestBuilder kubernetesManifestBuilder = new KubernetesManifestBuilder(metaData);
        final DeploymentSpec depSpec = new DeploymentSpec();
        switch (metaData.getDeployType()){
            case DEPLOYMENT:
                final RcDTO repControllerc = kubernetesManifestBuilder.buildRepController(pba);
                final SvcDTO svc = kubernetesManifestBuilder.buildService(pba);
                depSpec.setReplicationController(repControllerc);
                depSpec.setService(svc);
                Log.info("Creating Deployment object for " + pba.getPba().getApplicationInfo().getName());
                Log.debug("Created Deployment object " + depSpec);
            return depSpec;
            case SCHEDULE:
                final JobDTO job = kubernetesManifestBuilder.buildJob(pba);
                depSpec.setJob(job);
                Log.info("Creating Job object for " + pba.getPba().getApplicationInfo().getName());
                Log.debug("Created Job object " + depSpec);
            return depSpec;
            case SINGLE:
                final PodDTO pod = kubernetesManifestBuilder.buildPod(pba);
                depSpec.setPod(pod);
                Log.info("Creating pod object for " + pba.getPba().getApplicationInfo().getName());
                Log.debug("Created pod object as " + depSpec);
            return depSpec;
            default: throw new UnsupportedOperationException("Not supported environment parameter");
        }
    }
}
