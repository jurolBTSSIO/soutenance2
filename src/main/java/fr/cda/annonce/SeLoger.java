package fr.cda.annonce;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import fr.cda.immobilier.MyAppController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeLoger {
    private String url;
    private WebClient webClient;

    /**
     * @param url
     */
    public SeLoger(String url) throws IOException {
        this.url = url;
        this.webClient = new WebClient(BrowserVersion.EDGE);
    }
    public StringBuilder scrapSite() throws IOException {
        StringBuilder retourRecherche = new StringBuilder();
        String[] balisesSL = {
                "//div[@data-testid='sl.explore.card-container']",
                ".//div[@data-test='sl.title']",
                ".//div[@data-test='sl.address']",
                ".//div[@data-test='sl.price-label']",
                ".//img",
                ".//*[@data-testid='sl.explore.card-description']"
        };
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.webClient.getOptions().setCssEnabled(false);
        // Je recupere la page web SL par son url
        HtmlPage pageSL = this.webClient.getPage(url);
        //Je récupère toutes les divs principales de SLoger
        List<HtmlElement> htmlElements = pageSL.getByXPath("//div[@data-testid='sl.explore.card-container']");
        try {
            for (HtmlElement element : htmlElements) {
                if (element != null) {
                    double prixSL = 0;
                    String titreSL = "";
                    String descriptionSL = "";
                    String imageUrlSL = "";
                    double surfaceSL = 0;
                    String annonceUrlSL = "";
                    // Description
                    HtmlElement descriptionElement = (HtmlElement) element.getFirstByXPath(balisesSL[5]);
                    if (descriptionElement != null) {
                        descriptionSL = descriptionElement.asNormalizedText().trim().substring(0, 150);
                    }
                    // Lieu
                    HtmlElement lieuElement = (HtmlElement) element.getFirstByXPath(balisesSL[2]);
                    if (lieuElement != null) {
                        titreSL = lieuElement.asNormalizedText().trim();
                    }
                    // Surface
                    HtmlElement surfaceElement = (HtmlElement) element.getFirstByXPath(".//ul[@data-test='sl.tagsLine']/li[3]");
                    if (surfaceElement != null) {
                        surfaceSL = Double.parseDouble(surfaceElement.asNormalizedText().replace("m²", ""));
                    }
                    // Prix
                    HtmlElement prixElement = (HtmlElement) element.getFirstByXPath(balisesSL[3]);
                    // Je ne recupere que la partie numerique du prix
                    if (prixElement != null) {
                        String prixStr = prixElement.asNormalizedText().replace("€", "").replace(" ", "").trim();
                        prixSL = Double.parseDouble(prixStr);
                    }
                    // Url de l'annonce
                    HtmlElement urlAnnonceElement = (HtmlElement) element.getFirstByXPath(".//a[@data-testid='sl.explore.coveringLink']");
                    if (urlAnnonceElement != null) {
                        annonceUrlSL = "https://seloger.com" + urlAnnonceElement.getAttribute("href");
                    }
                    // Image
                    HtmlImage imgElement = (HtmlImage) element.getFirstByXPath(".//img");
                    if (imgElement != null) {
                        imageUrlSL = imgElement.getAttribute("src");
                        // J'ajoute tous au texte que je retourne
                        retourRecherche.append("Site : seloger.com").append("\n");
                        retourRecherche.append("Titre : ").append(titreSL).append("\n");
                        retourRecherche.append("Surface : ").append(surfaceSL).append("m²").append("\n");
                        retourRecherche.append("Prix : ").append(prixSL).append("€").append("\n");
                        retourRecherche.append("Url de l'image : ").append(imageUrlSL).append("\n\n");
                        MyAppController.annonceList.add(new Annonce(titreSL, prixSL, descriptionSL, surfaceSL, imageUrlSL, annonceUrlSL, 1, 1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retourRecherche;
    }
}
