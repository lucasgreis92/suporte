package br.com.logicae.cianove.suporte.dto;

public class ClientDtoV1 {

    private Long cdClient;

    private String dsClient;

    private String dsCpfcnpj;

    public Long getCdClient() {
        return cdClient;
    }

    public void setCdClient(Long cdClient) {
        this.cdClient = cdClient;
    }

    public String getDsClient() {
        return dsClient;
    }

    public void setDsClient(String dsClient) {
        this.dsClient = dsClient;
    }

    public String getDsCpfcnpj() {
        return dsCpfcnpj;
    }

    public void setDsCpfcnpj(String dsCpfcnpj) {
        this.dsCpfcnpj = dsCpfcnpj;
    }
}
