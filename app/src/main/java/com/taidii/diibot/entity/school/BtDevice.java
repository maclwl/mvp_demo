package com.taidii.diibot.entity.school;

public class BtDevice {

    private String device_Name;
    private String service_Name;
    private String characteristic_Name;

    public BtDevice(String device_Name, String service_Name, String characteristic_Name) {
        this.device_Name = device_Name;
        this.service_Name = service_Name;
        this.characteristic_Name = characteristic_Name;
    }

    public String getDevice_Name() {
        return device_Name;
    }

    public void setDevice_Name(String device_Name) {
        this.device_Name = device_Name;
    }

    public String getService_Name() {
        return service_Name;
    }

    public void setService_Name(String service_Name) {
        this.service_Name = service_Name;
    }

    public String getCharacteristic_Name() {
        return characteristic_Name;
    }

    public void setCharacteristic_Name(String characteristic_Name) {
        this.characteristic_Name = characteristic_Name;
    }
}
