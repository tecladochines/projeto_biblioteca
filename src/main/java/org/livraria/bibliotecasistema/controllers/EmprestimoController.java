package org.livraria.bibliotecasistema.controllers;

import jakarta.validation.Valid;
import org.livraria.bibliotecasistema.dtos.EmprestimoRecordDto;
import org.livraria.bibliotecasistema.dtos.response.EmprestimoResponseDto;
import org.livraria.bibliotecasistema.dtos.response.ListEmprestimosResponseDto;
import org.livraria.bibliotecasistema.models.ConfiguracoesModel;
import org.livraria.bibliotecasistema.models.EmprestimoModel;
import org.livraria.bibliotecasistema.models.EmprestimoModel.StatusEmprestimo;
import org.livraria.bibliotecasistema.models.ExemplarModel;
import org.livraria.bibliotecasistema.models.ExemplarModel.StatusExemplar;
import org.livraria.bibliotecasistema.models.UsuarioModel;
import org.livraria.bibliotecasistema.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    final EmprestimoRepository emprestimoRepository;
    final ExemplarRepository exemplarRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final ConfiguracoesRepository configuracoesRepository;

    public EmprestimoController(EmprestimoRepository emprestimoRepository, ExemplarRepository exemplarRepository, UsuarioRepository usuarioRepository, LivroRepository livroRepository, ConfiguracoesRepository configuracoesRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.exemplarRepository = exemplarRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.configuracoesRepository = configuracoesRepository;
    }

    @GetMapping
    public ResponseEntity<List<ListEmprestimosResponseDto>> listEmprestimos() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(emprestimoRepository.findAll().stream()
                        .map(it -> new ListEmprestimosResponseDto(
                                it.getUsuario().getNome(),
                                it.getUsuario().getCpf(),
                                it.getExemplar().getLivro().getTitulo(),
                                it.getExemplar().getIdTombo(),
                                it.getDataEmprestimo(),
                                it.getDataEmprestimo(),
                                it.getStatus()
                        ))
                        .toList());
    }
    // Realiza o empréstimo, altera o status do exemplar e diminui a quantidade disponível do livro
    @PostMapping
    public ResponseEntity<Object> saveEmprestimo(@RequestBody @Valid EmprestimoRecordDto emprestimoRecordDto) {
        // Cria o modelo de empréstimo e copia os dados do DTO para o modelo
        var emprestimoModel = new EmprestimoModel();
        BeanUtils.copyProperties(emprestimoRecordDto, emprestimoModel);

        // Verifica se existe um exemplar disponível
        Optional<ExemplarModel> exemplarDisponivel = exemplarRepository.findByIdTombo(emprestimoRecordDto.tomboId());

        // Verifica se existe um usuario disponivel
        Optional<UsuarioModel> usuarioDisponivel = usuarioRepository.findByIdUsuario(emprestimoRecordDto.usuarioId());

        if (exemplarDisponivel.isEmpty() || exemplarDisponivel.get().getStatus() != StatusExemplar.DISPONIVEL) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exemplar não disponível para empréstimo.");
        }

        if (usuarioDisponivel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Usuário não encontrado.");
        }

        //Atribui a data do emprestimo como o dia corrente
        emprestimoModel.setDataEmprestimo(LocalDateTime.now());
        // Atribui a data de devolução para 8 dias após a data atual
        emprestimoModel.setDataDevolucao(LocalDateTime.now().plusDays(buscaPeriodoEmprestimo()));
        // Atribui o id do exemplar
        emprestimoModel.setExemplar(exemplarDisponivel.get());
        // Atribui o id do usuário
        emprestimoModel.setUsuario(usuarioDisponivel.get());

        // Salva o empréstimo primeiro
        emprestimoModel = emprestimoRepository.save(emprestimoModel);

        // Marca o exemplar como emprestado
        ExemplarModel exemplar = exemplarDisponivel.get();
        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        exemplarRepository.save(exemplar);

        // Atualiza a quantidade de exemplares cadastrados no livro
        var livro = exemplar.getLivro();
        livro.setQtdCadastro(livro.getQtdCadastro() - 1);
        livroRepository.save(livro);
        // Atualiza o livro no repositório
        // Não precisa salvar o livro novamente, pois ele já está gerenciado pela JPA

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponseDto(emprestimoModel));
    }

    private Integer buscaPeriodoEmprestimo(){
        String chave = "periodoEmprestimo";
        Integer valorPadrao = 2;
        Optional<ConfiguracoesModel> configuracao = this.configuracoesRepository.findByChave(chave);
        return configuracao.map(configuracoesModel -> Integer.parseInt(configuracoesModel.getValor())).orElse(valorPadrao);
    }

    // Realiza a devolução do livro, muda o status do exemplar e incrementa a quantidade de exemplares no livro
    @PutMapping("/devolucao/{id}")
    public ResponseEntity<Object> devolveEmprestimo(@PathVariable(value = "id") Long idEmprestimo) {
        Optional<EmprestimoModel> emprestimoO = emprestimoRepository.findByIdEmprestimo(idEmprestimo);
        if (emprestimoO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado.");
        }

        EmprestimoModel emprestimo = emprestimoO.get();
        ExemplarModel exemplar = emprestimo.getExemplar(); // Supondo que cada empréstimo tenha apenas um exemplar associado

        // Verifica se o exemplar está emprestado
        if (exemplar.getStatus() != StatusExemplar.EMPRESTADO) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este exemplar não está emprestado.");
        }

        // Atualiza o status do exemplar para DISPONIVEL
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        exemplarRepository.save(exemplar);

        // Atualiza a quantidade de exemplares cadastrados no livro
        var livro = exemplar.getLivro();
        livro.setQtdCadastro(livro.getQtdCadastro() + 1);

        // Finaliza o emprestimo
        emprestimo.setStatus(StatusEmprestimo.FINALIZADO);
        emprestimoRepository.save(emprestimo);

        // Salva o livro
        // Não precisa salvar o livro novamente, pois ele já está gerenciado pela JPA

        // Atualiza o status do exemplar para disponivel
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        exemplarRepository.save(exemplar);

        return ResponseEntity.status(HttpStatus.OK)
                .body(buildResponseDto(emprestimo));
    }

    // Realiza a extensão do empréstimo, aplicando mais 8 dias na data de devolução
    @PutMapping("/renovar/{id}")
    public ResponseEntity<Object> estendeEmprestimo(@PathVariable(value = "id") Long idEmprestimo) {
        Optional<EmprestimoModel> emprestimoO = emprestimoRepository.findByIdEmprestimo(idEmprestimo);
        if (emprestimoO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado.");
        }

        EmprestimoModel emprestimoModel = emprestimoO.get();
        emprestimoModel.setDataDevolucao(LocalDateTime.now().plusDays(8));

        emprestimoRepository.save(emprestimoModel);
        // Salva o empréstimo com a nova data de devolução
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildResponseDto(emprestimoModel));
    }

    private EmprestimoResponseDto buildResponseDto(EmprestimoModel model) {
        return new EmprestimoResponseDto(
                model.getIdEmprestimo(),
                model.getDataEmprestimo(),
                model.getDataDevolucao(),
                model.getUsuario().getIdUsuario(),
                model.getExemplar().getIdTombo(),
                model.getStatus());
    }
}
