package fr.cda.immobilier;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tool.Mail;

public class EnvoiCourielController {
    private TextArea annonces;
    @FXML
    private TextField email;
    public void setAnnonces(TextArea annonces) {
        this.annonces = annonces;
    }
    @FXML
    private void envoyerEmail() {
        System.out.println(email.getText());
        Mail.sendEmail(email.getText(), "Mr", annonces.getText().replace("\r\n", "\n"));
    }
    @FXML
    private void fermerFenetre() {
        // Get the reference to the current stage
        Stage stage = (Stage) email.getScene().getWindow();  // You can use any control that belongs to the current stage

        // Close the stage
        stage.close();
    }

}
