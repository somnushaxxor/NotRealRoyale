module ru.nsu.fit.kolesnik.notrealroyale {
    requires javafx.controls;
    requires javafx.fxml;

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
    exports ru.nsu.fit.kolesnik.notrealroyale.model.subscriber;
    opens ru.nsu.fit.kolesnik.notrealroyale.model.subscriber to javafx.fxml;
    exports ru.nsu.fit.kolesnik.notrealroyale.networking;
}