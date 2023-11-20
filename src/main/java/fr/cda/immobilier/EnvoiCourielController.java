package fr.cda.immobilier;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sendinblue.ApiException;
import tool.Mail;

/**
 * Classe EnvoiCourielController
 * @author cda
 */
public class EnvoiCourielController {
    private TextArea annonces;
    @FXML
    private TextField email;
    public void setAnnonces(TextArea annonces) {
        this.annonces = annonces;
    }

    /**
     * Methode d'envoi d'email
     */
    @FXML
    private void envoyerEmail() throws ApiException {
        System.out.println(email.getText());
        Mail.sendEmail(email.getText(), "Mr", annonces.getText().replace("\r\n", "\n"));
        fermerFenetre();
    }
    /**
     * Methode pour fermer la fenetre
     */
    @FXML
    private void fermerFenetre() {
        // Je recupere le sate
        Stage stage = (Stage) email.getScene().getWindow();  // You can use any control that belongs to the current stage

        // Close the stage
        stage.close();
    }

}
