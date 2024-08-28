package com.ericsson.component.aia.sdk.deploymentmanager.environment.plugin;

/**
 *
 * The attributes that identify and allow access to an environment
 *
 * @author emilawl
 *
 */
public class EnvironmentMetadata {

    private String environmentId;
    private String ipAddress;
    private String environmentType;
    private String token;
    private String httpsCert;
    private String userCert;
    private String userName;

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(final String environmentId) {
        this.environmentId = environmentId;
    }

    public String getIP() {
        return ipAddress;
    }

    public void setIP(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(final String environmentType) {
        this.environmentType = environmentType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getHttpsCert() {
        return httpsCert;
    }

    public void setHttpsCert(final String httpsCert) {
        this.httpsCert = httpsCert;
    }

    public String getUserCert() {
        return userCert;
    }

    public void setUserCert(final String userCert) {
        this.userCert = userCert;
    }

    @Override
    public String toString() {
        return "Environmemt metadeta: ID:" + environmentId + ", IP:" + ipAddress + ", Type:" + environmentType + ", Token: " + token + ", UserName: "
                + userName + ", httpsCert:" + httpsCert + ", userCert:" + userCert;
    }

}
