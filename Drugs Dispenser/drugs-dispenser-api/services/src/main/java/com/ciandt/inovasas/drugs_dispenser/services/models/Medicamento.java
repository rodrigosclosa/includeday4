package com.ciandt.inovasas.drugs_dispenser.services.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

import javax.annotation.Nullable;

/**
 * Created by rodrigosclosa on 05/07/16.
 */

@Entity
public class Medicamento {
    @Id
    private Long id;

    @Index
    @Nullable
    private Integer codigoMedicamento;
    @Nullable
    private String descricaoMedicamento;
    @Nullable
    private Date validadeMedicamento;

    public Medicamento() {
    }

    public Medicamento(Integer codigoMedicamento, String descricaoMedicamento, Date validadeMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
        this.descricaoMedicamento = descricaoMedicamento;
        this.validadeMedicamento = validadeMedicamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public Integer getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(@Nullable Integer codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    @Nullable
    public String getDescricaoMedicamento() {
        return descricaoMedicamento;
    }

    public void setDescricaoMedicamento(@Nullable String descricaoMedicamento) {
        this.descricaoMedicamento = descricaoMedicamento;
    }

    @Nullable
    public Date getValidadeMedicamento() {
        return validadeMedicamento;
    }

    public void setValidadeMedicamento(@Nullable Date validadeMedicamento) {
        this.validadeMedicamento = validadeMedicamento;
    }
}
