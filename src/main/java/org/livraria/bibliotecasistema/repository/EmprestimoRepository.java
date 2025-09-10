package org.livraria.bibliotecasistema.repository;

import jakarta.persistence.Id;
import org.livraria.bibliotecasistema.models.EmprestimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoModel, Id> {
    Optional<EmprestimoModel> findByIdEmprestimo(Long id);
}
