package br.com.logicae.cianove.suporte.service;

import br.com.logicae.cianove.suporte.dto.CommunicationDtoV1;
import br.com.logicae.cianove.suporte.dto.CraneDataDtoV1;
import br.com.logicae.cianove.suporte.dto.SensorsDtoV1;
import br.com.logicae.cianove.suporte.service.client.geo.GCraneDataClientService;
import br.com.logicae.cianove.suporte.service.client.geo.GSensorsClientService;
import br.com.logicae.cianove.suporte.service.client.lesense.LSensorsClientService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CommunicationService {

    @Autowired
    private LSensorsClientService lSensorsClientService;

    @Autowired
    private GCraneDataClientService gCraneDataClientService;

    @Autowired
    private GSensorsClientService gSensorsClientService;

    private static final  DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    public CommunicationDtoV1 findByFilter(String device,
                                           Long port,
                                           LocalDateTime collectedIni,
                                           LocalDateTime collectedFim) {

        CommunicationDtoV1 communicationDtoV1 = new CommunicationDtoV1();
        communicationDtoV1.setSensorsGeo(gSensorsClientService.findByFilter(device,port,collectedIni,collectedFim));
        communicationDtoV1.setSensorsLesense(lSensorsClientService.findByFilter(device,port,collectedIni,collectedFim));
        communicationDtoV1.setCranesGeo(gCraneDataClientService.findByFilter(device,port,collectedIni,collectedFim));
        communicationDtoV1.setDevice(device);
        communicationDtoV1.setPort(port);
        communicationDtoV1.setCollectedIni(collectedIni);
        communicationDtoV1.setCollectedFim(collectedFim);
        communicationDtoV1.setNotInGeo(new ArrayList<>());

        HashMap<String, SensorsDtoV1> auxMap = new HashMap<>();
        communicationDtoV1.getSensorsGeo().forEach( gs -> {
            auxMap.put(gs.getId().toString(),gs);
        });
        communicationDtoV1.getSensorsLesense().forEach( ls -> {
            if (!auxMap.containsKey(ls.getId().toString())) {
                communicationDtoV1.getNotInGeo().add(ls);
            }
        });

        communicationDtoV1.getCranesGeo().forEach( gc -> {
            if (auxMap.containsKey(gc.getId().toString())) {
                auxMap.get(gc.getId().toString()).setCrane(gc);
            }
        });

        return communicationDtoV1;

    }


    public byte[] gerarRelatoriodeComunicacao(String device,
                                              Long port,
                                              LocalDateTime collectedIni,
                                              LocalDateTime collectedFim) throws IOException {

        CommunicationDtoV1 dto = findByFilter(device,port,collectedIni,collectedFim);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Dados sensores Lesense");
        generateLesenseSensors(sheet, dto);
        sheet = workbook.createSheet("Dados sensores GEO");
        generateGEOSensors(sheet, dto);
        sheet = workbook.createSheet("Dados ciclos");
        generateCiclos(sheet, dto);
        sheet = workbook.createSheet("Não encontrado no GEO");
        generateNaoEncontrouGEO(sheet, dto);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        byte[] bytes = os.toByteArray();
        return bytes;

    }

    public void generateCiclos(Sheet sheet, CommunicationDtoV1 dto) {
        Row newRow = sheet.createRow(0);
        newRow.createCell(0).setCellValue("Serial");
        newRow.createCell(1).setCellValue("Porta");
        newRow.createCell(2).setCellValue("Data ON");
        newRow.createCell(3).setCellValue("Data OFF");
        for (int i = 0; i < dto.getCranesGeo().size(); i++) {
            CraneDataDtoV1 crane = dto.getCranesGeo().get(i);
            int linha = i+1;
            newRow = sheet.createRow(linha);
            newRow.createCell(0).setCellValue(crane.getDeviceSerial());
            newRow.createCell(1).setCellValue(crane.getPort());
            newRow.createCell(2).setCellValue(crane.getOn().format(FORMATTER));
            newRow.createCell(3).setCellValue(crane.getOff().format(FORMATTER));
        }
    }

    public void generateGEOSensors(Sheet sheet, CommunicationDtoV1 dto) {
        Row newRow = sheet.createRow(0);
        newRow.createCell(0).setCellValue("Serial");
        newRow.createCell(1).setCellValue("Porta");
        newRow.createCell(2).setCellValue("Data coleta");
        newRow.createCell(3).setCellValue("Valor");
        newRow.createCell(4).setCellValue("Tipo");
        newRow.createCell(5).setCellValue("Modelo");
        newRow.createCell(6).setCellValue("Data desligamento");
        for (int i = 0; i < dto.getSensorsGeo().size(); i++) {
            SensorsDtoV1 sensors = dto.getSensorsGeo().get(i);
            int linha = i+1;
            newRow = sheet.createRow(linha);
            newRow.createCell(0).setCellValue(sensors.getDeviceSerial());
            newRow.createCell(1).setCellValue(sensors.getPort());
            newRow.createCell(2).setCellValue(sensors.getCollected().format(FORMATTER));
            newRow.createCell(3).setCellValue(sensors.getValue().intValue() == 1 ? "Ligado" : "Desligado");
            newRow.createCell(4).setCellValue(sensors.getType());
            newRow.createCell(5).setCellValue(sensors.getModel());
            if (sensors.getCrane() != null) {
                CraneDataDtoV1 crane = sensors.getCrane();
                if (crane.getOff() != null) {
                    newRow.createCell(6).setCellValue(crane.getOff().format(FORMATTER));
                }
            }
        }
    }

    public void generateLesenseSensors(Sheet sheet, CommunicationDtoV1 dto) {
        Row newRow = sheet.createRow(0);
        newRow.createCell(0).setCellValue("Serial");
        newRow.createCell(1).setCellValue("Porta");
        newRow.createCell(2).setCellValue("Data Coleta");
        newRow.createCell(3).setCellValue("Valor");
        newRow.createCell(4).setCellValue("Tipo");
        newRow.createCell(5).setCellValue("Modelo");
        for (int i = 0; i < dto.getSensorsLesense().size(); i++) {
            SensorsDtoV1 sensors = dto.getSensorsLesense().get(i);
            int linha = i+1;
            newRow = sheet.createRow(linha);
            newRow.createCell(0).setCellValue(sensors.getDeviceSerial());
            newRow.createCell(1).setCellValue(sensors.getPort());
            newRow.createCell(2).setCellValue(sensors.getCollected().format(FORMATTER));
            newRow.createCell(3).setCellValue(sensors.getValue().intValue() == 1 ? "Ligado" : "Desligado");
            newRow.createCell(4).setCellValue(sensors.getType());
            newRow.createCell(5).setCellValue(sensors.getModel());
        }
    }

    public void generateNaoEncontrouGEO(Sheet sheet, CommunicationDtoV1 dto) {
        Row newRow = sheet.createRow(0);
        newRow.createCell(0).setCellValue("Serial");
        newRow.createCell(1).setCellValue("Porta");
        newRow.createCell(2).setCellValue("Data Coleta");
        newRow.createCell(3).setCellValue("Valor");
        newRow.createCell(4).setCellValue("Tipo");
        newRow.createCell(5).setCellValue("Modelo");
        for (int i = 0; i < dto.getNotInGeo().size(); i++) {
            SensorsDtoV1 sensors = dto.getNotInGeo().get(i);
            int linha = i+1;
            newRow = sheet.createRow(linha);
            newRow.createCell(0).setCellValue(sensors.getDeviceSerial());
            newRow.createCell(1).setCellValue(sensors.getPort());
            newRow.createCell(2).setCellValue(sensors.getCollected().format(FORMATTER));
            newRow.createCell(3).setCellValue(sensors.getValue().intValue() == 1 ? "Ligado" : "Desligado");
            newRow.createCell(4).setCellValue(sensors.getType());
            newRow.createCell(5).setCellValue(sensors.getModel());
        }
    }
}
