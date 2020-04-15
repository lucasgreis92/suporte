package br.com.logicae.cianove.suporte.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FatCompletoDtoV1 {

    private List<CraneDataDtoV1> craneList;
    private List<Long> tempoCraneList = new ArrayList<>();
    private List<List<CraneDataDtoV1>> reportData;
    private List<CraneDataDtoV1> cicloAcionamento;
    private List<CraneDataDtoV1> motores;

    private int maxSize = 0;
    private LocalDateTime collectedIni;
    private LocalDateTime collectedFim;
    private Long tempoTotal = 0L;
    private Long tempoReal = 0L;

    public FatCompletoDtoV1(List<CraneDataDtoV1> craneList,
                            LocalDateTime collectedIni,
                            LocalDateTime collectedFim) {

        this.craneList = craneList;
        this.collectedIni = collectedIni;
        this.collectedFim = collectedFim;
        init();
    }

    public void init() {
        initCompleto();
        initParcial();
    }

    private void initParcial() {
        this.cicloAcionamento = new ArrayList<>();
        this.motores = new ArrayList<>();
        List<CraneDataDtoV1> craneListAsc = new ArrayList<>();
        this.reportData.get(0)
                .stream().filter( c -> {
            return c.getOn() != null && c.getOff() != null;
        }).collect(Collectors.toList()).forEach( c -> {
            craneListAsc.add(new CraneDataDtoV1(LocalDateTime.from(c.getOn()),LocalDateTime.from(c.getOff())));
        });
        this.motores.addAll(this.reportData.get(2));
        this.motores.addAll(this.reportData.get(3));
        this.motores.addAll(this.reportData.get(4));
        this.motores = this.motores.stream().filter( c -> {
            return c.getOn() != null && c.getOff() != null;
        }).sorted( (a,b) -> {
            return a.getOn().compareTo(b.getOn());
        }).collect(Collectors.toList());

        craneListAsc.sort( (a, b) -> {
            return a.getOn().compareTo(b.getOn());
        });

        agruparDtInicial(craneListAsc);
        adicionarFaltantes(craneListAsc);
        setarDtFinal(craneListAsc);
        setarDtInicio();
        setMotores();
        setMotoresFinal();
        setMotoresInicio();
        calcularTotais();
    }

    private void calcularTotais(){
        for (CraneDataDtoV1 craneData : this.cicloAcionamento) {
            tempoTotal += craneData.getOff().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    - craneData.getOn().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            tempoReal += craneData.getAcionamento().getOff().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    - craneData.getAcionamento().getOn().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }

    private void setMotoresInicio() {
        CraneDataDtoV1 lastCrane = null;
        for (CraneDataDtoV1 crane: this.cicloAcionamento) {
            if (lastCrane != null
                    && lastCrane.getAcionamento().getOff()
                    .equals(lastCrane.getAcionamento().getOff().toLocalDate().atTime(23,59,59))){
                crane.getAcionamento().setOn(crane.getOn().toLocalDate().atTime(0,0,0));
                crane.getAcionamento().setPortOn(lastCrane.getAcionamento().getPortOff());
            }
            lastCrane = crane;
        }
    }

    private void setMotoresFinal() {
        this.cicloAcionamento.forEach(c -> {
            if (c.getOn().toLocalDate().isBefore(c.getAcionamento().getOff().toLocalDate())) {
                c.getAcionamento().setOff(c.getOn().toLocalDate().atTime(23,59,59));
            }
        });
    }

    private void setMotores() {
        this.cicloAcionamento.forEach( c -> {
            List<CraneDataDtoV1> cranes = this.motores.stream().filter( m -> {
                return c.getOn().toLocalDate().equals(m.getOn().toLocalDate());
            }).sorted((a, b) -> {
                return a.getOn().compareTo(b.getOn()) ;
            }).collect(Collectors.toList());
            CraneDataDtoV1 craneIni = cranes.get(0);
            cranes = cranes.stream().sorted((a, b) -> {
                return b.getOff().compareTo(a.getOff()) ;
            }).collect(Collectors.toList());

            CraneDataDtoV1 craneFim = cranes.get(0);
            c.setAcionamento(new AcionamentoDtoV1(craneIni.getOn(),craneFim.getOff(), craneIni.getPort(), craneFim.getPort()));
        });
    }

    private void agruparDtInicial(List<CraneDataDtoV1> craneListAsc ) {
        for (CraneDataDtoV1 crane : craneListAsc) {
            if (!existeDataOn(crane.getOn().toLocalDate()).isPresent()) {
                cicloAcionamento.add(crane);
            }
        }
    }

    private void adicionarFaltantes(List<CraneDataDtoV1> craneListAsc) {
        LocalDate inicio = collectedIni.toLocalDate();
        LocalDate fim = collectedFim.toLocalDate();
        while (inicio.isBefore(fim) || inicio.equals(fim)) {
            if (existDataEmIntervalo(inicio, craneListAsc)
                    && !existeDataOn(inicio).isPresent()){
                cicloAcionamento.add(new CraneDataDtoV1(inicio.atTime(0,0,0), null));
            }

            inicio = inicio.plusDays(1l);
        }
        cicloAcionamento.sort( (a, b) -> {
            return a.getOn().compareTo(b.getOn());
        });
    }

    private boolean existDataEmIntervalo(LocalDate dt, List<CraneDataDtoV1> craneListAsc) {
        return craneListAsc.stream().filter( c -> {
            return (dt.equals(c.getOn().toLocalDate()) || dt.isAfter(c.getOn().toLocalDate()))
                    && (dt.equals(c.getOff().toLocalDate()) || dt.isBefore(c.getOff().toLocalDate()));
        }).findFirst().isPresent();
    }

    private void setarDtInicio() {
        CraneDataDtoV1 craneUlt = null;
        for (CraneDataDtoV1 craneCiclo : cicloAcionamento) {
            if (craneUlt == null) {
                craneUlt = craneCiclo;
            } else if (craneUlt.getOff()
                    .equals(LocalDate.from(craneUlt.getOff().toLocalDate()).atTime(23,59,59) )) {
                craneCiclo.setOn(LocalDate.from(craneCiclo.getOn().toLocalDate()).atTime(0,0,0));
            }

            craneUlt = craneCiclo;
        }
    }



    private void setarDtFinal(List<CraneDataDtoV1> craneListAsc ) {
        for (CraneDataDtoV1 craneCiclo : cicloAcionamento) {
            if (craneCiclo.getOff() == null) {
                Optional<CraneDataDtoV1> crane = findLastDtOff(craneCiclo.getOn().toLocalDate(), craneListAsc);
                if (crane.isPresent()) {
                    craneCiclo.setOff(crane.get().getOff());
                } else {
                    craneCiclo.setOff(craneCiclo.getOn().toLocalDate().atTime(23,59,59));
                }
            }
        }
        for (CraneDataDtoV1 craneCiclo : cicloAcionamento) {
            CraneDataDtoV1 craneFim = null;
            for (CraneDataDtoV1 crane : craneListAsc) {
                if (craneCiclo.getOn().toLocalDate().equals(crane.getOn().toLocalDate())) {
                    if (craneFim == null) {
                        craneFim = crane;

                    } else if (crane.getOff().isAfter(craneFim.getOff())) {
                        craneFim = crane;
                    }
                }
            }
            if (craneFim != null && craneFim.getOff().toLocalDate().isAfter(craneFim.getOn().toLocalDate())) {
                craneFim.setOff(LocalDate.from(craneFim.getOn().toLocalDate()).atTime(23,59, 59));
            }
            if (craneFim != null) {
                craneCiclo.setOff(craneFim.getOff());
            }
        }
    }


    public Optional<CraneDataDtoV1> existeDataOn(LocalDate data) {
        return cicloAcionamento.stream().filter( c -> {
            return c.getOn().toLocalDate().equals(data);
        }).findFirst();
    }

    public Optional<CraneDataDtoV1> findLastDtOff(LocalDate data, List<CraneDataDtoV1> craneListAsc) {
        CraneDataDtoV1 craneRet = null;
        List<CraneDataDtoV1> listaDia = craneListAsc.stream().filter( c -> {
            return c.getOff().toLocalDate().equals(data);
        }).collect(Collectors.toList());

        for (CraneDataDtoV1 crane : listaDia) {
            if (craneRet == null) {
                craneRet = crane;
            }else if (craneRet.getOff().isBefore(crane.getOff())) {
                craneRet = crane;
            }
        }
        if (craneRet == null) {
            return Optional.empty();
        }
        return Optional.of(craneRet);
    }

    private void initCompleto() {
        List<List<CraneDataDtoV1>> retorno = new ArrayList<>();

        for (int port = 0; port <=5; port++) {
            List<CraneDataDtoV1> cranesPort = filterPort(port);
            Long tempoCrane = 0L;
            for (CraneDataDtoV1 craneData : cranesPort) {
                tempoCrane += craneData.getOff().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        - craneData.getOn().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            tempoCraneList.add(tempoCrane);
            if (cranesPort.size() > maxSize) {
                maxSize = cranesPort.size();
            }
            retorno.add(cranesPort);

        }
        for (List<CraneDataDtoV1> lista : retorno) {
            while (lista.size() < maxSize) {
                lista.add(new CraneDataDtoV1());
            }
        }
        this.reportData = retorno;
    }

    public List<List<CraneDataDtoV1>> getReportData() {
        return reportData;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public List<Long> getTempoCraneList() {
        return tempoCraneList;
    }

    public List<CraneDataDtoV1> filterPort(int port) {
        return craneList.stream().filter( c -> {
            return c.getPort().intValue() == port;
        }).sorted( (a,b) -> {
            return b.getOn().compareTo(a.getOn());
        }).collect(Collectors.toList());
    }

    public List<CraneDataDtoV1> getCraneList() {
        return craneList;
    }

    public LocalDateTime getCollectedIni() {
        return collectedIni;
    }

    public LocalDateTime getCollectedFim() {
        return collectedFim;
    }

    public List<CraneDataDtoV1> getCicloAcionamento() {
        return cicloAcionamento;
    }

    public List<CraneDataDtoV1> getMotores() {
        return motores;
    }

    public Long getTempoTotal() {
        return tempoTotal;
    }

    public Long getTempoReal() {
        return tempoReal;
    }
}
