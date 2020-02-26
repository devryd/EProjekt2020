package de.thbin.epro.model;

public class ServiceInstanceSchema {

    // optional

    private InputParameterSchema create;
    private InputParameterSchema update;

    // CONSTRUCTOR

    public ServiceInstanceSchema(){
        create = new InputParameterSchema("si_create");
        update = new InputParameterSchema("si_update");

    }

    // GETTER AND SETTER


    public InputParameterSchema getCreate() {
        return create;
    }

    public void setCreate(InputParameterSchema create) {
        this.create = create;
    }

    public InputParameterSchema getUpdate() {
        return update;
    }

    public void setUpdate(InputParameterSchema update) {
        this.update = update;
    }
}
