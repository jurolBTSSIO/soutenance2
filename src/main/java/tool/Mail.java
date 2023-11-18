package tool;

import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe Mail
 * @author Julien Rolland
 */
public class Mail {
    private static final String API_KEY = "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";
    private static final String EMAIL_SENDER = "tot@greta-bretagne-sud.fr";
    private static final String NAME_SENDER = "toto";

    public static void sendEmail(String emailDest, String nomDest, String annonces) {

        // Configure l'API client SendinBlue
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Configure l'authentification API avec votre clé d'API SendinBlue
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(API_KEY);
        try {
            // Crée une instance de l'API d'envoi d'emails transactionnels
            TransactionalEmailsApi api = new TransactionalEmailsApi();

            // Configure l'expéditeur
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(EMAIL_SENDER);
            sender.setName(NAME_SENDER);

            // Configure le destinataire
            List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(emailDest);
            to.setName(nomDest);
            toList.add(to);

            // J'attache un document
            SendSmtpEmailAttachment attachment = new SendSmtpEmailAttachment();
            attachment.setName("Annonce.txt");
            byte[] encode = Files.readAllBytes(Paths.get("Annonce.txt"));
            attachment.setContent(encode);
            List<SendSmtpEmailAttachment> attachmentList = new ArrayList<SendSmtpEmailAttachment>();
            attachmentList.add(attachment);
            // Crée un objet SendSmtpEmail pour définir le contenu de l'email
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setAttachment(attachmentList);
            sendSmtpEmail.setHtmlContent("<html><body><p></p>" +
                    "<p>Cordialement,</p>" +
                    "<br>" +
                    "<p>Mr Le Banquier.</p></body></html>");
            sendSmtpEmail.setSubject("Annonces");

            // Envoie l'email
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            System.out.println("Email envoyé avec succès : " + response);

        } catch (Exception e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
    }
}

