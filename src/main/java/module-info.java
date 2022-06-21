module ru.nsu.fit.kolesnik.notrealroyale {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens ru.nsu.fit.kolesnik.notrealroyale to javafx.fxml;
    exports ru.nsu.fit.kolesnik.notrealroyale;
    exports ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;
    opens ru.nsu.fit.kolesnik.notrealroyale.model.gameobject to javafx.fxml;
    exports ru.nsu.fit.kolesnik.notrealroyale.controller;
    opens ru.nsu.fit.kolesnik.notrealroyale.controller to javafx.fxml;
    exports ru.nsu.fit.kolesnik.notrealroyale.model;
    opens ru.nsu.fit.kolesnik.notrealroyale.model to javafx.fxml;
    exports ru.nsu.fit.kolesnik.notrealroyale.view;
    opens ru.nsu.fit.kolesnik.notrealroyale.view to javafx.fxml;
    exports ru.nsu.fit.kolesnik.notrealroyale.view.javafx;
    opens ru.nsu.fit.kolesnik.notrealroyale.view.javafx to javafx.fxml;
    exports ru.nsu.fit.kolesnik.notrealroyale.model.worldmap;
    opens ru.nsu.fit.kolesnik.notrealroyale.model.worldmap to javafx.fxml;
}