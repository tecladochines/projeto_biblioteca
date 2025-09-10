package org.livraria.bibliotecasistema.controllers;

import jakarta.validation.Valid;
import org.livraria.bibliotecasistema.dtos.request.ConfiguracaoRequestDto;
import org.livraria.bibliotecasistema.dtos.response.ConfiguracaoResponseDto;
import org.livraria.bibliotecasistema.models.ConfiguracoesModel;
import org.livraria.bibliotecasistema.repository.ConfiguracoesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/configuracoes")
public class ConfiguracoesController {
    private ConfiguracoesRepository configuracoesRepository;

    ConfiguracoesController(ConfiguracoesRepository repository) {
        this.configuracoesRepository = repository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ConfiguracaoRequestDto dto) {
        if (this.configuracoesRepository.findByChave(dto.chave()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Configuração já existe.");
        }

        ConfiguracoesModel configuracoes = new ConfiguracoesModel(dto.chave(), dto.valor());

        this.configuracoesRepository.save(configuracoes);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.constroiResponse(configuracoes));
    }

    @PutMapping
    public ResponseEntity<?> atualiza(@RequestBody @Valid ConfiguracaoRequestDto dto) {
        Optional<ConfiguracoesModel> configuracao = this.configuracoesRepository.findByChave(dto.chave());

        if (configuracao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuração não encontrada.");
        }

        configuracao.get().setChave(dto.chave());
        configuracao.get().setValor(dto.valor());

        this.configuracoesRepository.save(configuracao.get());

        return ResponseEntity.status(HttpStatus.OK).body(this.constroiResponse(configuracao.get()));
    }

    private ConfiguracaoResponseDto constroiResponse(ConfiguracoesModel model) {
        return new ConfiguracaoResponseDto(model.getIdConfiguracao(), model.getChave(), model.getValor());
    }
}
