package de.thbin.epro.model;

public class VolumeMount {
    String driver;
    String container_dir;
    String mode;
    String device_type;
    Device device;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getContainer_dir() {
        return container_dir;
    }

    public void setContainer_dir(String container_dir) {
        this.container_dir = container_dir;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
