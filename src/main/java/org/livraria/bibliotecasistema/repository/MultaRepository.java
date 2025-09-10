package org.livraria.bibliotecasistema.repository;

import jakarta.persistence.Id;
import org.livraria.bibliotecasistema.models.EmprestimoModel;
import org.livraria.bibliotecasistema.models.MultaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MultaRepository extends JpaRepository<MultaModel, Id> {
    Optional<MultaModel> findByIdMulta(Long id);
    Optional<MultaModel> findByEmprestimo(EmprestimoModel emprestimo);
}