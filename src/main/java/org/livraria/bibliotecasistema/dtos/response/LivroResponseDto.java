package org.livraria.bibliotecasistema.dtos.response;

public record LivroResponseDto( Long idLivro,  String titulo,    String autor,
                                String genero, int anoPublicacao,
                                String setor,  int qtdCadastro){ }
