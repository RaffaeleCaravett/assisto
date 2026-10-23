package com.example.user_service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public void sendAccessCode(String to, String nome, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Codice di accesso Assisto");
        String saluto = nome == null || nome.isBlank() ? "Ciao" : "Ciao " + nome;
        message.setText(saluto + ",\n\nil codice per entrare in Assisto è " + code
                + ".\nVale 10 minuti. Se non hai richiesto tu l'accesso, ignora questo messaggio.\n");
        mailSender.send(message);
    }
}
