package de.thbin.epro.model;

public class ServiceBrokerErrors {

    // optional

    private String error;
    private String description;
    private boolean instance_usable;
    private boolean update_repeatable;


    // CONSTRUCTOR
    public ServiceBrokerErrors(){
    }

    // set error code

    public void AsyncRequired(){
        this.error = "AsyncRequired";
        this.description = "This request requires client support for asynchronous service operations.";
    }

    public void ConcurrencyError(){
        this.error = "ConcurrencyError";
        this.description = "The Service Broker does not support concurrent requests that mutate the same resource.";
    }

    public void RequiresApp(){
        this.error = "RequiresApp";
        this.description = "The request body is missing the app_guid field.";
    }

    public void MaintenanceInfoConflict(){
        this.error = "MaintenanceInfoConflict";
        this.description = "The maintenance_info.version field provided in the request does not match the maintenance_info.version field provided in the Service Broker's Catalog.";
    }


    // GETTER AND SETTER


    public boolean isInstance_usable() {
        return instance_usable;
    }

    public void setInstance_usable(boolean instance_usable) {
        this.instance_usable = instance_usable;
    }

    public boolean isUpdate_repeatable() {
        return update_repeatable;
    }

    public void setUpdate_repeatable(boolean update_repeatable) {
        this.update_repeatable = update_repeatable;
    }
}
