package com.lab.expenseManager.user.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {

	void sendVerifyAccountEmail(String toEmail, String token) throws IOException {
		String verificationLink = "https://expense-manager-mocha.vercel.app/VerifyAccount/accountVerify?token=" + token;
		Email from = new Email("kaikymoura972@gmail.com");
		String htmlContent = "<h1>Olá!</h1>" + "<p>Por favor, clique no link abaixo para verificar sua conta:</p>"
				+ "<a href='" + verificationLink + "'>Clique aqui</a>";
		Email to = new Email(toEmail);
		Content content = new Content("text/html", htmlContent);

		String sendGridApiKey = System.getenv("SENDGRID_API_KEY");

		// Verifica se a chave foi carregada corretamente
		if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
			throw new IllegalArgumentException("SENDGRID_API_KEY não encontrada ou está vazia.");
		} else {
			System.out.println("SENDGRID_API_KEY carregada com sucesso.");
		}

		SendGrid sendGrid = new SendGrid(sendGridApiKey);
		Mail mail = new Mail(from, "Quase lá...", to, content);

		try {
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.addHeader("Content-Type", "application/json");
			request.setBody(mail.build());
			sendGrid.api(request);
		} catch (IOException ex) {
			throw ex;
		}
	}

	void sendResetPasswordEmail(String toEmail, String token) throws IOException {
		String verificationLink = "http://localhost:3000/ChangePassword/changePassword?token=" + token;
		Email from = new Email("kaikymoura972@gmail.com");
		String htmlContent = "<h1>Olá!</h1>"
				+ "<p>Recebemos uma solicitação para redefinir a senha da sua conta. Se você fez essa solicitação, siga as instruções abaixo para criar uma nova senha:</p>"
				+ "<p>Clique no link abaixo para redefinir sua senha:</p>" + "<a href='" + verificationLink + "style='"
				+ "display: inline-block; " + "padding: 10px 20px; " + "font-size: 16px; " + "color: white; "
				+ "background-color: #4CAF50; " + "text-decoration: none; " + "border-radius: 5px; "
				+ "margin: 10px 0;'>" + "Redefinir Senha</a>"
				+ "<p>Se você não fez essa solicitação, pode ignorar este e-mail.</p>";
		Email to = new Email(toEmail);
		Content content = new Content("text/html", htmlContent);

		String sendGridApiKey = System.getenv("SENDGRID_API_KEY");

		// Verifica se a chave foi carregada corretamente
		if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
			throw new IllegalArgumentException("SENDGRID_API_KEY não encontrada ou está vazia.");
		} else {
			System.out.println("SENDGRID_API_KEY carregada com sucesso.");
		}

		SendGrid sendGrid = new SendGrid(sendGridApiKey);
		Mail mail = new Mail(from, "Redifinição de senha", to, content);

		try {
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.addHeader("Content-Type", "application/json");
			request.setBody(mail.build());
			sendGrid.api(request);
		} catch (IOException ex) {
			throw ex;
		}
	}

	public void welcomeEmail(String toEmail) {
		Email from = new Email("kaikymoura972@gmail.com");

		String htmlContent = "Olá! :):),\n" + "\n"
				+ "Seja muito bem-vindo(a) à nossa comunidade! Estamos muito felizes em tê-lo(a) conosco.\n" + "\n"
				+ "Somos uma equipe dedicada a oferecer a melhor experiência possível aos nossos usuários, e estamos ansiosos para compartilhar tudo o que temos a oferecer com você.\n"
				+ "\n"
				+ "Sinta-se à vontade para explorar nosso site, descobrir nossos produtos incríveis e aproveitar as vantagens exclusivas de ser parte da nossa comunidade.\n"
				+ "\n"
				+ "Se precisar de qualquer ajuda ou tiver alguma dúvida, não hesite em entrar em contato conosco. Estamos sempre aqui para ajudar!\n"
				+ "\n"
				+ "Mais uma vez, obrigado(a) por se juntar a nós. Esperamos que você tenha uma experiência incrível em nossa plataforma.\n"
				+ "\n" + "Atenciosamente,\n" + "O desenvolvedor que só queria deixar uma mensagem bonita";

		Email to = new Email(toEmail);
		Content content = new Content("text/html", htmlContent);

		String sendGridApiKey = System.getenv("SENDGRID_API_KEY");

		// Verifica se a chave foi carregada corretamente
		if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
			throw new IllegalArgumentException("SENDGRID_API_KEY não encontrada ou está vazia.");
		} else {
			System.out.println("SENDGRID_API_KEY carregada com sucesso.");
		}

		SendGrid sendGrid = new SendGrid(sendGridApiKey);

		Mail mail = new Mail(from, "Quase lá...", to, content);
		try {
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.addHeader("Content-Type", "application/json");
			request.setBody(mail.build());
			sendGrid.api(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}