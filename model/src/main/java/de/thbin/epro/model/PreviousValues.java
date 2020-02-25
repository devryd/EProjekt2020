package de.thbin.epro.model;

public class PreviousValues {
    String service_id;
    String plan_id;
    String organization_id;
    String space_id;
    MaintenanceInfo maintenance_info;

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

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getSpace_id() {
        return space_id;
    }

    public void setSpace_id(String space_id) {
        this.space_id = space_id;
    }

    public MaintenanceInfo getMaintenance_info() {
        return maintenance_info;
    }

    public void setMaintenance_info(MaintenanceInfo maintenance_info) {
        this.maintenance_info = maintenance_info;
    }
}