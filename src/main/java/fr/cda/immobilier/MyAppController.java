package fr.cda.immobilier;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tool.ScrappyBot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Classe MyAppController
 * @author cda
 */
public class MyAppController {
    String filePath;
    private ObservableList<String> optionsType =
            FXCollections.observableArrayList(
                    "maison",
                    "appartement",
                    "box"
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
        types.setItems(optionsType);
        localisation.setItems(optionsVille);
        //Liez l'activité du bouton de recherche à la sélection de la ComboBox
        recherche.disableProperty().bind(types.valueProperty().isNull());
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
    }

    /**
     * Methode qui lance un script de scrapping et retourne un stringbuilder
     * @return
     */
    @FXML
    public void handleRechercheButton() {
        Task<StringBuilder> task = new Task<>() {
            @Override
            protected StringBuilder call() {
                return scrappyBot();
            }

            @Override
            protected void succeeded() {
                annonces.setText(getValue().toString());
            }

            @Override
            protected void failed() {
                System.out.println("erreur");
            }
        };
        new Thread(task).start();
    }

    /**
     * Methode de scrapping
     * @return
     */
    public StringBuilder scrappyBot() {
        // Variable pour le numero de la ville
        String villeNum = null;
        // Je cree un stringbuilder
        StringBuilder retourRecherche = new StringBuilder();

        // Je fais correspondre les villes et leurs numeros
        switch (localisation.getValue()) {
            case "vannes":
                villeNum = "vannes-56-56000";
                break;
            case "lorient":
                villeNum = "lorient-56-56100";
                break;
            case "brest":
                villeNum = "brest-29-29200";
                break;
            case "quimper":
                villeNum = "quimper-29-29000";
                break;
            case "st-brieuc":
                villeNum = "st-brieuc-22-22000";
                break;
            case "guingamp":
                villeNum = "guingamp-22-22220";
                break;
        }
        // Je recupere la page web par son url
        try {
            HtmlPage page = ScrappyBot.getWebClient().getPage(ScrappyBot.urlBuilder(
                    types.getValue(),
                    villeNum,
                    prixMini.getText(),
                    prixMaxi.getText(),
                    surfaceMin.getText(),
                    surfaceMax.getText())
            );
            String[] balises = {
                    "//div[@class='annBlocDesc']",
                    "//span[@class='annTitre']",
                    "//span[@class='annTexte hidden-phone']",
                    "//span[@class='annPrix']",
                    "//img[@class='annPhoto']"
            };
            // Je recupere tous les titres d'annonces
            List<HtmlElement> divs = page.getByXPath(balises[0]);

            // Pour toutes les annonces je recupere le prix et la description
            for (HtmlElement div : divs) {
                HtmlElement titre = (HtmlElement) div.getFirstByXPath(balises[1]);
                HtmlElement description = (HtmlElement) div.getFirstByXPath(balises[2]);
                HtmlElement prix = (HtmlElement) div.getFirstByXPath(balises[3]);
                HtmlElement parent = (HtmlElement) div.getParentNode();
                HtmlElement image = (HtmlElement) parent.getFirstByXPath("//div[@class='img annPhoto']");
                System.out.println(parent.asXml());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        // Je retourne les annonces
        return retourRecherche;
    }

    /**
     * Methode qui enregistre les annonces dans un fichier.txt
     */
    @FXML
    private void enregistrerDansUnFichier() {

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
     * Ferme la fenetre
     */
    @FXML
    private void fermer() {
        Stage stage = (Stage) annonces.getScene().getWindow();
        stage.close();
    }
}