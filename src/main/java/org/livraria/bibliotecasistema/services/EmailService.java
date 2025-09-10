package org.livraria.bibliotecasistema.services;

public interface EmailService {
    void envia(String destinario, String assunto, String conteudo);
}
