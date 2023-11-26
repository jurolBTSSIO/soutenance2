package fr.cda.immobilier;


import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import fr.cda.annonce.Annonce;
import fr.cda.annonce.AnnonceDao;
import fr.cda.annonce.DaoFactory;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import fr.cda.tool.ScrappyBot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe MyAppController
 * @author cda
 */
public class MyAppController {
    private AnnonceDao annonceDao;
    private String filePath;
    private List<Annonce> annonceList;
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
    public void initialize() throws SQLException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        this.annonceDao = daoFactory.getAnnonceDao();
        this.types.setItems(optionsType);
        this.localisation.setItems(optionsVille);
        //Liez l'activité du bouton de recherche à la sélection de la ComboBox
        this.recherche.disableProperty().bind(types.valueProperty().isNull());
        this.annonceList = new ArrayList<>();
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
        progressBar.setProgress(0);
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
                System.out.println("erreur dans le thread");
            }
        };
        new Thread(task).start();
        // Exécuter une mise à jour de la barre de progression sur le thread de l'interface graphique
        task.progressProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> progressBar.setProgress(newValue.doubleValue()));
        });
    }

    /**
     * Methode de scrapping
     * @return
     */
    public StringBuilder scrappyBot() {
        // Variable pour le numero de la ville
        String villeNumOf = null;
        String villeNumSeloger = null;

        // Variable pour l'id du type
        int idType = 0;

        // Variable pour l'id de la ville
        int idVille = 0;

        // Je cree un stringbuilder
        StringBuilder retourRecherche = new StringBuilder();

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
        // J'associe un type de bien à son id
        switch (types.getValue()) {
            case "maison":
                idType = 1;
                break;
            case "appartement":
                idType = 2;
                break;
            case "box":
                idType = 3;
                break;
        }
        // Je recupere la page web OF par son url
        try {
            HtmlPage pageOF = ScrappyBot.getWebClient().getPage(ScrappyBot.urlBuilderOuestFrance(
                    types.getValue(),
                    villeNumOf,
                    prixMini.getText(),
                    prixMaxi.getText(),
                    surfaceMin.getText(),
                    surfaceMax.getText())
            );
            // Je recupere la page web SL par son url
            HtmlPage pageSL = ScrappyBot.getWebClient().getPage(ScrappyBot.urlBuilderSeLoger(
                    idType,
                    villeNumSeloger,
                    prixMini.getText(),
                    prixMaxi.getText(),
                    surfaceMin.getText(),
                    surfaceMax.getText()
            ));
            // Tableau des balises ouestfrance
            String[] balisesOF = {
                    "//a[@class='annLink']",
                    ".//span[@class='annTitre']",
                    ".//span[@class='annTexte hidden-phone']",
                    ".//span[@class='annPrix']",
                    ".//img[@class='annPhoto']",
                    ".//span[@class='annCriteres']/div"
            };
            // Tableau des balises seloger
            String[] balisesSL = {
                    "//div[@data-testid='sl.explore.card-container']",
                    ".//div[@data-test='sl.title']",
                    ".//div[@data-test='sl.address']",
                    ".//div[@data-test='sl.price-label']",
                    ".//img"
            };

            List<HtmlAnchor> htmlAnchors = pageOF.getByXPath("//*[@id=\"listAnnonces\"]/a");

            for (HtmlAnchor htmlAnchor: htmlAnchors) {
                double prixOF = 0;
                String titreOF = "";
                double surfaceOF = 0;
                String descriptionOF = "";
                HtmlPage pageAnnonce = (HtmlPage) htmlAnchor.click();
                HtmlElement prixElement = (HtmlElement) pageAnnonce.getFirstByXPath(".//span[@class='price']");
                // Je teste si l'element est null
                if (prixElement != null ) {
                    String prixStr = prixElement.asNormalizedText().replace("€", "").replace(" ", "");
                    prixOF = Double.parseDouble(prixStr);
                    System.out.println(prixOF);
                }
                HtmlElement titreElement = (HtmlElement) pageAnnonce.getFirstByXPath(".//h2[@class='annDescriptif fontDarkGrey']");
                if (titreElement != null) {
                    titreOF = titreElement.asNormalizedText();
                    System.out.println(titreOF);
                }
                HtmlElement surfaceElement = (HtmlElement) pageAnnonce.getFirstByXPath(".//span[@class='visible-phone-inline-block ann-criteres']/div[1]");
                if (surfaceElement != null) {
                    surfaceOF =Double.parseDouble(surfaceElement.asNormalizedText().replace("m²", ""));
                    System.out.println(surfaceOF);
                }
                HtmlElement descriptionElement = (HtmlElement) pageAnnonce.getFirstByXPath(".//div[@id='blockonDescriptif']");
                if (descriptionElement != null) {
                    descriptionOF = descriptionElement.asNormalizedText();
                    System.out.println(descriptionOF);
                }
            }


















//            // Je récupère toutes les divs principales
//            List<HtmlElement> htmlElements = pageSL.getByXPath(balisesSL[0]);
//
//            // Je boucle dessus pour recuperer ce qui m'interresse
//            for (HtmlElement element: htmlElements) {
//                // Description
//                HtmlElement descriptionElement = (HtmlElement) element.getFirstByXPath(balisesSL[1]);
//                String description = descriptionElement.asNormalizedText().trim();
//                // Lieu
//                HtmlElement lieuElement = (HtmlElement) element.getFirstByXPath(balisesSL[2]);
//                String lieu = lieuElement.asNormalizedText().trim();
//                // Prix
//                HtmlElement prixElement = (HtmlElement) element.getFirstByXPath(balisesSL[3]);
//                // Image
//                HtmlElement imgElement = (HtmlElement) element.getFirstByXPath(balisesSL[4]);
//                String imageUrl = "";
//
//                // Je teste si l'image est "null"
//                if( imgElement != null) {
//                    imageUrl = imgElement.getAttribute("src");
//                }
//                // Je ne recupere que la partie numerique du prix
//                String prixStr = prixElement.asNormalizedText().replace("€", "").replace(" ", "").trim();
//
//                // J'ajoute tous au texte que je retourne
//                retourRecherche.append("Site : seloger.com").append("\n");
//                retourRecherche.append("Lieu : ").append(lieu).append("\n");
//                retourRecherche.append("Description : ").append(description).append("\n");
//                retourRecherche.append("Prix : ").append(prixStr).append("€").append("\n");
//                retourRecherche.append("Url de l'image : ").append(imageUrl).append("\n\n");
//                this.annonceList.add(new Annonce(lieu, description, Double.parseDouble(prixStr), 0, idVille, idType));
//            }
//
//            // Je recupere tous les titres d'annonces de OF
//            List<HtmlElement> divs = page.getByXPath(balisesOF[0]);
//
//            // Pour toutes les annonces je recupere le prix et la description
//            for (HtmlElement div : divs) {
//                HtmlElement titreElement = (HtmlElement) div.getFirstByXPath(balisesOF[1]);
//                String titre = titreElement.asNormalizedText().trim();
//                HtmlElement descriptionElement = (HtmlElement) div.getFirstByXPath(balisesOF[2]);
//                String description = descriptionElement.asNormalizedText().trim();
//                HtmlElement prixElement = (HtmlElement) div.getFirstByXPath(balisesOF[3]);
//                String prixStr = prixElement.asNormalizedText().replace("€", "").trim();
//                String chiffresSeuls = prixStr.replaceAll("[^0-9]", "");
//                double prix = Double.parseDouble(chiffresSeuls.replace(" ", ""));
//                HtmlElement surfaceElement = (HtmlElement) div.getFirstByXPath(balisesOF[5]);
//                String surfaceStr = surfaceElement.asNormalizedText().replace("m²", "").trim();
//                double surface = Double.parseDouble(surfaceStr.replace(" ", ""));
//                HtmlElement parent = (HtmlElement) div.getParentNode();
//
//                HtmlElement imageElement = (HtmlElement) parent.getFirstByXPath("//img[@class='img annPhoto lazy']");
//
//                retourRecherche.append("Ouestfrance-immo\n");
//                retourRecherche.append("Titre : ").append(titre).append("\n");
//                retourRecherche.append("Surface : ").append(surface).append("\n");
//                retourRecherche.append("Description : ").append(description).append("\n");
//                retourRecherche.append("Prix : ").append(prix).append("\n\n");
//                this.annonceList.add(new Annonce(titre, description, prix, surface, idVille, idType));
//            }
//
//            // Je fais un update pour la barre de progression
//            for (int i = 0; i < htmlElements.size() + divs.size(); i++) {
//                // ... votre code existant
//
//                // Mettre à jour la progression à chaque itération
//                double progress = (i + 1.0) / (htmlElements.size() + divs.size());
//
//                // Exécutez la mise à jour de la barre de progression sur le thread de l'interface graphique
//                Platform.runLater(() -> progressBar.setProgress(progress));
//            }

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
        TransmissionBddController transmissionBddController = fxmlLoader.getController();
        transmissionBddController.setAnnonceList(this.annonceList);
        stage.setScene(scene);
        stage.show();
    }
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