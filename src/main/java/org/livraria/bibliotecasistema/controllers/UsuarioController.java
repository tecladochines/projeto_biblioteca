package org.livraria.bibliotecasistema.controllers;

import jakarta.validation.Valid;
import org.livraria.bibliotecasistema.dtos.UsuarioRecordDto;
import org.livraria.bibliotecasistema.models.UsuarioModel;
import org.livraria.bibliotecasistema.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController //Marca a classe como um controlador Rest: responsável por lidar com as requisições HTTP
@RequestMapping("/usuarios") //Mapeia a rota da classe como /usuarios
public class UsuarioController {

    final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping //Rota: POST /usuarios
    public ResponseEntity<UsuarioModel> saveUsuario(@RequestBody @Valid UsuarioRecordDto usuarioRecordDto) {
        var usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioRecordDto, usuarioModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuarioModel));
    }

    @GetMapping//Rota: GET /usuarios
    public ResponseEntity<List<UsuarioModel>> getAllUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }

    @PutMapping("/usuarios/{id}") //Rota: GET /usuarios
    ResponseEntity<Object> updateUsuario (@PathVariable(value="id") Long id,
                                          @RequestBody @Valid UsuarioRecordDto usuarioRecordDto){
        Optional<UsuarioModel> usuarioO = usuarioRepository.findByIdUsuario(id);
        if(usuarioO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        var usuarioModel = usuarioO.get();
        BeanUtils.copyProperties(usuarioRecordDto, usuarioModel);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuarioModel));
    }

    @DeleteMapping("/usuarios/{id}")
    ResponseEntity<Object> deleteUsuario(@PathVariable(value = "id") Long id){
        Optional<UsuarioModel> usuarioO = usuarioRepository.findByIdUsuario(id);
        if(usuarioO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        usuarioRepository.delete(usuarioO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso");
    }
}

