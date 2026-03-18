package edu.mmcm.tradigital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Now loading the LOGIN view instead of the MAIN view
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/edu/mmcm/tradigital/fxml/login_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MMCM Tradigital - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}