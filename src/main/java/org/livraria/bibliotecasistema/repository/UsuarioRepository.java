package org.livraria.bibliotecasistema.repository;


import jakarta.persistence.Id;
import org.livraria.bibliotecasistema.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Id> {
    Optional<UsuarioModel> findByIdUsuario(Long id);
    Optional<UsuarioModel> findByNomeIgnoreCase(String nome);
    Optional<UsuarioModel> findByCpf(String cpf);
    Optional<UsuarioModel> findByEmail(String email);
    List<UsuarioModel> findByTipoUsuario(int tipoUsuario);

    @Query(value = """
        SELECT u.*
        FROM tb_usuarios AS u
            INNER JOIN tb_emprestimos e ON u.id_usuario = e.usuario_id
            INNER JOIN tb_multas m ON e.id_emprestimo = m.emprestimo_id
            WHERE m.status = 'EM_ANDAMENTO'
        """, nativeQuery = true)
    List<UsuarioModel> findEmailsDeUsuariosComMultaEmAndamento();
}
