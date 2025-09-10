package org.livraria.bibliotecasistema.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.servlet.http.PushBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_EMPRESTIMOS")

public class EmprestimoModel implements Serializable {

    private static final long serialVersion = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEmprestimo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime dataEmprestimo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime dataDevolucao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;  // Relação muitos para um com Usuario

    @ManyToOne
    @JoinColumn(name = "tombo_id", nullable = false)
    private ExemplarModel exemplar;  // Relação muitos para um com Exemplar

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status = StatusEmprestimo.EM_ANDAMENTO;

    public enum StatusEmprestimo {
        EM_ANDAMENTO, FINALIZADO;
    }

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(Long idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public LocalDateTime getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public ExemplarModel getExemplar() {
        return exemplar;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setExemplar(ExemplarModel exemplar) {
        this.exemplar = exemplar;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }


}