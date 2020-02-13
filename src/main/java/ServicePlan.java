public class ServicePlan {

    // must have

    final private String id, name, description;

    // optional

    private Object metadata;
    private boolean free;
    private boolean bindable;
    private boolean plan_updateable;
    //private Schemas schemas;
    private int maximum_polling_duration;
    //private MaintenanceInfo maintenance_info;

    // CONSTRUCTOR

    public ServicePlan(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // GETTER AND SETTER

    // must have

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // optional

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isBindable() {
        return bindable;
    }

    public void setBindable(boolean bindable) {
        this.bindable = bindable;
    }

    public boolean isPlan_updateable() {
        return plan_updateable;
    }

    public void setPlan_updateable(boolean plan_updateable) {
        this.plan_updateable = plan_updateable;
    }

    public int getMaximum_polling_duration() {
        return maximum_polling_duration;
    }

    public void setMaximum_polling_duration(int maximum_polling_duration) {
        this.maximum_polling_duration = maximum_polling_duration;
    }
}
