package de.thbin.epro.model;

public class ServiceCatalog {

    // must have

    private ServiceOffering[] services;

    // CONSTRUCTOR

    public ServiceCatalog(){

    }

    // GETTER AND SETTER


    public ServiceOffering[] getServices() {
        return services;
    }

    public void setServices(ServiceOffering[] services) {
        this.services = services;
    }
}
