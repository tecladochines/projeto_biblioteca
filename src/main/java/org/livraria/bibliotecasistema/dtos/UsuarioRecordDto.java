package org.livraria.bibliotecasistema.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRecordDto(@NotBlank String nome, @NotBlank String cpf, @NotBlank String email,
                               @NotBlank String senha, @NotNull int tipoUsuario) {
}
