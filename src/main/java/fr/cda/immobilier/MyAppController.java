package fr.cda.immobilier;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Map;

/**
 * Classe MyAppController
 * @author cda
 */
public class MyAppController {
    private Map<Integer, String> annoncesList;
    private ObservableList<String> options =
            FXCollections.observableArrayList(
                    "Maison",
                    "Appartement",
                    "Box"
            );
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
    private CheckBox seLoger;
    @FXML
    private CheckBox ouestFrance;
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
    public StringBuilder handleRechercheButton() {
        // Je créé un stringbuilder
        StringBuilder retourRecherche = new StringBuilder();
        // Mettez ici le code pour lancer la recherche en fonction des critères
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
            HtmlPage page = client.getPage(url);
            List<HtmlElement> titres = page.getByXPath("//span[@class='annTitre']");
            List<HtmlElement> prix = page.getByXPath("//span[@class='annPrix']");
            List<HtmlElement> links = page.getByXPath("//a[@class='annLink']");
            for (HtmlElement titre : titres) {
                String annonceText = titre.asNormalizedText().trim();
                System.out.println("Titre: " + annonceText);
                retourRecherche.append("Titre: " + annonceText).append("\n");
                // Trouver le parent div de l'annonce
                HtmlElement parentDiv = (HtmlElement) titre.getParentNode();

                // Trouver le parent au dessus de parentDiv
                HtmlElement parentDiv2 = (HtmlElement) parentDiv.getParentNode();
                //System.out.println(parentDiv2.asXml());

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
        return retourRecherche;
        }

    @FXML
    private void enregistrerDansUnFichier() {

    }
}