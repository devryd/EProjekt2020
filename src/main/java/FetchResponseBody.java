public class FetchResponseBody {
    String service_id;
    String plan_id;
    String dashboard_url;
    Object parameters;

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

    public String getDashboard_url() {
        return dashboard_url;
    }

    public void setDashboard_url(String dashboard_url) {
        this.dashboard_url = dashboard_url;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }
}
