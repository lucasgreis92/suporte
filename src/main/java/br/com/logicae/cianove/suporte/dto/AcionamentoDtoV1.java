package br.com.logicae.cianove.suporte.dto;

import java.time.LocalDateTime;

public class AcionamentoDtoV1 {

    private String deviceSerial;

    private LocalDateTime on;

    private LocalDateTime off;

    private Integer portOn;

    private Integer portOff;

    public AcionamentoDtoV1() {
    }

    public AcionamentoDtoV1(LocalDateTime on, LocalDateTime off, Integer portOn, Integer portOff) {
        this.on = on;
        this.off = off;
        this.portOn = portOn;
        this.portOff = portOff;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public LocalDateTime getOn() {
        return on;
    }

    public void setOn(LocalDateTime on) {
        this.on = on;
    }

    public LocalDateTime getOff() {
        return off;
    }

    public void setOff(LocalDateTime off) {
        this.off = off;
    }

    public Integer getPortOn() {
        return portOn;
    }

    public void setPortOn(Integer portOn) {
        this.portOn = portOn;
    }

    public Integer getPortOff() {
        return portOff;
    }

    public void setPortOff(Integer portOff) {
        this.portOff = portOff;
    }
}
