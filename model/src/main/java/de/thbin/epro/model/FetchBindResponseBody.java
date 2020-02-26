package de.thbin.epro.model;

public class FetchBindResponseBody {
    BindingMetadata metadata;
    Object credentials;
    String syslog_drain_url;
    String route_service_url;
    VolumeMount[] volume_mounts;

    public BindingMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BindingMetadata metadata) {
        this.metadata = metadata;
    }

    public Object getCredentials() {
        return credentials;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public String getSyslog_drain_url() {
        return syslog_drain_url;
    }

    public void setSyslog_drain_url(String syslog_drain_url) {
        this.syslog_drain_url = syslog_drain_url;
    }

    public String getRoute_service_url() {
        return route_service_url;
    }

    public void setRoute_service_url(String route_service_url) {
        this.route_service_url = route_service_url;
    }

    public VolumeMount[] getVolume_mounts() {
        return volume_mounts;
    }

    public void setVolume_mounts(VolumeMount[] volume_mounts) {
        this.volume_mounts = volume_mounts;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public Endpoint[] getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Endpoint[] endpoints) {
        this.endpoints = endpoints;
    }

    Object parameters;
    Endpoint[] endpoints;
}
