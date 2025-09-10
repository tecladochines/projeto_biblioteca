package org.livraria.bibliotecasistema.dtos;

import jakarta.validation.constraints.NotNull;

public record MultaRecordDto(@NotNull int multaInicial, @NotNull int periodoEmprestimo) {
}
