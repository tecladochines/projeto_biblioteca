package org.livraria.bibliotecasistema.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MULTAS")

public class MultaModel implements Serializable {

    private static final long serialVersion = 1L;

    public MultaModel(EmprestimoModel emprestimo) {
        this.emprestimo = emprestimo;
        this.status = MultaStatus.EM_ANDAMENTO;
        this.criadoEm = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMulta;
    private LocalDateTime criadoEm;

    @ManyToOne
    @JoinColumn(name = "emprestimo_id", nullable = false)
    private EmprestimoModel emprestimo;

    @Enumerated(EnumType.STRING)
    private MultaStatus status;

    public MultaModel() {

    }

    public enum MultaStatus {
        EM_ANDAMENTO, FINALIZADO
    }

    public MultaStatus getStatus() {
        return status;
    }

    public void setStatus(MultaStatus status) {
        this.status = status;
    }

    public EmprestimoModel getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(EmprestimoModel emprestimo) {
        this.emprestimo = emprestimo;
    }

    public Long getIdMulta() {
        return idMulta;
    }

    public void setIdMulta(Long idMulta) {
        this.idMulta = idMulta;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
