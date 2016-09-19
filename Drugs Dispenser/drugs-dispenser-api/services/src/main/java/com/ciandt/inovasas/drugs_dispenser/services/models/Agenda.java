package com.ciandt.inovasas.drugs_dispenser.services.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

import javax.annotation.Nullable;

/**
 * Created by rodrigosclosa on 01/07/16.
 */
@Entity
public class Agenda {

    @Id
    private Long id;
    @Index
    private Long idDispenser;
    @Ignore
    private Dispenser dispenser;
    @Index
    private Integer numeroMedicamento;
    private Integer dosagem;
    @Ignore
    private Medicamento medicamento;

    private Date dataInicio;
    private Integer intervaloMinutos;
    @Index
    @Nullable
    private Date ultimaDosagem;
    @Nullable
    private boolean dosagemCaiu;
    @Nullable
    private boolean dosagemRetirada;

    public Agenda() {
    }

    public Agenda(Long id, Long idDispenser, Dispenser dispenser, Integer numeroMedicamento, Integer dosagem, Date dataInicio, Integer intervaloMinutos, Date ultimaDosagem, boolean dosagemCaiu, boolean dosagemRetirada) {
        this.id = id;
        this.idDispenser = idDispenser;
        this.dispenser = dispenser;
        this.numeroMedicamento = numeroMedicamento;
        this.dosagem = dosagem;
        this.dataInicio = dataInicio;
        this.intervaloMinutos = intervaloMinutos;
        this.ultimaDosagem = ultimaDosagem;
        this.dosagemCaiu = dosagemCaiu;
        this.dosagemRetirada = dosagemRetirada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDispenser() {
        return idDispenser;
    }

    public void setIdDispenser(Long idDispenser) {
        this.idDispenser = idDispenser;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Integer getIntervaloMinutos() {
        return intervaloMinutos;
    }

    public void setIntervaloMinutos(Integer intervaloMinutos) {
        this.intervaloMinutos = intervaloMinutos;
    }

    @Nullable
    public Date getUltimaDosagem() {
        return ultimaDosagem;
    }

    public void setUltimaDosagem(@Nullable Date ultimaDosagem) {
        this.ultimaDosagem = ultimaDosagem;
    }

    @Nullable
    public boolean isDosagemCaiu() {
        return dosagemCaiu;
    }

    public void setDosagemCaiu(@Nullable boolean dosagemCaiu) {
        this.dosagemCaiu = dosagemCaiu;
    }

    @Nullable
    public boolean isDosagemRetirada() {
        return dosagemRetirada;
    }

    public void setDosagemRetirada(@Nullable boolean dosagemRetirada) {
        this.dosagemRetirada = dosagemRetirada;
    }

    public Dispenser getDispenser() {
        return dispenser;
    }

    public void setDispenser(Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    public Integer getNumeroMedicamento() {
        return numeroMedicamento;
    }

    public void setNumeroMedicamento(Integer numeroMedicamento) {
        this.numeroMedicamento = numeroMedicamento;
    }

    public Integer getDosagem() {
        return dosagem;
    }

    public void setDosagem(Integer dosagem) {
        this.dosagem = dosagem;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
}
