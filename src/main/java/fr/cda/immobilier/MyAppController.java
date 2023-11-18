package fr.cda.immobilier;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
import java.util.List;

/**
 * Classe MyAppController
 * @author cda
 */
public class MyAppController {
    String filePath;
    private ObservableList<String> options =
            FXCollections.observableArrayList(
                    "Maison",
                    "Appartement",
                    "Box"
            );
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ComboBox<String> types = new ComboBox<>();
    @FXML
    private TextArea annonces;
    @FXML
    private TextField localisation;
    @FXML
    private TextField budgetMin;
    @FXML
    private TextField budgetMax;
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
        types.setItems(options);
        //Liez l'activité du bouton de recherche à la sélection de la ComboBox
        recherche.disableProperty().bind(types.valueProperty().isNull());

    }

    /**
     * Methode que reinitialise le formulaire
     */
    @FXML
    private void effacerChamps() {
        localisation.clear();
        budgetMax.clear();
        budgetMin.clear();
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

        // Je cree un stringbuilder
        StringBuilder retourRecherche = new StringBuilder();

        // Mettre ici le code pour lancer la recherche en fonction des criteres
        String url = "https://www.ouestfrance-immo.com/acheter/maison/quimper-29-29000/?prix=0_300000";

        // Creation d'un webclient
        WebClient client = new WebClient(BrowserVersion.FIREFOX);
        client.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        // Parametrage des options
        client.getOptions().setUseInsecureSSL(true);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setTimeout(5000); // Set a timeout of 5 seconds
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setThrowExceptionOnScriptError(false);

        try {
            // Je recupere la page web par son url
            HtmlPage page = client.getPage(url);

            // Je recupere tous les titres d'annonces
            List<HtmlElement> titres = page.getByXPath("//span[@class='annTitre']");

            // Pour toutes les annonces je recupere le prix et la description
            for (HtmlElement titre : titres) {
                String annonceText = titre.asNormalizedText().trim();
                System.out.println("Titre: " + annonceText);
                retourRecherche.append("Titre: " + annonceText).append("\n");

                // Trouver le parent div de l'annonce
                HtmlElement parentDiv = (HtmlElement) titre.getParentNode();

                // Trouver le parent au dessus de parentDiv
                HtmlElement parentDiv2 = (HtmlElement) parentDiv.getParentNode();

                // Acceder au prix à l'interieur du parent div
                HtmlElement prixElement = parentDiv.getFirstByXPath(".//span[@class='annPrix']");
                String price = prixElement != null ? prixElement.asNormalizedText().trim() : "N/A";
                System.out.println("Prix: " + price);
                retourRecherche.append("Prix: " + price).append("\n");

                // Accéder à la description à l'intérieur du parent div
                HtmlElement descriptionElement = parentDiv2.getFirstByXPath(".//span[@class='annTexte hidden-phone']");
                String description = descriptionElement != null ? descriptionElement.asNormalizedText().trim() : "N/A";
                System.out.println("Description: " + description);
                retourRecherche.append("Description: " + description).append("\n");
                System.out.println();
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
        String cheminDuFichier = "Annonce.txt";

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
        envoiCourielController.setFilePath(filePath);
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