package fr.cda.immobilier;

import fr.cda.annonce.AnnonceDao;
import fr.cda.annonce.DaoFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TransmissionBddControllerTest {

    @Test
    void validerTrasmission() throws SQLException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        AnnonceDao annonceDao = daoFactory.getAnnonceDao();
        assertNotNull(annonceDao.find(1));
    }
}