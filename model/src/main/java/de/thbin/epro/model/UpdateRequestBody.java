package de.thbin.epro.model;

public class UpdateRequestBody {
    Object context;

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

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public PreviousValues getPrevious_values() {
        return previous_values;
    }

    public void setPrevious_values(PreviousValues previous_values) {
        this.previous_values = previous_values;
    }

    String service_id;
    String plan_id;
    Object parameters;
    PreviousValues previous_values;
    //de.thbin.epro.model.MaintenanceInfo maintenance_info;
}
