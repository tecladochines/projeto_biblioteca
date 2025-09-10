package org.livraria.bibliotecasistema.repository;

import jakarta.persistence.Id;
import org.livraria.bibliotecasistema.models.ConfiguracoesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracoesRepository extends JpaRepository<ConfiguracoesModel, Long> {
    Optional<ConfiguracoesModel> findByChave(String chave);
}
