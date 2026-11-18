package main;

import javafx.application.Application;
import javafx.stage.Stage;
import db.Database;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database.prepareDatabase();
        new Navigator(stage).bukaKatalog();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
