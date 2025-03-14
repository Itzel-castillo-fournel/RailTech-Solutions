module fr.irontrail.railtechrh {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires dotenv.java;
    requires java.desktop;
    requires java.prefs;

    exports fr.irontrail.railtechrh.controller.conducteur to javafx.fxml;
    opens fr.irontrail.railtechrh.controller to javafx.fxml;
    opens fr.irontrail.railtechrh.controller.conducteur to javafx.fxml;
    exports fr.irontrail.railtechrh.main;
    exports fr.irontrail.railtechrh.dao;
    exports fr.irontrail.railtechrh.model;
    opens fr.irontrail.railtechrh.dao;
    exports fr.irontrail.railtechrh.controller.operateur; // Exporte le package contenant le contrôleur
    opens fr.irontrail.railtechrh.controller.operateur to javafx.fxml; // Ouvre le package pour la réflexion

    exports fr.irontrail.railtechrh.controller.technicien;
    opens fr.irontrail.railtechrh.controller.technicien to javafx.fxml;

    exports fr.irontrail.railtechrh.controller.admin;
    opens fr.irontrail.railtechrh.controller.admin to javafx.fxml;

    opens fr.irontrail.railtechrh.main; // Ouvre le package pour la réflexion
}