package de.thbin.epro.model;

/**
 * ServiceInstanceSchema contains information for creating and updating an service instance
 * @author Michael Kunz
 * */
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
