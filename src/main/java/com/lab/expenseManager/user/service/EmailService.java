/*
 * package com.lab.expenseManager.user.service;
 * 
 * import org.springframework.mail.SimpleMailMessage; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.stereotype.Component; import
 * org.springframework.stereotype.Service;
 * 
 * import jakarta.mail.MessagingException;
 * 
 * @Service
 * 
 * @Component public class EmailService {
 * 
 * private JavaMailSender javaMailSender;
 * 
 * public EmailService(JavaMailSender javaMailSender) { this.javaMailSender =
 * javaMailSender; }
 * 
 * public void verifyAccountEmail(String destinatario) throws MessagingException
 * { SimpleMailMessage mensagem = new SimpleMailMessage(); String htmlContent =
 * "<h1>Olá!</h1>" +
 * "<p>Por favor, clique no link abaixo para verificar sua conta:</p>" +
 * "<a href='https://expense-manager-mocha.vercel.app/VerifyAccount/accountVerified'>Clique aqui</a>"
 * ; mensagem.setTo(destinatario); mensagem.setSubject("Quase lá");
 * mensagem.setText(htmlContent); javaMailSender.send(mensagem); }
 * 
 * public void sendEmail(String destinatario) { try { SimpleMailMessage mensagem
 * = new SimpleMailMessage(); mensagem.setTo(destinatario);
 * mensagem.setSubject("Bem vindo ao expense manager");
 * mensagem.setText("Olá! :):),\n" + "\n" +
 * "Seja muito bem-vindo(a) à nossa comunidade! Estamos muito felizes em tê-lo(a) conosco.\n"
 * + "\n" +
 * "Somos uma equipe dedicada a oferecer a melhor experiência possível aos nossos usuários, e estamos ansiosos para compartilhar tudo o que temos a oferecer com você.\n"
 * + "\n" +
 * "Sinta-se à vontade para explorar nosso site, descobrir nossos produtos incríveis e aproveitar as vantagens exclusivas de ser parte da nossa comunidade.\n"
 * + "\n" +
 * "Se precisar de qualquer ajuda ou tiver alguma dúvida, não hesite em entrar em contato conosco. Estamos sempre aqui para ajudar!\n"
 * + "\n" +
 * "Mais uma vez, obrigado(a) por se juntar a nós. Esperamos que você tenha uma experiência incrível em nossa plataforma.\n"
 * + "\n" + "Atenciosamente,\n" +
 * "O desenvolvedor que só queria deixar uma mensagem bonita");
 * javaMailSender.send(mensagem); } catch (Exception e) { e.printStackTrace(); }
 * } }
 */