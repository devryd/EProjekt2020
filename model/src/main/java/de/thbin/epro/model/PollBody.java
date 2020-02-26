package de.thbin.epro.model;

public class PollBody {
    String state;
    String description;
    boolean instance_usable;
    boolean update_repeatable;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
