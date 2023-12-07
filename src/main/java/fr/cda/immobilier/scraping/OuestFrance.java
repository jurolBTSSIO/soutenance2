package fr.cda.immobilier.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import fr.cda.annonce.Annonce;
import fr.cda.immobilier.MyAppController;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe OuestFrance
 * @author Julien Rolland
 */
public class OuestFrance {
    private String url;
    private WebClient webClient;

    /**
     * @param url
     */
    public OuestFrance(String url) throws IOException {
        this.url = url;
        this.webClient = new WebClient();
    }
    public StringBuilder scrapSite() throws IOException {
        MyAppController.annonceList = new ArrayList<>();
        StringBuilder retourRecherche = new StringBuilder();
        this.webClient.getOptions().setCssEnabled(false);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage pageOF = this.webClient.getPage(this.url);
        List<HtmlAnchor> htmlAnchors = pageOF.getByXPath("//*[@id=\"listAnnonces\"]/a");
        try {
            for (HtmlAnchor htmlAnchor : htmlAnchors) {
                double prixOF = 0;
                String titreOF = "";
                double surfaceOF = 0;
                String descriptionOF = "";
                String urlAnnonce = "https://www.ouestfrance-immo.com/" + htmlAnchor.getAttribute("href");
                String fetchedPhoto = "";

                HtmlPage pageAnnonce = (HtmlPage) htmlAnchor.click();
                HtmlElement prixElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//span[@class='price']");
                // Je teste si l'element est null
                if (prixElementOF != null) {
                    String prixStr = prixElementOF.asNormalizedText().replace("€", "").replace(" ", "");
                    prixOF = Double.parseDouble(prixStr);
                }
                HtmlElement titreElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//h2[@class='annDescriptif fontDarkGrey']");
                if (titreElementOF != null) {
                    titreOF = titreElementOF.asNormalizedText();
                }
                HtmlElement surfaceElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//span[@class='visible-phone-inline-block ann-criteres']/div[1]");
                if (surfaceElementOF != null) {
                    surfaceOF = Double.parseDouble(surfaceElementOF.asNormalizedText().replace("m²", ""));
                }
                HtmlElement descriptionElementOF = (HtmlElement) pageAnnonce.getFirstByXPath(".//div[@id='blockonDescriptif']");
                if (descriptionElementOF != null) {
                    descriptionOF = descriptionElementOF.asNormalizedText().substring(0, 150);
                }
                HtmlElement individualPhotos = pageAnnonce.getFirstByXPath(".//div[@id='sliderLoader']/img");
                if (individualPhotos != null) {
                    fetchedPhoto = individualPhotos.getAttribute("src");
                }
                // Je filtre les annonces incomplètes
                if ( prixOF != 0) {
                    retourRecherche.append("Site : ouestfrance-immo.com").append("\n");
                    retourRecherche.append("Type de bien : ").append(titreOF).append("\n");
                    retourRecherche.append("Prix : ").append(prixOF).append("€").append("\n");
                    retourRecherche.append("Description : ").append(descriptionOF).append("\n");
                    retourRecherche.append("Url de l'annonce : ").append(urlAnnonce).append("\n\n");
                    MyAppController.annonceList.add(new Annonce(titreOF, prixOF, descriptionOF, surfaceOF, fetchedPhoto, urlAnnonce, 1, 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retourRecherche;
    }
}
