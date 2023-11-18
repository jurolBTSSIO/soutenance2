module fr.cda.immobilier {
    requires javafx.controls;
    requires javafx.fxml;
    requires htmlunit;
    requires sib.api.v3.sdk;
    requires java.sql;
    requires mysql.connector.j;


    opens fr.cda.immobilier to javafx.fxml;
    exports fr.cda.immobilier;
}