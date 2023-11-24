package fr.cda.immobilier;

import annonce.Annonce;
import annonce.AnnonceDao;
import annonce.DaoFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransmissionBddController {
    private List<Annonce> annonceList;
    @FXML
    private Button button;
    @FXML
    public void initialize() throws SQLException {

    }
    @FXML
    private void validerTrasmission() throws SQLException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        AnnonceDao annonceDao = daoFactory.getAnnonceDao();

        for (Annonce annonce: annonceList) {
            annonceDao.add(annonce);
        }
    }
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    public void setAnnonceList(List<Annonce> annonceList) {
        this.annonceList = annonceList;
    }
}
