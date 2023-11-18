package fr.cda.immobilier;

import annonce.DaoFactory;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BddController {
    @FXML
    private TextField nomHote;
    @FXML
    private TextField nomBDD;
    @FXML
    private TextField numPort;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    @FXML
    private void parametrerBDD() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoFactory.setUsername(login.getText());
        daoFactory.setUrl(nomHote.getText());
        daoFactory.setPassword(password.getText());
        Stage stage = (Stage) password.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) password.getScene().getWindow();
        stage.close();
    }
}
