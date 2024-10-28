package com.lab.expenseManager.user.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

@Service
public class GmailAPIService {

	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final Collection<String> SCOPES = Collections.singleton(GmailScopes.GMAIL_SEND);

	void sendVerifyAccountEmail(String toEmail, String token) throws Exception {

		final GoogleCredentials googleCredentials = ServiceAccountCredentials.getApplicationDefault()
				.createScoped(SCOPES);

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

	private Message createEmail(String to, String subject, String bodyText)
			throws IOException, AddressException, MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress("kaikymoura972@gmail.com"));
		email.addRecipient(RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setContent(bodyText, "text/html; charset=UTF-8");

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		byte[] rawMessageBytes = buffer.toByteArray();
		String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

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