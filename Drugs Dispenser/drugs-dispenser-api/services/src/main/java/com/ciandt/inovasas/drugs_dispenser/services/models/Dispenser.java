package com.ciandt.inovasas.drugs_dispenser.services.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

import javax.annotation.Nullable;

/**
 * Created by rodrigosclosa on 30/06/16.
 */
@Entity
public class Dispenser {

    @Id
    private Long id;
    private String nomeDispenser;
    @Index
    private String codigo;
    private Date dataCadastro;

    @Index
    @Nullable
    private String carteirinhaAssociado;

    @Index
    @Nullable
    private Integer codigoMedicamento1;
    @Nullable
    private String descricaoMedicamento1;
    @Nullable
    private Date validadeMedicamento1;

    @Index
    @Nullable
    private Integer codigoMedicamento2;
    @Nullable
    private String descricaoMedicamento2;
    @Nullable
    private Date validadeMedicamento2;

    @Index
    @Nullable
    private Integer codigoMedicamento3;
    @Nullable
    private String descricaoMedicamento3;
    @Nullable
    private Date validadeMedicamento3;

    @Index
    @Nullable
    private Integer codigoMedicamento4;
    @Nullable
    private String descricaoMedicamento4;
    @Nullable
    private Date validadeMedicamento4;

    public Dispenser() {
    }

    public Dispenser(Long id, String nomeDispenser, String codigo, Date dataCadastro, String carteirinhaAssociado) {
        this.id = id;
        this.nomeDispenser = nomeDispenser;
        this.codigo = codigo;
        this.dataCadastro = dataCadastro;
        this.carteirinhaAssociado = carteirinhaAssociado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDispenser() {
        return nomeDispenser;
    }

    public void setNomeDispenser(String nomeDispenser) {
        this.nomeDispenser = nomeDispenser;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Nullable
    public String getCarteirinhaAssociado() {
        return carteirinhaAssociado;
    }

    public void setCarteirinhaAssociado(@Nullable String carteirinhaAssociado) {
        this.carteirinhaAssociado = carteirinhaAssociado;
    }

    @Nullable
    public Integer getCodigoMedicamento1() {
        return codigoMedicamento1;
    }

    public void setCodigoMedicamento1(@Nullable Integer codigoMedicamento1) {
        this.codigoMedicamento1 = codigoMedicamento1;
    }

    @Nullable
    public String getDescricaoMedicamento1() {
        return descricaoMedicamento1;
    }

    public void setDescricaoMedicamento1(@Nullable String descricaoMedicamento1) {
        this.descricaoMedicamento1 = descricaoMedicamento1;
    }

    @Nullable
    public Date getValidadeMedicamento1() {
        return validadeMedicamento1;
    }

    public void setValidadeMedicamento1(@Nullable Date validadeMedicamento1) {
        this.validadeMedicamento1 = validadeMedicamento1;
    }

    @Nullable
    public Integer getCodigoMedicamento2() {
        return codigoMedicamento2;
    }

    public void setCodigoMedicamento2(@Nullable Integer codigoMedicamento2) {
        this.codigoMedicamento2 = codigoMedicamento2;
    }

    @Nullable
    public String getDescricaoMedicamento2() {
        return descricaoMedicamento2;
    }

    public void setDescricaoMedicamento2(@Nullable String descricaoMedicamento2) {
        this.descricaoMedicamento2 = descricaoMedicamento2;
    }

    @Nullable
    public Date getValidadeMedicamento2() {
        return validadeMedicamento2;
    }

    public void setValidadeMedicamento2(@Nullable Date validadeMedicamento2) {
        this.validadeMedicamento2 = validadeMedicamento2;
    }

    @Nullable
    public Integer getCodigoMedicamento3() {
        return codigoMedicamento3;
    }

    public void setCodigoMedicamento3(@Nullable Integer codigoMedicamento3) {
        this.codigoMedicamento3 = codigoMedicamento3;
    }

    @Nullable
    public String getDescricaoMedicamento3() {
        return descricaoMedicamento3;
    }

    public void setDescricaoMedicamento3(@Nullable String descricaoMedicamento3) {
        this.descricaoMedicamento3 = descricaoMedicamento3;
    }

    @Nullable
    public Date getValidadeMedicamento3() {
        return validadeMedicamento3;
    }

    public void setValidadeMedicamento3(@Nullable Date validadeMedicamento3) {
        this.validadeMedicamento3 = validadeMedicamento3;
    }

    @Nullable
    public Integer getCodigoMedicamento4() {
        return codigoMedicamento4;
    }

    public void setCodigoMedicamento4(@Nullable Integer codigoMedicamento4) {
        this.codigoMedicamento4 = codigoMedicamento4;
    }

    @Nullable
    public String getDescricaoMedicamento4() {
        return descricaoMedicamento4;
    }

    public void setDescricaoMedicamento4(@Nullable String descricaoMedicamento4) {
        this.descricaoMedicamento4 = descricaoMedicamento4;
    }

    @Nullable
    public Date getValidadeMedicamento4() {
        return validadeMedicamento4;
    }

    public void setValidadeMedicamento4(@Nullable Date validadeMedicamento4) {
        this.validadeMedicamento4 = validadeMedicamento4;
    }
}
