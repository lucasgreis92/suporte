package br.com.logicae.cianove.suporte.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

public class CraneDataDtoV1 {

    private UUID id;

    private String username;

    private String deviceSerial;

    private LocalDateTime created;

    private LocalDateTime on;

    private LocalDateTime off;

    private Integer port;

    private Double value;

    private UUID userId;

    private AcionamentoDtoV1 acionamento;

    public CraneDataDtoV1() {
    }

    public CraneDataDtoV1(LocalDateTime on, LocalDateTime off) {
        this.on = on;
        this.off = off;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public AcionamentoDtoV1 getAcionamento() {
        return acionamento;
    }

    public void setAcionamento(AcionamentoDtoV1 acionamento) {
        this.acionamento = acionamento;
    }
}
