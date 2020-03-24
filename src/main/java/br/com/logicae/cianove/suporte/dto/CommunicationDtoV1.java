package br.com.logicae.cianove.suporte.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CommunicationDtoV1 {

    private String device;
    private Long port;
    private LocalDateTime collectedIni;
    private LocalDateTime collectedFim;
    private List<SensorsDtoV1> sensorsLesense;
    private List<SensorsDtoV1> sensorsGeo;
    private List<CraneDataDtoV1> cranesGeo;
    private List<SensorsDtoV1> notInGeo;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public LocalDateTime getCollectedIni() {
        return collectedIni;
    }

    public void setCollectedIni(LocalDateTime collectedIni) {
        this.collectedIni = collectedIni;
    }

    public LocalDateTime getCollectedFim() {
        return collectedFim;
    }

    public void setCollectedFim(LocalDateTime collectedFim) {
        this.collectedFim = collectedFim;
    }

    public List<SensorsDtoV1> getSensorsLesense() {
        return sensorsLesense;
    }

    public void setSensorsLesense(List<SensorsDtoV1> sensorsLesense) {
        this.sensorsLesense = sensorsLesense;
    }

    public List<SensorsDtoV1> getSensorsGeo() {
        return sensorsGeo;
    }

    public void setSensorsGeo(List<SensorsDtoV1> sensorsGeo) {
        this.sensorsGeo = sensorsGeo;
    }

    public List<CraneDataDtoV1> getCranesGeo() {
        return cranesGeo;
    }

    public void setCranesGeo(List<CraneDataDtoV1> cranesGeo) {
        this.cranesGeo = cranesGeo;
    }

    public List<SensorsDtoV1> getNotInGeo() {
        return notInGeo;
    }

    public void setNotInGeo(List<SensorsDtoV1> notInGeo) {
        this.notInGeo = notInGeo;
    }
}
