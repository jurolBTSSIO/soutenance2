package tool;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Classe ScrappyBot
 * @author Julien Rolland
 */
public class ScrappyBot {
    private static WebClient webClient;
    /**
     * Methode qui retourne le WebClient
     * @return
     */
    public static WebClient getWebClient() {
        // Creation d'un webclient
        webClient = new WebClient(BrowserVersion.FIREFOX);
        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        // Parametrage des options
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setTimeout(5000);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        return webClient;
    }
    public static String urlBuilderOuestFrance(String type, String ville, String prixMin, String prixMax, String surfaceMin, String surfaceMax) {
        StringBuilder url = new StringBuilder();
        url.append("https://www.ouestfrance-immo.com/acheter/");

        // J'ajoute le type de biens
        url.append(type);

        // J'ajoute le separateur
        url.append("/");

        // J'ajoute le ref de la ville choisie
        url.append(ville);

        // J'ajoute le parametre prix
        url.append("/?prix=");

        // J'ajoute le prix mini à l'url
        url.append(prixMin);

        // J'ajoute le separateur prix mini / prix maxi
        url.append("_");

        // J'ajoute le prix maxi
        url.append(prixMax);

        // J'ajoute le &
        url.append("&");

        // J'ajoute le parametre surface
        url.append("surface=");

        // J'ajoute la surface mini
        url.append(surfaceMin);

        // J'ajoute le separateur surface mini / surface maxi
        url.append("_");

        // J'ajoute la surface maxi
        url.append(surfaceMax);
        return url.toString();
    }
}
