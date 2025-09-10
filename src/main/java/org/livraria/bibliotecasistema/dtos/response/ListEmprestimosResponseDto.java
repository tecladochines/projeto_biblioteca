package org.livraria.bibliotecasistema.dtos.response;

import org.livraria.bibliotecasistema.models.EmprestimoModel.StatusEmprestimo;

import java.time.LocalDateTime;

public record ListEmprestimosResponseDto(
        String nome,
        String cpf,
        String tituloLivro,
        long tomboId,
        LocalDateTime dataEmprestimo,
        LocalDateTime dataDevolucao,
        StatusEmprestimo status
) {
}
