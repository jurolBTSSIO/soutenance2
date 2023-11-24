package fr.cda.immobilier;

import annonce.DaoFactory;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Classe controller de la vue de paramètre
 * de la base de données
 * @author cda
 */
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

    /**
     * Methode qui recupere les valeurs du formulaire
     */
    @FXML
    private void parametrerBDD() {
         //J'instancie un stringbuilder
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://" + nomHote.getText() + ":" + numPort.getText() + "/" + nomBDD.getText());
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoFactory.setUsername(login.getText());
        daoFactory.setUrl(url.toString());
        daoFactory.setPassword(password.getText());
        Stage stage = (Stage) password.getScene().getWindow();
        stage.close();
    }

    /**
     * Methode qui ferme la fenetre
     */
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) password.getScene().getWindow();
        stage.close();
    }
}
