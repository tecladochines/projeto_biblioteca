package org.livraria.bibliotecasistema.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "TB_EXEMPLARES")
public class ExemplarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTombo;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    @JsonIgnore
    private LivroModel livro;

    @OneToMany(mappedBy = "exemplar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmprestimoModel> emprestimos;  // Relação um para muitos com Emprestimo


    public enum StatusExemplar {
        DISPONIVEL, EMPRESTADO, INDISPONIVEL;
    }

    @Enumerated(EnumType.STRING)
    private StatusExemplar status = StatusExemplar.DISPONIVEL;  // Definido como DISPONÍVEL por padrão


    // Getters e Setters
    public Long getIdTombo() {
        return idTombo;
    }

    public void setIdTombo(Long idTombo) {
        this.idTombo = idTombo;
    }

    public LivroModel getLivro() {
        return livro;
    }

    public void setLivro(LivroModel livro) {
        this.livro = livro;
    }

    public StatusExemplar getStatus() {
        return status;
    }

    public void setStatus(StatusExemplar status) {
        this.status = status;
    }

    public List<EmprestimoModel> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<EmprestimoModel> emprestimos) {
        this.emprestimos = emprestimos;
    }
}

