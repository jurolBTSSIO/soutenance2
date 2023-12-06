package fr.cda.immobilier;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ModeEmploiController {
    @FXML
    private TextArea modeEmploi;

    @FXML
    private void initialize() {
        this.modeEmploi.setText("\nMode d'Emploi - Application de Recherche Immobilière\n\n"  +
                "1. Lancement de l'Application\n" +
                "    • Assurez-vous que Java JDK est installé sur votre machine.\n" +
                "    • Exécutez l'application en double-cliquant sur le fichier NomDeVotreApplication.jar ou via la\n" +
                "    ligne de commande.\n" +
                "2. Interface Principale\n" +
                "    Lorsque l'application démarre, vous verrez une interface conviviale avec les fonctionnalités\n" +
                "    principales.\n" +
                "3. Recherche de Biens Immobiliers\n" +
                "    1. Remplissez les Critères:\n" +
                "       • Sélectionnez le type de bien, l'emplacement géographique, le prix en euros, et la\n" +
                "       surface en mètres carrés.\n" +
                "2. Choisissez les Sites:\n" +
                "       • Cochez les cases correspondant aux sites sur lesquels vous souhaitez effectuer la\n" +
                "       recherche.\n" +
                "3. Lancement de la Recherche:\n" +
                "       • Cliquez sur le bouton \"Rechercher\" pour lancer la recherche.\n" +
                "4. Affichage des Résultats\n" +
                "       • Les résultats de la recherche seront affichés sous forme de liste avec des détails tels que le\n" +
                "       type de bien, le prix, la description, et le site web.\n" +
                "6. Enregistrement des Résultats\n" +
                "       • Cliquez sur le bouton \"Enregistrer dans un fichier\" pour sauvegarder les résultats dans un\n" +
                "       fichier texte.\n" +
                "       • Choisissez l'emplacement et le nom du fichier.\n" +
                "7. Envoi par Courriel\n" +
                "       • Cliquez sur le bouton \"Envoi Courriel\" pour envoyer les résultats par e-mail.\n" +
                "       • Saisissez l'adresse e-mail du destinataire et cliquez sur \"Envoyer\".\n" +
                "8. Stockage dans la Base de Données\n" +
                "       • Configurez les paramètres de connexion à la base de données dans le menu \"Paramètres-\n" +
                "       • Cliquez sur \"Enregistrer dans la base de données\" pour stocker les résultats.\n" +
                "9. Site Web\n" +
                "       • Déployez le site Web PHP ou JSP sur un serveur pour visualiser les résultats stockés dans la\n" +
                "       base de données.\n" +
                "10. Mode d'Emploi et Assistance\n" +
                "       • Cliquez sur le menu \"Aide\" pour accéder à l'item \"Mode d'emploi\" et obtenir des\n" +
                "       informations supplémentaires sur l'utilisation de l'application.");
    }
    @FXML
    private void fermer() {
        Stage stage = (Stage) modeEmploi.getScene().getWindow();
        stage.close();
    }
}