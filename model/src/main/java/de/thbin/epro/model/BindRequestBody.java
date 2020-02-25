package de.thbin.epro.model;

public class BindRequestBody {
    Object context;
    String service_id;
    String plan_id;
    String app_guid;
    BindResource bind_resource;
    Object parameters;

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getApp_guid() {
        return app_guid;
    }

    public void setApp_guid(String app_guid) {
        this.app_guid = app_guid;
    }

    public BindResource getBind_resource() {
        return bind_resource;
    }

    public void setBind_resource(BindResource bind_resource) {
        this.bind_resource = bind_resource;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

}
