package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.helmutil;

import org.junit.jupiter.api.Test;


class helmerTest {
    @Test
    void helmconfig() {
        HelmUtil helmUtil = new HelmUtil();
        helmUtil.helmconfig();
    }

    @Test
    void aiarepoconfig() {
        HelmUtil helmUtil = new HelmUtil();
        helmUtil.aiaRepoConfig("http://10.44.149.114");
    }

    @Test
    void chartsearch() {
        HelmUtil helmUtil = new HelmUtil();
        helmUtil.search("kafka");
    }

    @Test
    void chartinstall() {
        HelmUtil helmUtil = new HelmUtil();
        helmUtil.install("aia/kafka");
    }
    @Test
    void listreleases() {
        HelmUtil helmUtil = new HelmUtil();
        helmUtil.list_releases();
    }

    @Test
    void deleterelease() {
        HelmUtil helmUtil = new HelmUtil();
        helmUtil.delete_release("measly-saola");
    }
}