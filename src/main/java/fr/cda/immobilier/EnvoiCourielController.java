package fr.cda.immobilier;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sendinblue.ApiException;
import tool.Mail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class EnvoiCourielController {
    private String filePath;
    private TextArea annonces;
    @FXML
    private TextField email;
    public void setAnnonces(TextArea annonces) {
        this.annonces = annonces;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
