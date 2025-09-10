package org.livraria.bibliotecasistema.dtos.response;

import org.livraria.bibliotecasistema.models.MultaModel.MultaStatus;

import java.time.LocalDateTime;

public record MultaResponseDto(
        long id,
        long emprestimoId,
        LocalDateTime criadoEm,
        MultaStatus status
) {}
