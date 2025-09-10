package org.livraria.bibliotecasistema.controllers;

import org.livraria.bibliotecasistema.models.ExemplarModel;
import org.livraria.bibliotecasistema.models.LivroModel;
import org.livraria.bibliotecasistema.repository.ExemplarRepository;
import org.livraria.bibliotecasistema.repository.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exemplares")
public class ExemplarController {

    final ExemplarRepository exemplarRepository;
    final LivroRepository livroRepository;

    public ExemplarController(ExemplarRepository exemplarRepository, LivroRepository livroRepository) {
        this.exemplarRepository = exemplarRepository;
        this.livroRepository = livroRepository;
    }

    @GetMapping("/{idTombo}")
    public ResponseEntity<Object> getExemplarById(@PathVariable Long idTombo) {
        Optional<ExemplarModel> exemplar = exemplarRepository.findByIdTombo(idTombo);
        if (exemplar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exemplar não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(exemplar.get());
    }

    @GetMapping("/livro/{livroId}")
    public ResponseEntity<Object> getExemplaresByLivro(@PathVariable Long livroId) {
        List<ExemplarModel> exemplares = exemplarRepository.findByLivro_IdLivro(livroId);
        if (exemplares.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum exemplar encontrado para este livro.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(exemplares);
    }

    @DeleteMapping("/{idTombo}")
    public ResponseEntity<Object> excluirExemplar(@PathVariable Long idTombo) {
        Optional<ExemplarModel> exemplarO = exemplarRepository.findById(idTombo);
        if (exemplarO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exemplar não encontrado.");
        }

        ExemplarModel exemplar = exemplarO.get();
        LivroModel livro = exemplar.getLivro();
        livro.setQtdCadastro(livro.getQtdCadastro() - 1);

        livroRepository.save(livro);
        exemplarRepository.delete(exemplar);

        return ResponseEntity.status(HttpStatus.OK).body("Exemplar excluído com sucesso.");
    }
}
