package org.livraria.bibliotecasistema.repository;

import org.livraria.bibliotecasistema.models.ExemplarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExemplarRepository extends JpaRepository<ExemplarModel, Long> {

    Optional<ExemplarModel> findByIdTombo(Long idTombo);
    List<ExemplarModel> findByLivro_IdLivro(Long livroId);
}

