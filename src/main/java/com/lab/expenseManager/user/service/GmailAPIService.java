package com.lab.expenseManager.user.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Service
public class GmailAPIService {

	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

	void sendVerifyAccountEmail(String toEmail, String token) throws Exception {

		final GoogleCredentials googleCredentials = ServiceAccountCredentials.getApplicationDefault();

		final Gmail gmailService = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
				new HttpCredentialsAdapter(googleCredentials)).setApplicationName("Gmail API Java").build();
		try {
			String verificationLink = "https://expense-manager-mocha.vercel.app/VerifyAccount/accountVerify?token="
					+ token;

			String htmlContent = "<h1>Olá!</h1>" + "<p>Por favor, clique no link abaixo para verificar sua conta:</p>"
					+ "<a href='" + verificationLink + "'>Clique aqui</a>";

			Message message = this.createEmail(toEmail, "Quase lá...", htmlContent);
			gmailService.users().messages().send("me", message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Message createEmail(String to, String subject, String bodyText) throws IOException {
		String from = "kaikymoura972@gmail.com";
		String messageText = "From: " + from + "\n" + "To: " + to + "\n" + "Subject: " + subject + "\n"
				+ "MIME-Version: 1.0\n" + "Content-Type: text/html; charset=UTF-8\n\n" + bodyText;

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		buffer.write(messageText.getBytes(StandardCharsets.UTF_8));
		byte[] bytes = buffer.toByteArray();

		String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	void sendResetPasswordEmail(String toEmail, String token) throws IOException, GeneralSecurityException {
		final GoogleCredentials googleCredentials = ServiceAccountCredentials.getApplicationDefault();

		final Gmail gmailService = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
				new HttpCredentialsAdapter(googleCredentials)).setApplicationName("Gmail API Java").build();
		try {
			String verificationLink = "https://expense-manager-mocha.vercel.app/VerifyAccount/accountVerify?token="
					+ token;

			String htmlContent = "<h1>Olá!</h1>"
					+ "<p>Recebemos uma solicitação para redefinir a senha da sua conta. Se você fez essa solicitação, siga as instruções abaixo para criar uma nova senha:</p>"
					+ "<p>Clique no link abaixo para redefinir sua senha:</p>" + "<a href='" + verificationLink
					+ "style='" + "display: inline-block; " + "padding: 10px 20px; " + "font-size: 16px; "
					+ "color: white; " + "background-color: #4CAF50; " + "text-decoration: none; "
					+ "border-radius: 5px; " + "margin: 10px 0;'>" + "Redefinir Senha</a>"
					+ "<p>Se você não fez essa solicitação, pode ignorar este e-mail.</p>";

			Message message = this.createEmail(toEmail, "Redifinição de senha", htmlContent);
			gmailService.users().messages().send("me", message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}