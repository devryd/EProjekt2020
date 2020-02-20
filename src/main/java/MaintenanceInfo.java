public class MaintenanceInfo {

    //  must have

    private String version;

    // optional

    private String description;

    public MaintenanceInfo(String version){
        this.version = version;
    }

    // GETTER AND SETTER

    // must have

    public String getVersion(){
        return version;
    }

    // optional

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
