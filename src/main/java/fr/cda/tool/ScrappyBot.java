package fr.cda.tool;

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
        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        // Parametrage des options
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }

    /**
     * Methode qui retourne une url pour OF
     * @param type
     * @param ville
     * @param prixMin
     * @param prixMax
     * @param surfaceMin
     * @param surfaceMax
     * @return
     */
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

    /**
     * Methode qui retourne une url pour SL
     * @param type
     * @param ville
     * @param prixMin
     * @param prixMax
     * @param surfaceMin
     * @param surfaceMax
     * @return
     */
    public static String urlBuilderSeLoger(int type, String ville, String prixMin, String prixMax, String surfaceMin, String surfaceMax) {
        return "https://www.seloger.com/list.htm?projects=2,5&types="+ String.valueOf(type)+"&natures=1,2,4&places=[{\"inseeCodes\":["+ ville +"]}]&price="+ prixMin +"/"+ prixMax +"&mandatorycommodities=0&enterprise=0&qsVersion=1.0&m=homepage_buy-redirection-search_results";
    }
}
