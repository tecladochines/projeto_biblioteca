package org.livraria.bibliotecasistema.jobs;

import org.livraria.bibliotecasistema.models.UsuarioModel;
import org.livraria.bibliotecasistema.repository.UsuarioRepository;
import org.livraria.bibliotecasistema.services.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnviaEmailParaUsuariosComMultaJob {
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;


    public EnviaEmailParaUsuariosComMultaJob(EmailService emailService, UsuarioRepository usuarioRepository) {
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void executa() {
        String assunto = "MULTA DE ATRASO DE EMPRÉSTIMO NA BIBLIOTECA";
        List<UsuarioModel> usuarios = this.usuarioRepository.findEmailsDeUsuariosComMultaEmAndamento();

        if (!usuarios.isEmpty()) {
            usuarios.forEach(usuario -> {
                String mensagem = String.format("""
                    Caro %s, nosso sistema consta uma multa em seu nome.
                    Por favor entre em contato conosco para solucionar essa pendencia.
                    """, usuario.getNome());
                try {
                    this.emailService.envia(usuario.getEmail(), assunto, mensagem);

                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println("ERRO AO ENVIAR EMAIL PARA " + usuario.getNome());
                    System.out.println(e.getMessage());
                }
            });
        }
    }
}
