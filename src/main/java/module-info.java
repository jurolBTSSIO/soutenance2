module fr.cda.immobilier.soutenance2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires htmlunit;


    opens fr.cda.immobilier to javafx.fxml;
    exports fr.cda.immobilier;
}