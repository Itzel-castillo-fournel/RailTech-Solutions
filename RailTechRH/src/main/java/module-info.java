module fr.irontrail.railtechrh {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires dotenv.java;
    requires java.desktop;

    exports fr.irontrail.railtechrh.controller.conducteur to javafx.fxml;
    opens fr.irontrail.railtechrh.controller to javafx.fxml;
    opens fr.irontrail.railtechrh.controller.conducteur to javafx.fxml;
    exports fr.irontrail.railtechrh.main;

}