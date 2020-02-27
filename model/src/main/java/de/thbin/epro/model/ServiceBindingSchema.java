package de.thbin.epro.model;

/**
 * ServiceBindingSchema contains information for binding an instance.
 * @author Michael Kunz
 * */
public class ServiceBindingSchema {

    // optional

    private InputParameterSchema create;

    // CONSTRUCTOR

    public ServiceBindingSchema(){
        create = new InputParameterSchema("sb");
    }

    // GETTER AND SETTER


    public InputParameterSchema getCreate() {
        return create;
    }

    public void setCreate(InputParameterSchema create) {
        this.create = create;
    }
}
