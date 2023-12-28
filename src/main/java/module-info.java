module flcwGUI {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.xml;


    opens flcwGUI to javafx.fxml;
    exports flcwGUI;
}
