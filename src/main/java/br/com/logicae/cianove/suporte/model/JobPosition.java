package br.com.logicae.cianove.suporte.model;

import javax.persistence.*;

@Entity
@Table(name = "job_position")
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_job_position")
    private Long cdJobPosition;

    @Basic(optional = false)
    @Column(name = "ds_job_position")
    private String dsJobPosition;

    public Long getCdJobPosition() {
        return cdJobPosition;
    }

    public void setCdJobPosition(Long cdJobPosition) {
        this.cdJobPosition = cdJobPosition;
    }

    public String getDsJobPosition() {
        return dsJobPosition;
    }

    public void setDsJobPosition(String dsJobPosition) {
        this.dsJobPosition = dsJobPosition;
    }
}
