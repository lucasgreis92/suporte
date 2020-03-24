package br.com.logicae.cianove.suporte.controller.v1;

import br.com.logicae.cianove.suporte.dto.SensorsDtoV1;
import br.com.logicae.cianove.suporte.service.client.geo.GSensorsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/rs/v1/sensors")
public class SensorsControlleV1 {

    @Autowired
    private GSensorsClientService gSensorsClientService;

    @GetMapping("findstatus/{device}/{port}")
    public SensorsDtoV1 findStatus(@PathVariable("device") String device, @PathVariable("port") Long port) {
        return gSensorsClientService.findStatus(device,port);
    }
}
