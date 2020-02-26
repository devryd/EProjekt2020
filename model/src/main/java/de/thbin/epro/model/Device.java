package de.thbin.epro.model;

public class Device {
    public String getVolume_id() {
        return volume_id;
    }

    public void setVolume_id(String volume_id) {
        this.volume_id = volume_id;
    }

    public Object getMount_config() {
        return mount_config;
    }

    public void setMount_config(Object mount_config) {
        this.mount_config = mount_config;
    }

    String volume_id;
    Object mount_config;
}
