package fr.cda.immobilier;

import fr.cda.annonce.Annonce;
import fr.cda.annonce.AnnonceDao;
import fr.cda.annonce.DaoFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe TransmissionBddController
 * @author Julien Rolland
 */
public class TransmissionBddController {
    @FXML
    private Button button;
    AnnonceDao annonceDao;

    /**
     * Methode d'initialisation
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        this.annonceDao = daoFactory.getAnnonceDao();
    }

    /**
     * Transmet les annonces a la base de donnees
     * @throws SQLException
     */
    @FXML
    public void validerTrasmission() throws SQLException {
        // Je vide d'abord la table
        annonceDao.truncate();
        // J'ajoute ensuite les nouvelles annonces
        for (Annonce annonce: MyAppController.annonceList) {
            annonceDao.add(annonce);
        }
        // Et je ferme la fenetre
        fermerFenetre();
    }

    /**
     * Ferme la fenetre de transmission
     */
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) this.button.getScene().getWindow();
        stage.close();
    }
}
