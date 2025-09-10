package org.livraria.bibliotecasistema.repository;

import jakarta.persistence.Id;
import org.livraria.bibliotecasistema.models.LivroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Id> {

    Optional<LivroModel> findByIdLivro(Long id);
    Optional<LivroModel> findByTitulo(String titulo);
    Optional<LivroModel> findByTituloAndAutor(String titulo, String autor);
    List<LivroModel> findByAutorIgnoreCase(String autor);
    List<LivroModel> findByGeneroIgnoreCase(String genero);
    List<LivroModel> findByAnoPublicacao(int anoPublicacao);
    List<LivroModel> findBySetorIgnoreCase(String setor);
}
