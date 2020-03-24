package br.com.logicae.cianove.suporte.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

public class SensorsDtoV1 {

    private UUID id;
    private String username;
    private UUID userId;
    private String deviceSerial;
    private LocalDateTime collected;
    private LocalDateTime created;
    private String version;
    private String type;
    private Double value;
    private Long port;
    private String tags;
    private String model;
    private CraneDataDtoV1 crane;

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public LocalDateTime getCollected() {
        return collected;
    }

    public void setCollected(LocalDateTime collected) {
        this.collected = collected;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CraneDataDtoV1 getCrane() {
        return crane;
    }

    public void setCrane(CraneDataDtoV1 crane) {
        this.crane = crane;
    }
}
