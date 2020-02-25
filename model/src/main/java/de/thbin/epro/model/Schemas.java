package de.thbin.epro.model;

public class Schemas {

    // optional

    ServiceInstanceSchema service_instance;
    ServiceBindingSchema service_binding;

    // CONSTRUCTOR

    public Schemas(){

    }

    // GETTER AND SETTER


    public ServiceInstanceSchema getService_instance() {
        return service_instance;
    }

    public void setService_instance(ServiceInstanceSchema service_instance) {
        this.service_instance = service_instance;
    }

    public ServiceBindingSchema getService_binding() {
        return service_binding;
    }

    public void setService_binding(ServiceBindingSchema service_binding) {
        this.service_binding = service_binding;
    }
}
