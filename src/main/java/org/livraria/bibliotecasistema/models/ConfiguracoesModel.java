package org.livraria.bibliotecasistema.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "TB_CONFIGURACOES")
public class ConfiguracoesModel implements Serializable {

    public ConfiguracoesModel(String chave, String valor) {
        this.chave = chave;
        this.valor = valor;
    }

    private static final long serialVersion = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idConfiguracao;

    private String chave;
    private String valor;

    public ConfiguracoesModel() {

    }

    public Long getIdConfiguracao() {
        return idConfiguracao;
    }

    public void setIdConfiguracao(Long idConfiguracao) {
        this.idConfiguracao = idConfiguracao;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
