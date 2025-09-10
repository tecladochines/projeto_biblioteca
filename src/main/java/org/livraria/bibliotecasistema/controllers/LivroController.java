package org.livraria.bibliotecasistema.controllers;

import jakarta.validation.Valid;
import org.livraria.bibliotecasistema.dtos.LivroRecordDto;
import org.livraria.bibliotecasistema.dtos.response.LivroResponseDto;
import org.livraria.bibliotecasistema.models.LivroModel;
import org.livraria.bibliotecasistema.models.UsuarioModel;
import org.livraria.bibliotecasistema.repository.LivroRepository;
import org.livraria.bibliotecasistema.repository.ExemplarRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.livraria.bibliotecasistema.models.ExemplarModel;

import java.util.List;
import java.util.Optional;

@RestController
public class LivroController {

    final LivroRepository livroRepository;
    final ExemplarRepository exemplarRepository;

    public LivroController(LivroRepository livroRepository, ExemplarRepository exemplarRepository) {
        this.livroRepository = livroRepository;
        this.exemplarRepository = exemplarRepository;
    }

    @PostMapping("/livros")
    public ResponseEntity<Object> saveLivro(@RequestBody @Valid LivroRecordDto livroRecordDto) {
        Optional<LivroModel> livroExistente = livroRepository.findByTituloAndAutor(
                livroRecordDto.titulo(),
                livroRecordDto.autor()
        );

        LivroModel livroModel;
        if (livroExistente.isPresent()) {
            livroModel = livroExistente.get();
            livroModel.setQtdCadastro(livroModel.getQtdCadastro() + livroRecordDto.qtdCadastro());
        } else {
            livroModel = new LivroModel();
            BeanUtils.copyProperties(livroRecordDto, livroModel);
            livroModel.setQtdCadastro(livroRecordDto.qtdCadastro());
        }

        LivroModel livroSalvo = livroRepository.save(livroModel);

        // Criar os exemplares
        for (int i = 0; i < livroRecordDto.qtdCadastro(); i++) {
            ExemplarModel exemplar = new ExemplarModel();
            exemplar.setLivro(livroSalvo);
            exemplar.setStatus(ExemplarModel.StatusExemplar.DISPONIVEL);
            exemplarRepository.save(exemplar);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    @GetMapping("/livros")
    public ResponseEntity<List<LivroResponseDto>> getAllLivros() {
        return ResponseEntity.status(HttpStatus.OK).body(livroRepository.findAll().stream().map(livroModel -> this.constroiResponseDto(livroModel)).toList());
    }

    @GetMapping("/livros/{id}")
    public ResponseEntity<Object> getOneLivro(@PathVariable(value = "id") Long id) {
        Optional<LivroModel> livroO = livroRepository.findByIdLivro(id);
        if (livroO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroO.get());
    }

    @GetMapping("/livros/titulo/{titulo}")
    public ResponseEntity<Object> getLivroByTitulo(@PathVariable(value = "titulo") String titulo){
        Optional<LivroModel> livroO = livroRepository.findByTitulo(titulo);
        if (livroO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroO.get());
    }

    @GetMapping("/livros/autor/{autor}")
    public ResponseEntity<Object> getLivroByAutor(@PathVariable(value = "autor") String autor){
        List<LivroModel> livroO = livroRepository.findByAutorIgnoreCase(autor);
        if (livroO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum livro encontrado para o autor: " + autor);
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroO);
    }

    @GetMapping("/livros/genero/{genero}")
    public ResponseEntity<Object> getLivroByGenero(@PathVariable(value = "genero") String genero){
        List<LivroModel> livroO = livroRepository.findByGeneroIgnoreCase(genero);
        if (livroO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum livro encontrado para o gênero: " + genero);
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroO);
    }

    @GetMapping("/livros/anoPublicacao/{anoPublicacao}")
    public ResponseEntity<Object> getLivroByAnoPublicacao(@PathVariable(value = "anoPublicacao") int anoPublicacao){
        List<LivroModel> livroO = livroRepository.findByAnoPublicacao(anoPublicacao);
        if (livroO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum livro encontrado para ano de publicação: " + anoPublicacao);
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroO);
    }

    @GetMapping("/livros/setor/{setor}")
    public ResponseEntity<Object> getLivroBySetor(@PathVariable(value = "setor") String setor){
        List<LivroModel> livroO = livroRepository.findBySetorIgnoreCase(setor);
        if (livroO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum livro encontrado para este setor: " + setor);
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroO);
    }

    @PutMapping("/livros/{id}")
    ResponseEntity<Object> updateLivro (@PathVariable(value="id") Long id,
                                          @RequestBody @Valid LivroRecordDto livroRecordDto){
        Optional<LivroModel> livroO = livroRepository.findByIdLivro(id);
        if(livroO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
        }
        var livroModel = livroO.get();
        BeanUtils.copyProperties(livroRecordDto, livroModel);
        return ResponseEntity.status(HttpStatus.OK).body(livroRepository.save(livroModel));
    }

    @DeleteMapping("/livros/{id}")
    ResponseEntity<Object> deleteLivro(@PathVariable(value = "id") Long id){
        Optional<LivroModel> livroO = livroRepository.findByIdLivro(id);
        if(livroO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
        }
        livroRepository.delete(livroO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Livro deletado com sucesso");
    }

    @DeleteMapping("/livros/deleteAll")
    public ResponseEntity<Void> deleteAllLivros() {
        livroRepository.deleteAll(); // Exclui todos os registros
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Retorna 204 (sem conteúdo)
    }

    private LivroResponseDto constroiResponseDto(LivroModel model){
        return new LivroResponseDto(model.getIdLivro(), model.getTitulo(), model.getAutor(), model.getGenero(),  model.getAnoPublicacao(), model.getSetor(), model.getQtdCadastro());
    }
}
