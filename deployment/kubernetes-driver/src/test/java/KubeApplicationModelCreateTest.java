
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.KubeApplicationModel;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.*;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.JobDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.PodDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.RcDTO;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.dto.SvcDTO;
import org.junit.Test;

import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.DeploymentMetaData;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.DeploymentType;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.JobPolicy;
import com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.metadata.JobType;
import com.ericsson.component.aia.sdk.pba.model.PBAInstance;
import com.ericsson.component.aia.sdk.pba.tools.PBASchemaTool;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

/**
 * @author elesmac
 */
public class KubeApplicationModelCreateTest {

    private static final String CURRENT_PATH = Paths.get(".").toAbsolutePath().normalize().toString();

    protected static String readFileAsString(final File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
    }

    /* Right now put file woth path instead of Appname */
    public static void main(String[] args) throws IOException, ProcessingException {

    }

    public final File expectedJobYAMLFile = new File("./src/test/resources/job-deployment.yaml");

    @Test
    public void testgetApplicationModelasYaml() {
        try {
            final PBAInstance instance = new PBASchemaTool()
                    .getPBAModelInstance(readFileAsString(new File("./src/test/resources/test_spark_pba.json")));
            final DeploymentMetaData metaData = new DeploymentMetaData(JobType.BATCH, DeploymentType.SINGLE, JobPolicy.RESTART_ON_FAILURE);
            final KubeApplicationModel myApp = new KubeApplicationModel();
            myApp.getApplicationAsYaml(instance,metaData);
        } catch (final IOException ioe) {
            fail("IOException unexpected: " + ioe);
        } catch (ProcessingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testgetApplicationModel() {
        try {
            final PBAInstance instance = new PBASchemaTool()
                    .getPBAModelInstance(readFileAsString(new File("./src/test/resources/test_spark_pba.json")));
            final DeploymentMetaData metaData = new DeploymentMetaData(JobType.BATCH, DeploymentType.SINGLE, JobPolicy.RESTART_ON_FAILURE);
            final KubeApplicationModel myApp = new KubeApplicationModel();
            myApp.getApplication(instance,metaData);
        } catch (final IOException ioe) {
            fail("IOException unexpected: " + ioe);
        } catch (ProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPBAConversionToKubernetesJob() {
        try {
            final PBAInstance instance = new PBASchemaTool()
                    .getPBAModelInstance(readFileAsString(new File("./src/test/resources/test_spark_pba.json")));
            final DeploymentMetaData metaData = new DeploymentMetaData(JobType.BATCH, DeploymentType.SCHEDULE, JobPolicy.RESTART_ON_FAILURE);
            final KubernetesManifestBuilder kubernetesBuidler = new KubernetesManifestBuilder(metaData);
            final JobDTO job = kubernetesBuidler.buildJob(instance);
            job.getJob().setApiVersion("batch/v1");
            final String jobAsYaml = job.getJobSpecAsYaml();
            final String expectedYaml = readFileAsString(expectedJobYAMLFile);
            System.out.println(job.getJobSpecAsYaml());
            assertEquals("batch/v1", job.getJob().getApiVersion());
            assertEquals("Job", job.getJob().getKind());
        } catch (final IOException ioe) {
            fail("IOException unexpected: " + ioe);
        }
    }

    @Test
    public void testPBAConversionToKubernetesPod() {
        try {
            final PBAInstance instance = new PBASchemaTool()
                    .getPBAModelInstance(readFileAsString(new File("./src/test/resources/test_spark_pba.json")));
            final DeploymentMetaData metaData = new DeploymentMetaData(JobType.BATCH, DeploymentType.SINGLE, JobPolicy.RESTART_ON_FAILURE);
            final KubernetesManifestBuilder kubernetesBuilder = new KubernetesManifestBuilder(metaData);
            final PodDTO pod = kubernetesBuilder.buildPod(instance);
            final String podAsYaml = pod.getPodSpecAsYaml();
            System.out.println(pod.getPodSpecAsYaml());
            assertEquals("v1", pod.getPod().getApiVersion());
            assertEquals("Pod", pod.getPod().getKind());
        } catch (final IOException ioe) {
            fail("IOException unexpected: " + ioe);
        }
    }

    @Test
    public void testPBAConversionToKubernetesService() {
        try {
            final PBAInstance instance = new PBASchemaTool()
                    .getPBAModelInstance(readFileAsString(new File("./src/test/resources/test_spark_pba.json")));
            final DeploymentMetaData metaData = new DeploymentMetaData(JobType.BATCH, DeploymentType.SINGLE, JobPolicy.RESTART_ON_FAILURE);
            final KubernetesManifestBuilder kubernetesBuilder = new KubernetesManifestBuilder(metaData);
            final SvcDTO service = kubernetesBuilder.buildService(instance);
            final String serviceSvcSpecAsYaml = service.getSvcSpecAsYaml();
            System.out.println(service.getSvcSpecAsYaml());
            assertEquals("v1", service.getSvc().getApiVersion());
            assertEquals("Service", service.getSvc().getKind());
        } catch (final IOException ioe) {
            fail("IOException unexpected: " + ioe);
        }
    }

    @Test
    public void testPBAConversionToReplicationController() {
        try {
            final PBAInstance instance = new PBASchemaTool()
                    .getPBAModelInstance(readFileAsString(new File("./src/test/resources/test_spark_pba.json")));
            final DeploymentMetaData metaData = new DeploymentMetaData(JobType.BATCH, DeploymentType.SINGLE, JobPolicy.RESTART_ON_FAILURE);
            final KubernetesManifestBuilder kubernetesBuilder = new KubernetesManifestBuilder(metaData);

            final RcDTO rc = kubernetesBuilder.buildRepController(instance);
            final String getRCSpecAsYaml = rc.getRCSpecAsYaml();
            System.out.println(rc.getRCSpecAsYaml());

        } catch (final IOException ioe) {
            fail("IOException unexpected: " + ioe);
        }
    }
}
