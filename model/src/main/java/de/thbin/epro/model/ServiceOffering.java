package de.thbin.epro;

import java.util.ArrayList;

public class ServiceOffering {

    // must have

    final private String name, id, description;
    final private boolean bindable;
    final private ArrayList<ServicePlan> plans;

    // optional

    private ArrayList<String> tags;
    private ArrayList<String> requires;
    private boolean instances_retrievable;
    private boolean bindings_retrievable;
    private boolean allow_context_updates;
    private Object metadata;
    // private DashboardClient dashboard_client;
    private boolean plan_updateable;

    // CONSTRUCTOR

    public ServiceOffering(String name, String id, String description, boolean bindable, ArrayList<ServicePlan> plans){
        this.name = name;
        this.id = id;
        this.description = description;
        this.bindable = bindable;
        this.plans = plans;
    }

    // GETTER AND SETTER

    // must have

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBindable() {
        return bindable;
    }

    public ArrayList<ServicePlan> getPlans() {
        return plans;
    }

    // optional

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getRequires() {
        return requires;
    }

    public void setRequires(ArrayList<String> requires) {
        this.requires = requires;
    }

    public boolean isInstances_retrievable() {
        return instances_retrievable;
    }

    public void setInstances_retrievable(boolean instances_retrievable) {
        this.instances_retrievable = instances_retrievable;
    }

    public boolean isBindings_retrievable() {
        return bindings_retrievable;
    }

    public void setBindings_retrievable(boolean bindings_retrievable) {
        this.bindings_retrievable = bindings_retrievable;
    }

    public boolean isAllow_context_updates() {
        return allow_context_updates;
    }

    public void setAllow_context_updates(boolean allow_context_updates) {
        this.allow_context_updates = allow_context_updates;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public boolean isPlan_updateable() {
        return plan_updateable;
    }

    public void setPlan_updateable(boolean plan_updateable) {
        this.plan_updateable = plan_updateable;
    }
}
