package fr.cda.immobilier;


import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
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
        // TODO initialiser toutes les variables
        // Je l'utilise pour mettre a jour la progressbar
        int i = 0;

        // La vaiable de type string pour la localisation
        String localisationStr = null;

        // Variable pour l'id du type
        int idType = 0;

        // Variable pour l'id de la ville
        int idVille = 0;

        // Variable pour le numero de la ville
        String villeNumOf = null;
        String villeNumSeloger = null;

        // Je cree un stringbuilder pour retourner la recherche
        StringBuilder retourRecherche = new StringBuilder();

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
                ".//img",
                ".//*[@data-testid='sl.explore.card-description']"
        };



        // TODO recuperer les valeurs des champs et les filtrer
        if (localisation.getValue() != null) {

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
        // J'associe un type de bien à son id
        switch (types.getValue()) {
            case "appartement":
                idType = 1;
                break;
            case "maison":
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



            List<HtmlAnchor> htmlAnchors = pageOF.getByXPath("//*[@id=\"listAnnonces\"]/a");

            for (HtmlAnchor htmlAnchor: htmlAnchors) {
                double prixOF = 0;
                String titreOF = "";
                double surfaceOF = 0;
                String descriptionOF = "";
                String urlImgOF = "";
                HtmlPage pageAnnonce = (HtmlPage) htmlAnchor.click();
                HtmlElement prixElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//span[@class='price']");
                // Je teste si l'element est null
                if (prixElementOF != null ) {
                    String prixStr = prixElementOF.asNormalizedText().replace("€", "").replace(" ", "");
                    prixOF = Double.parseDouble(prixStr);
                }
                HtmlElement titreElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//h2[@class='annDescriptif fontDarkGrey']");
                if (titreElementOF != null) {
                    titreOF = titreElementOF.asNormalizedText();
                }
                HtmlElement surfaceElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//span[@class='visible-phone-inline-block ann-criteres']/div[1]");
                if (surfaceElementOF != null) {
                    surfaceOF =Double.parseDouble(surfaceElementOF.asNormalizedText().replace("m²", ""));
                }
                HtmlElement descriptionElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//div[@id='blockonDescriptif']");
                if (descriptionElementOF != null) {
                    descriptionOF = descriptionElementOF.asNormalizedText().substring(0, 50);
                }
                HtmlElement imgElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//img[@class='slideimg_0']");
                if (imgElementOF != null) {
                    urlImgOF = imgElementOF.getAttribute("src");
                }
                retourRecherche.append("Site : ouestfrance-immo.com").append("\n");
                retourRecherche.append("Titre : ").append(titreOF).append("\n");
                retourRecherche.append("Surface : ").append(surfaceOF).append("m²").append("\n");
                retourRecherche.append("Prix : ").append(prixOF).append("€").append("\n");
                retourRecherche.append("Url de l'image : ").append(urlImgOF).append("\n\n");
                this.annonceList.add(new Annonce(titreOF, descriptionOF, prixOF, 0, idVille, idType));
                // Mettre à jour la progression à chaque itération
                double progress = (i + 1.0) / htmlAnchors.size();
                i++;
                // Exécutez la mise à jour de la barre de progression sur le thread de l'interface graphique
                Platform.runLater(() -> progressBar.setProgress(progress));
            }
            // Je recupere la page web SL par son url
            HtmlPage pageSL = ScrappyBot.getWebClient().getPage(ScrappyBot.urlBuilderSeLoger(
                    idType,
                    villeNumSeloger,
                    prixMini.getText(),
                    prixMaxi.getText(),
                    surfaceMin.getText(),
                    surfaceMax.getText()
            ));
            // Je récupère toutes les divs principales de SLoger
            List<HtmlElement> htmlElements = pageSL.getByXPath(balisesSL[0]);
            // Je boucle dessus pour recuperer ce qui m'interresse
            for (HtmlElement element: htmlElements) {
                if (element != null) {
                    String prixStr = "";
                    String lieu = "";
                    String description = "";
                    String imageUrl = "";
                    // Description
                    HtmlElement descriptionElement = (HtmlElement) element.getFirstByXPath(balisesSL[5]);
                    if (descriptionElement != null) {
                        description = descriptionElement.asNormalizedText().trim();
                    }
                    // Lieu
                    HtmlElement lieuElement = (HtmlElement) element.getFirstByXPath(balisesSL[2]);
                    if (lieuElement != null) {
                        lieu = lieuElement.asNormalizedText().trim();
                    }
                    // Prix
                    HtmlElement prixElement = (HtmlElement) element.getFirstByXPath(balisesSL[3]);
                    // Je ne recupere que la partie numerique du prix
                    if (prixElement != null) {
                        prixStr = prixElement.asNormalizedText().replace("€", "").replace(" ", "").trim();
                    }
                    // Image
                    HtmlImage imgElement = (HtmlImage) element.getFirstByXPath(".//img");

                    if (imgElement != null) {
                        System.out.println(imgElement.asXml());
                        imageUrl = imgElement.getAttribute("src");
                    }

                    // J'ajoute tous au texte que je retourne
                    retourRecherche.append("Site : seloger.com").append("\n");
                    retourRecherche.append("Lieu : ").append(lieu).append("\n");
                    retourRecherche.append("Description : ").append(description).append("\n");
                    retourRecherche.append("Prix : ").append(prixStr).append("€").append("\n");
                    retourRecherche.append("Url de l'image : ").append(imageUrl).append("\n\n");
                    this.annonceList.add(new Annonce(lieu, description, Double.parseDouble(prixStr), 0, idVille, idType));
                    double progress = (i + 1.0) / htmlAnchors.size();
                    i++;
                    // Exécutez la mise à jour de la barre de progression sur le thread de l'interface graphique
                    Platform.runLater(() -> progressBar.setProgress(progress));
                }
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