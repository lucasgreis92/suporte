package br.com.logicae.cianove.suporte.controller.v1;

import br.com.logicae.cianove.suporte.dto.CommunicationDtoV1;
import br.com.logicae.cianove.suporte.service.CommunicationService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("public/rs/v1/reportdata")
public class ReportDataControlleV1 {

    @Autowired
    private CommunicationService communicationService;

    @GetMapping("communication/filter")
    public CommunicationDtoV1 findByFilter(@RequestParam(required = true) String device,
                                           @RequestParam(required = false) Long port ,
                                           @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime collectedIni,
                                           @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime collectedFim) {

        return communicationService.findByFilter(device, port,collectedIni,collectedFim);
    }

    @RequestMapping(value = "communication/filter/relatorio.xlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] gerarRelatorioComunicacao(@RequestParam(required = true) String device,
                    @RequestParam(required = false) Long port ,
                    @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime collectedIni,
                    @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime collectedFim) throws IOException {

        return communicationService.gerarRelatorioComunicacao(device,port,collectedIni,collectedFim);

    }

    @RequestMapping(value = "faturamento/filter/relatorio.xlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] gerarRelatorioFaturamento(@RequestParam(required = true) String device,
                                              @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime collectedIni,
                                              @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime collectedFim) throws IOException {

        return communicationService.gerarRelatorioFaturamento(device, collectedIni, collectedFim);

    }

}
