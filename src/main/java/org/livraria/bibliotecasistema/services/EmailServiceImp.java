package org.livraria.bibliotecasistema.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailService{

    private JavaMailSender javaMailSender;

    EmailServiceImp(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void envia(String destinario, String assunto, String conteudo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinario);
        message.setSubject(assunto);
        message.setText(conteudo);

        this.javaMailSender.send(message);
    }
}
