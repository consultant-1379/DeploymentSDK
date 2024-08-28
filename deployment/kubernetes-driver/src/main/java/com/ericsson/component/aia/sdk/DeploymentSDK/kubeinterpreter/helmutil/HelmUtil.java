package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.helmutil;

import java.io.IOException;

public class HelmUtil {

    public void helmconfig()
    {
        /*TODO - this has to manually check if kubectl if in place and if helm binary is in place. Based on result if kubectl is not in place then break.
          If helm is not in place but kubectl is then download helm and init it*/
        final CmdExecutor cmdExecutor = new CmdExecutor();
        final String helmcmd = "kubectl";
        try {
            cmdExecutor.cmdExec(helmcmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void aiaRepoConfig(final String aiaRepoUrl){
        final CmdExecutor cmdExecutor = new CmdExecutor();
        final String helmcmd = "helm repo add aia "+aiaRepoUrl;
        try {
            cmdExecutor.cmdExec(helmcmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void search(final String chartName)
    {
        final CmdExecutor cmdExecutor = new CmdExecutor();
        final String helmcmd = "helm search "+chartName;
        try {
            cmdExecutor.cmdExec(helmcmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void install(final String chartName)
    {
        final CmdExecutor cmdExecutor = new CmdExecutor();
        final String helmcmd = "helm install "+chartName;
        try {
            cmdExecutor.cmdExec(helmcmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void list_releases()
    {
        final CmdExecutor cmdExecutor = new CmdExecutor();
        final String helmcmd = "helm list";
        try {
            cmdExecutor.cmdExec(helmcmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void delete_release(final String releaseName)
    {
        final CmdExecutor cmdExecutor = new CmdExecutor();
        final String helmcmd = "helm delete "+releaseName;
        try {
            cmdExecutor.cmdExec(helmcmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
