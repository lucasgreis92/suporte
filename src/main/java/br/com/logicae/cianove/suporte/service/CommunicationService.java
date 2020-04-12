package br.com.logicae.cianove.suporte.service;

import br.com.logicae.cianove.suporte.dto.CommunicationDtoV1;
import br.com.logicae.cianove.suporte.dto.CraneDataDtoV1;
import br.com.logicae.cianove.suporte.dto.FatCompletoDtoV1;
import br.com.logicae.cianove.suporte.dto.SensorsDtoV1;
import br.com.logicae.cianove.suporte.service.client.geo.GCraneDataClientService;
import br.com.logicae.cianove.suporte.service.client.geo.GSensorsClientService;
import br.com.logicae.cianove.suporte.service.client.lesense.LSensorsClientService;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunicationService {

    @Autowired
    private LSensorsClientService lSensorsClientService;

    @Autowired
    private GCraneDataClientService gCraneDataClientService;

    @Autowired
    private GSensorsClientService gSensorsClientService;

    static final int MINUTES_PER_HOUR = 60;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    private static final  DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final  DateTimeFormatter FORMATTER_DAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final  DateTimeFormatter FORMATTER_HOUR = DateTimeFormatter.ofPattern("HH:mm:ss");

    public CommunicationDtoV1 findByFilter(String device,
                                           Long port,
                                           LocalDateTime collectedIni,
                                           LocalDateTime collectedFim) {

        CommunicationDtoV1 communicationDtoV1 = new CommunicationDtoV1();
        communicationDtoV1.setSensorsGeo(gSensorsClientService.findByFilter(device,port,collectedIni,collectedFim,true));
        communicationDtoV1.setSensorsLesense(lSensorsClientService.findByFilter(device,port,collectedIni,collectedFim, true));
        communicationDtoV1.setCranesGeo(gCraneDataClientService.findByFilter(device,port,collectedIni,collectedFim,true));
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


    public byte[] gerarRelatorioComunicacao(String device,
                                            Long port,
                                            LocalDateTime collectedIni,
                                            LocalDateTime collectedFim) throws IOException {

        CommunicationDtoV1 dto = findByFilter(device,port,collectedIni,collectedFim);
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

    public byte[] gerarRelatorioFaturamento(String device, LocalDateTime collectedIni, LocalDateTime collectedFim) throws IOException {
        Long tempoIni = System.currentTimeMillis();
        List<CraneDataDtoV1> cranes = gCraneDataClientService.findByFilter(device,null,collectedIni,collectedFim,true);
        System.out.println("tempo sql " + ((System.currentTimeMillis() - tempoIni) / 1000) + " segundos");
        tempoIni = System.currentTimeMillis();
        FatCompletoDtoV1 fatCompletoDtoV1 = new FatCompletoDtoV1(cranes, collectedIni, collectedFim);
        System.out.println("tempo rotina " + ((System.currentTimeMillis() - tempoIni) / 1000) + " segundos");
        Workbook workbook = new SXSSFWorkbook();
        CellStyle cellStyleTitle = createCellStyleTitle(workbook);
        CellStyle cellStyleBody = createCellStyleBody(workbook);
        CellStyle cellStyleBodyPort = createCellStyleBodyPort(workbook);
        CellStyle cellStyleSpace = createCellStyleSpace(workbook);
        Sheet sheet = workbook.createSheet("Parcial");
        tempoIni = System.currentTimeMillis();
        generateParcial(sheet, fatCompletoDtoV1, cellStyleTitle, cellStyleBody, cellStyleSpace, cellStyleBodyPort);
        System.out.println("tempo parcial " + ((System.currentTimeMillis() - tempoIni) / 1000) + " segundos");
        sheet = workbook.createSheet("Completo");
        tempoIni = System.currentTimeMillis();
        generateCompleto(sheet, fatCompletoDtoV1, cellStyleTitle, cellStyleBody, cellStyleSpace);
        System.out.println("tempo completo " + ((System.currentTimeMillis() - tempoIni) / 1000) + " segundos");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        tempoIni = System.currentTimeMillis();
        workbook.write(os);
        System.out.println("tempo write " + ((System.currentTimeMillis() - tempoIni) / 1000) + " segundos");
        byte[] bytes = os.toByteArray();
        return bytes;
    }

    public CellStyle createCellStyleTitle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = createXSSFFont(workbook);
        font.setBold(true);
        cellStyle.setFont(font);
        return  cellStyle;
    }

    public CellStyle createCellStyleBody(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(createXSSFFont(workbook));
        return  cellStyle;
    }

    public CellStyle createCellStyleBodyPort(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.JUSTIFY);
        cellStyle.setFont(createXSSFFont(workbook));
        return  cellStyle;
    }

    public CellStyle createCellStyleSpace(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(createXSSFFont(workbook));
        cellStyle.setFillBackgroundColor(new HSSFColor().getIndex());
        return  cellStyle;
    }

    public XSSFFont createXSSFFont(Workbook workbook) {
        XSSFFont font = (XSSFFont) workbook.createFont();

        return font;
    }

    public void generateParcial(Sheet sheet,
                                FatCompletoDtoV1 dataRel,
                                CellStyle cellStyleTitle,
                                CellStyle cellStyleBody,
                                CellStyle cellStyleSpace,
                                CellStyle cellStyleBodyPort) {
        List<List<CraneDataDtoV1>> listCrane = dataRel.getReportData();
        int row = 0;
        sheet.setDefaultColumnWidth(20);
        Row newRow = sheet.createRow(row++);
        int col = 0;
        createCol(newRow,col++,"Equipamento: ", cellStyleTitle);
        createCol(newRow,col++, listCrane.get(0).get(0).getDeviceSerial(), cellStyleTitle);
        newRow = sheet.createRow(row++);
        col = 0;
        createCol(newRow,col++,"Intervalo: " ,cellStyleTitle);
        createCol(newRow,col++,FORMATTER_DAY.format(dataRel.getCollectedIni()) ,cellStyleTitle);
        createCol(newRow,col++,FORMATTER_DAY.format(dataRel.getCollectedFim()) ,cellStyleTitle);

        newRow = sheet.createRow(row++);
        col = 0;
        createCol(newRow,col++,"Ligado " ,cellStyleTitle);
        createCol(newRow,col++,"", cellStyleTitle);
        createCol(newRow,col++,"", cellStyleTitle);
        createCol(newRow,col++,"Tempo Real " ,cellStyleTitle);
        createCol(newRow,col++,diffDaysSecondsStr(dataRel.getTempoCraneList().get(0) / 1000), cellStyleTitle);
        createCol(newRow,col++,"", cellStyleSpace);
        createCol(newRow,col++,"Tempo Operação " ,cellStyleTitle);
        createCol(newRow,col++,"", cellStyleTitle);
        createCol(newRow,col++,"", cellStyleTitle);
        createCol(newRow,col++,"", cellStyleTitle);
        createCol(newRow,col++,"Tempo Total " ,cellStyleTitle);
        createCol(newRow,col++,diffDaysSecondsStr(dataRel.getTempoCraneList().get(0) / 1000), cellStyleTitle);

        col = 0;
        newRow = sheet.createRow(row++);
        newRow = sheet.createRow(row++);

        createCol(newRow,col++,"Data de Início", cellStyleTitle);
        createCol(newRow,col++,"Hora de Início", cellStyleTitle);
        createCol(newRow,col++,"Data de Fim", cellStyleTitle);
        createCol(newRow,col++,"Hora de Fim", cellStyleTitle);
        createCol(newRow,col++,"Total", cellStyleTitle);
        createCol(newRow,col++,"", cellStyleSpace);
        createCol(newRow,col++,"Data de Início", cellStyleTitle);
        createCol(newRow,col++,"Hora de Início", cellStyleTitle);
        createCol(newRow,col++,"Primeiro acionamento", cellStyleTitle);
        createCol(newRow,col++,"Hora de Fim", cellStyleTitle);
        createCol(newRow,col++,"Último acionamento", cellStyleTitle);
        createCol(newRow,col++,"Total Combinado", cellStyleTitle);


        for (int rowRel = 0; rowRel < dataRel.getCicloAcionamento().size(); rowRel++ ) {
            Row selectedRow = sheet.createRow(rowRel + row);
            int colRel = 0;
            CraneDataDtoV1 crane = dataRel.getCicloAcionamento().get(rowRel);
            createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_DAY.format(crane.getOn()),cellStyleBody);
            createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_HOUR.format(crane.getOn()),cellStyleBody);
            createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_DAY.format(crane.getOff()),cellStyleBody);
            createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_HOUR.format(crane.getOff()),cellStyleBody);
            createCol(selectedRow, colRel++, findPeriodo(crane), cellStyleBody);
            createCol(selectedRow, colRel++,"", cellStyleSpace);
            createCol(selectedRow, colRel++, FORMATTER_DAY.format(crane.getAcionamento().getOn()),cellStyleBody);
            createCol(selectedRow, colRel++, FORMATTER_HOUR.format(crane.getAcionamento().getOn()),cellStyleBody);
            createCol(selectedRow, colRel++, portaToDescricao(crane.getAcionamento().getPortOn()),cellStyleBodyPort);
            createCol(selectedRow, colRel++, FORMATTER_HOUR.format(crane.getAcionamento().getOff()),cellStyleBody);
            createCol(selectedRow, colRel++, portaToDescricao(crane.getAcionamento().getPortOff()),cellStyleBodyPort);
            createCol(selectedRow, colRel++, diffDaysSecondsStr(crane.getAcionamento().getOn(), crane.getAcionamento().getOff()),cellStyleBody);
        }


    }

    public void generateCompleto(Sheet sheet,
                                 FatCompletoDtoV1 dataRel,
                                 CellStyle cellStyleTitle,
                                 CellStyle cellStyleBody,
                                 CellStyle cellStyleSpace) {

        List<List<CraneDataDtoV1>> listCrane = dataRel.getReportData();
        int row = 0;
        sheet.setDefaultColumnWidth(18);
        Row newRow = sheet.createRow(row++);
        int col = 0;
        for (int port = 0; port <=5; port++) {
            if (port != 1) {
                createCol(newRow,col++,portaToDescricao(port),cellStyleTitle);
                createCol(newRow,col++,"", cellStyleTitle);
                createCol(newRow,col++,"", cellStyleTitle);
                createCol(newRow,col++,"Tempo total", cellStyleTitle);
                createCol(newRow,col++, diffDaysSecondsStr(dataRel.getTempoCraneList().get(port) / 1000), cellStyleTitle);
                createCol(newRow,col++,"", cellStyleSpace);
            }
        }
        col = 0;
        newRow = sheet.createRow(row++);
        for (int port = 0; port <=5; port++) {
            if (port != 1) {
                createCol(newRow,col++,"Data de Início", cellStyleTitle);
                createCol(newRow,col++,"Hora de Início", cellStyleTitle);
                createCol(newRow,col++,"Data de Fim", cellStyleTitle);
                createCol(newRow,col++,"Hora de Fim", cellStyleTitle);
                createCol(newRow,col++,"Total", cellStyleTitle);
                createCol(newRow,col++,"", cellStyleSpace);
            }
        }

        for (int rowRel = 0; rowRel < dataRel.getMaxSize(); rowRel++ ) {
            int colRel = 0;
            Row selectedRow = sheet.createRow(rowRel + row);
            for (int port = 0; port <=5; port++) {
                if (port != 1) {
                    CraneDataDtoV1 crane = listCrane.get(port).get(rowRel);
                    createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_DAY.format(crane.getOn()),cellStyleBody);
                    createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_HOUR.format(crane.getOn()),cellStyleBody);
                    createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_DAY.format(crane.getOff()),cellStyleBody);
                    createCol(selectedRow, colRel++, crane.getOn() == null ? "" : FORMATTER_HOUR.format(crane.getOff()),cellStyleBody);
                    createCol(selectedRow, colRel++, findPeriodo(crane), cellStyleBody);
                    createCol(selectedRow, colRel++,"", cellStyleSpace);

                }
            }
        }
    }

    public String findPeriodo(CraneDataDtoV1 crane) {

        return diffDaysSecondsStr(crane.getOn(),crane.getOff());
    }

    public void createCol(Row row, int col, String value, CellStyle cellStyle) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }


    public List<CraneDataDtoV1> filterPort(List<CraneDataDtoV1> cranes, int port) {
        return cranes.stream().filter( c -> {
            return c.getPort().intValue() == port;
        }).sorted( (a,b) -> {
            return b.getOn().compareTo(a.getOn());
        }).collect(Collectors.toList());
    }

    public String portaToDescricao(int porta) {
        switch (porta) {
            case 0 :
                return "Ligado                            ";
            case 1:
                return "Em Movimento                      ";
            case 2:
                return "Motor do Carro                    ";
            case 3:
                return "Motor de Giro                     ";
            case 4:
                return "Motor de Elevação                 ";
            case 5:
                return "Emergência                        ";
            default:
                return "";
        }
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

    public double diffDaysSeconds(LocalDateTime fromDateTime, LocalDateTime toDateTime) {

        return (toDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - fromDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) / 1000d;
    }

    public String diffDaysSecondsStr(double segundosTotais) {

        long[] hora =  diffDaysSeconds(segundosTotais);
        return hora[0] + "h " + hora[1] + "min " + hora[2] + "s";
    }

    public String diffDaysSecondsStr(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        if (fromDateTime == null || toDateTime == null) {
            return "";
        }
        double segundosTotais = diffDaysSeconds(fromDateTime, toDateTime);
        return diffDaysSecondsStr(segundosTotais);
    }

    public long[] diffDaysSeconds(double segundosTotais) {
        double segundosEmHora = (60d * 60d); // 3600
        double segundosEmMinuto = 60d;
        double hours, minutes, seconds, resto;
        if (segundosTotais < segundosEmHora) {
            hours = 0;
        } else {
            resto  = segundosTotais % segundosEmHora;
            hours = (segundosTotais - resto) / segundosEmHora;
            segundosTotais = resto;
        }
        if (segundosTotais < segundosEmMinuto) {
            minutes = 0;
        } else {
            resto = segundosTotais % segundosEmMinuto;
            minutes = (segundosTotais - resto) / segundosEmMinuto;
            segundosTotais = resto;
        }
        seconds = segundosTotais;


        return new long[] {(long) hours, (long) minutes, (long) seconds};
    }

    private static long[] getTime(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        double segundosEmHora = (60d * 60d); // 3600
        double segundosEmMinuto = 60d;
        double hours, minutes, seconds, resto;
        double segundosTotais = (toDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - fromDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) / 1000;

        if (segundosTotais < segundosEmHora) {
            hours = 0;
        } else {
            resto  = segundosTotais % segundosEmHora;
            hours = (segundosTotais - resto) / segundosEmHora;
            segundosTotais = resto;
        }
        if (segundosTotais < segundosEmMinuto) {
            minutes = 0;
        } else {
            resto = segundosTotais % segundosEmMinuto;
            minutes = (segundosTotais - resto) / segundosEmMinuto;
            segundosTotais = resto;
        }
        seconds = segundosTotais;


        return new long[] {(long) hours, (long) minutes, (long) seconds};
    }

}
