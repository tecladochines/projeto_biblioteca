package org.livraria.bibliotecasistema.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_LIVROS")

public class LivroModel implements Serializable {

    private static final long serialVersion = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLivro;
    private String titulo;
    private String autor;
    private String genero;
    private int anoPublicacao;
    private String setor;
    private int qtdCadastro;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExemplarModel> exemplares = new ArrayList<>();

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public int getQtdCadastro() {
        return qtdCadastro;
    }

    public void setQtdCadastro(int qtdCadastro) {
        this.qtdCadastro = qtdCadastro;
    }

    public List<ExemplarModel> getExemplares() {
        return exemplares;
    }

    public void setExemplares(List<ExemplarModel> exemplares) {
        this.exemplares = exemplares;
    }
}
