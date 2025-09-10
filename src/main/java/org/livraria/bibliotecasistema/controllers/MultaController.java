package org.livraria.bibliotecasistema.controllers;

import jakarta.validation.Valid;
import org.livraria.bibliotecasistema.dtos.request.MultaRequestDto;
import org.livraria.bibliotecasistema.dtos.response.MultaResponseDto;
import org.livraria.bibliotecasistema.models.EmprestimoModel;
import org.livraria.bibliotecasistema.models.EmprestimoModel.StatusEmprestimo;
import org.livraria.bibliotecasistema.models.MultaModel;
import org.livraria.bibliotecasistema.models.MultaModel.MultaStatus;
import org.livraria.bibliotecasistema.repository.EmprestimoRepository;
import org.livraria.bibliotecasistema.repository.MultaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/multas")
public class MultaController {

    final MultaRepository multaRepository;
    final EmprestimoRepository emprestimoRepository;

    public MultaController(MultaRepository multaRepository, EmprestimoRepository emprestimoRepository) {
        this.multaRepository = multaRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody @Valid MultaRequestDto requestDto) {
        Optional<EmprestimoModel> optionalEmprestimo = this.emprestimoRepository.findByIdEmprestimo(requestDto.emprestimoId());

        if (optionalEmprestimo.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Empréstimo com id=" + requestDto.emprestimoId() + " não encontrado.");

        if (!emprestimoEstaAtrasado(optionalEmprestimo.get()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Empréstimo com id: " + requestDto.emprestimoId()+ " está dentro da data de devolução.");

        if (this.multaRepository.findByEmprestimo(optionalEmprestimo.get()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Já existe uma multa para empréstimo com id=" + requestDto.emprestimoId());


        MultaModel multa = new MultaModel(optionalEmprestimo.get());

        this.multaRepository.save(multa);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.constroiResponseDto(multa));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> encerraMulta(@PathVariable(value = "id", required = true) Long id) {
        Optional<MultaModel> multa = this.multaRepository.findByIdMulta(id);

        if (multa.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Multa com id=" + id + " não encontrado.");

        if (multa.get().getStatus().equals(MultaStatus.FINALIZADO))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Multa com id=" + id + "já está finalizada.");

        multa.get().setStatus(MultaStatus.FINALIZADO);

        this.multaRepository.save(multa.get());

        return ResponseEntity.status(HttpStatus.OK).body(this.constroiResponseDto(multa.get()));
    }

    private boolean emprestimoEstaAtrasado(EmprestimoModel emprestimo) {
        return LocalDateTime.now().isAfter(emprestimo.getDataDevolucao()) &&
                emprestimo.getStatus().equals(StatusEmprestimo.EM_ANDAMENTO);
    }

    private MultaResponseDto constroiResponseDto(MultaModel model) {
        return new MultaResponseDto(model.getIdMulta(), model.getEmprestimo().getIdEmprestimo(), model.getCriadoEm(), model.getStatus());
    }
}
//mudar data de dataDevolucao para o dia corrente quando o status for finalizado