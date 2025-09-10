package org.livraria.bibliotecasistema.dtos.response;

import org.livraria.bibliotecasistema.models.EmprestimoModel.StatusEmprestimo;

import java.time.LocalDateTime;

public record EmprestimoResponseDto(
        long id,
        LocalDateTime dataEmprestimo,
        LocalDateTime dataDevolucao,
        long usuario,
        long exemplar,
        StatusEmprestimo status
) {}
