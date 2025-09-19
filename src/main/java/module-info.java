module org.example.examen1_aaron_ruiz_medina {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.xml.bind;

    opens org.example.examen1_aaron_ruiz_medina to javafx.fxml;
    exports org.example.examen1_aaron_ruiz_medina;

    opens datos to jakarta.xml.bind;
    opens model to jakarta.xml.bind;
    opens logica to jakarta.xml.bind;

    opens controller to javafx.fxml;
    exports controller;
    opens datos.entity to jakarta.xml.bind;
}