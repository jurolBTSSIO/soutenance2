package fr.cda.immobilier;

import annonce.Annonce;
import annonce.AnnonceDao;
import annonce.DaoFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TransmissionBddController {
    @FXML
    private Button button;
    @FXML
    private void validerTrasmission() throws SQLException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        AnnonceDao annonceDao = daoFactory.getAnnonceDao();
        Annonce annonce = new Annonce();
        annonce.setTitre("titre1");
        annonce.setDescription("description");
        annonce.setPrix(50000);
        annonce.setSurface(75);
        annonce.setId_type(3);
        annonce.setId_ville(1);
        annonceDao.add(annonce);
    }
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
