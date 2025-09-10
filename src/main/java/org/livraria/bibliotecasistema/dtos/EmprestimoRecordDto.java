package org.livraria.bibliotecasistema.dtos;

import jakarta.validation.constraints.NotNull;

public record EmprestimoRecordDto(
        @NotNull Long usuarioId,            // ID do Usuário que vai fazer o empréstimo
        @NotNull Long tomboId              // ID do Exemplar do livro que será emprestado
){
}

