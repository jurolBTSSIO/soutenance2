package fr.cda.tool;

import sendinblue.ApiClient;
import sendinblue.ApiResponse;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;
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

    /**
     * @param emailDest
     * @param nomDest
     * @param annonces
     */
    public static String sendEmail(String emailDest, String nomDest, String annonces) {
        // Je declare une variable de retour
        String retour = null;
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
            sendSmtpEmail.setHtmlContent("<html><body><p>      Bonjour Mr, Mme</p>" +
                    "<p>Vous trouverez ci-joint le fichier avec les annonces</p>" +
                    "<br>" +
                    "<p>Cordialement,</p>" +
                    "<br>" +
                    "<p>Mr L'agent immobilier.</p></body></html>");
            sendSmtpEmail.setSubject("Scrapping Immobilier");

            // Envoie l'email
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            retour = response.getMessageId();
            System.out.println(response.toString());

        } catch (Exception e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
        return retour;
    }
}

