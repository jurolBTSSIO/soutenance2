package fr.cda.immobilier;

import fr.cda.annonce.*;
import fr.cda.exception.SaisieIncorrectException;
import fr.cda.immobilier.scraping.OuestFrance;
import fr.cda.immobilier.scraping.SeLoger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe MyAppController
 * @author cda
 */
public class MyAppController {
    private StringBuilder urlOF = new StringBuilder();
    private String urlSL = null;
    // Variable pour le numero de la ville
    private String villeNumOf = null;
    private String villeNumSeloger = null;
    // Variable pour l'id du type
    int idType = 0;
    // Variable pour l'id de la ville
    int idVille = 0;
    private String filePath;
    // Liste des annonces en static
    public static List<Annonce> annonceList;
    private ObservableList<String> optionsType =
            FXCollections.observableArrayList(
                    "maison",
                    "appartement",
                    "terrain",
                    "Box"
            );
    private ObservableList<String> optionsVille =
            FXCollections.observableArrayList(
                    "vannes",
                    "lorient",
                    "brest",
                    "quimper",
                    "st-brieuc",
                    "guingamp"
            );
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ComboBox<String> localisation = new ComboBox<>();
    @FXML
    private ComboBox<String> types = new ComboBox<>();
    @FXML
    private TextArea annonces;
    @FXML
    private TextField prixMini;
    @FXML
    private TextField prixMaxi;
    @FXML
    private TextField surfaceMin;
    @FXML
    private TextField surfaceMax;
    @FXML
    private CheckBox seLogerBox;
    @FXML
    private CheckBox ouestFranceBox;
    @FXML
    private Button recherche;
    @FXML
    private Button effacer;

    /**
     * Methode d'initialisation
     */
    @FXML
    public void initialize() {
        this.types.setItems(optionsType);
        this.localisation.setItems(optionsVille);
        //Liez l'activité du bouton de recherche à la sélection de la ComboBox
        this.recherche.disableProperty().bind(types.valueProperty().isNull()
                .or(localisation.valueProperty().isNull()
                        .or(seLogerBox.selectedProperty().not()
                                .and(ouestFranceBox.selectedProperty().not()))));
        annonceList = new ArrayList<>();
        this.annonces.setEditable(false);
    }

    /**
     * Methode que reinitialise le formulaire
     */
    @FXML
    private void effacerChamps() {
        prixMaxi.clear();
        prixMini.clear();
        surfaceMax.clear();
        surfaceMin.clear();
        types.getSelectionModel().clearSelection();
        localisation.getSelectionModel().clearSelection();
        seLogerBox.setSelected(false);
        ouestFranceBox.setSelected(false);
    }

    /**
     * Methode qui lance un script de scrapping et retourne un stringbuilder
     *
     * @return
     */
    @FXML
    public void handleRechercheButton() throws  IOException {
        annonceList = new ArrayList<>();
        setValues();
        // Reset progress bar
        progressBar.setProgress(0);
        // Si la checkbox OF est selectionnee
        if (ouestFranceBox.isSelected()) {
            Task<StringBuilder> taskOF = new Task<StringBuilder>() {
                @Override
                protected StringBuilder call() throws SaisieIncorrectException, IOException, InterruptedException {
                    OuestFrance ouestFrance = new OuestFrance(getUrlOF());
                    return ouestFrance.scrapSite();
                }
                @Override
                protected void succeeded() {
                    annonces.appendText(getValue().toString());
                }
                @Override
                protected void failed() {
                    System.out.println("erreur dans le thread");
                }
            };
            //Bind progress bar property to task progress
            progressBar.progressProperty().bind(taskOF.progressProperty());
            new Thread(taskOF).start();
        }
        // Si la checkbox SL est selectionnee
        if (seLogerBox.isSelected()) {
            Task<StringBuilder> taskSL = new Task<StringBuilder>() {
                @Override
                protected StringBuilder call() throws IOException, InterruptedException {
                    SeLoger seLoger = new SeLoger(getUrlSL());
                    return seLoger.scrapSite();
                }
                @Override
                protected void succeeded() {
                    annonces.appendText(getValue().toString());
                }
                @Override
                protected void failed() {
                    System.out.println("erreur dans le thread");
                }
            };
            new Thread(taskSL).start();
        }
    }

    public void setValues() {
        // J'affecte les types de biens
        switch (types.getValue()) {
            case "appartement":
                idType = 1;
                break;
            case "maison":
                idType = 2;
                break;
            case "terrain":
                idType = 3;
                break;
            case "box":
                idType = 4;
                break;
        }
        // Je fais correspondre les villes et leurs numeros
        switch (localisation.getValue()) {
            case "vannes":
                villeNumOf = "vannes-56-56000";
                villeNumSeloger = "560260";
                idVille = 1;
                break;
            case "lorient":
                villeNumOf = "lorient-56-56100";
                villeNumSeloger = "560121";
                idVille = 2;
                break;
            case "brest":
                villeNumOf = "brest-29-29200";
                villeNumSeloger = "290019";
                idVille = 3;
                break;
            case "quimper":
                villeNumOf = "quimper-29-29000";
                villeNumSeloger = "290232";
                idVille = 4;
                break;
            case "st-brieuc":
                villeNumOf = "st-brieuc-22-22000";
                villeNumSeloger = "220278";
                idVille = 5;
                break;
            case "guingamp":
                villeNumOf = "guingamp-22-22220";
                villeNumSeloger = "220070";
                idVille = 6;
                break;
        }
    }
    public String getUrlOF() {

        urlOF.append("https://www.ouestfrance-immo.com/acheter/");

        // J'ajoute le type de biens
        urlOF.append(this.types.getValue());

        // J'ajoute le separateur
        urlOF.append("/");

        // J'ajoute le ref de la ville choisie
        urlOF.append(villeNumOf);

        // J'ajoute le parametre prix
        urlOF.append("/?prix=");

        // J'ajoute le prix mini à l'url
        urlOF.append(prixMini.getText());

        // J'ajoute le separateur prix mini / prix maxi
        urlOF.append("_");

        // J'ajoute le prix maxi
        urlOF.append(prixMaxi.getText());

        // J'ajoute le &
        urlOF.append("&");

        // J'ajoute le parametre surface
        urlOF.append("surface=");

        // J'ajoute la surface mini
        urlOF.append(surfaceMin);

        // J'ajoute le separateur surface mini / surface maxi
        urlOF.append("_");

        // J'ajoute la surface maxi
        urlOF.append(surfaceMax);

        // Je retourne l'url pour ouestfrance
        return urlOF.toString();
    }
    public String getUrlSL() {
        // URL de Seloger.com
        return "https://www.seloger.com/list.htm?projects=2,5&types="+ idType +"&natures=1,2,4&places=[{%22inseeCodes%22:["+ villeNumSeloger +"]}]&price="+ prixMini.getText() +"/"+ prixMaxi.getText() +"&mandatorycommodities=0&enterprise=0&qsVersion=1.0&m=homepage_buy-redirection-search_results";
    }

    /**
     * Methode qui enregistre les annonces dans un fichier.txt
     */
    @FXML
    private void enregistrerDansUnFichier() {
        // J'initialise le filechooser
        FileChooser fileChooser = new FileChooser();

        // Configure le FileChooser
        fileChooser.setTitle("Enregistrer le fichier");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt"));

        // Affiche la boîte de dialogue Enregistrer
        File selectedFile = fileChooser.showSaveDialog(new Stage());

        // Si l'utilisateur a choisi un fichier
        if (selectedFile != null) {
            // J'ajoute le contenu du text area annonces
            try (FileWriter fileWriter = new FileWriter(selectedFile)) {
                fileWriter.write(annonces.getText());
                filePath = selectedFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Path local du fichier .txt
        String cheminDuFichier = "Annonce.txt";

        // J'ajoute le contenu du text area annonces
        try (FileWriter fileWriter = new FileWriter(new File(cheminDuFichier))) {
            fileWriter.write(annonces.getText());
            filePath = cheminDuFichier;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode qui ouvre une fenetre modale
     * @throws IOException
     */
    @FXML
    private void envoiCourielView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MyApp.class.getResource("envoi-couriel-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Envoi Couriel");
        stage.setScene(scene);
        EnvoiCourielController envoiCourielController = fxmlLoader.getController();
        envoiCourielController.setAnnonces(annonces);
        stage.show();
    }

    /**
     * Methode qui ouvre la vue de parametrage
     * de la base de données
     * @throws IOException
     */
    @FXML
    private void bddView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MyApp.class.getResource("bdd-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Paramètrage de la base de données");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Methode qui ouvre la fenetre
     * de transmission de données
     * @throws IOException
     */
    @FXML
    private void transmissionView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MyApp.class.getResource("transmissionBDD-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
        Stage stage = new Stage();
        stage.setTitle("Transmission des données");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * modeEmploi
     * @throws IOException
     */
    @FXML
    private void modeEmploi() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MyApp.class.getResource("modeEmploi-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 750);
        Stage stage = new Stage();
        stage.setTitle("Mode d'Emploi");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Ferme la fenetre
     */
    @FXML
    private void fermer() throws IOException{
        Stage stage = (Stage) annonces.getScene().getWindow();
        stage.close();
    }
}