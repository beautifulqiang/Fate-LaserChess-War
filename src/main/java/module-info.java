module com.example.flcw {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
//    requires eu.hansolo.fx.countries;

    opens flcwGUI to javafx.fxml;
    exports flcwGUI;
}