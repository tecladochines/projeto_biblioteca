package org.livraria.bibliotecasistema.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroRecordDto(@NotBlank String titulo, @NotBlank String autor,
                             @NotBlank String genero, @NotNull int anoPublicacao,
                             @NotBlank String setor, @NotNull int qtdCadastro) {
}
