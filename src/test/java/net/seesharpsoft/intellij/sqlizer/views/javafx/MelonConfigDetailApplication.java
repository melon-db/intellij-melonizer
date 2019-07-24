package net.seesharpsoft.intellij.sqlizer.views.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.seesharpsoft.commons.collection.Properties;
import net.seesharpsoft.commons.util.SharpIO;
import net.seesharpsoft.intellij.plugins.sqlizer.views.javafx.MelonConfigDetailController;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;

public class MelonConfigDetailApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MelonConfigDetailController controller = new MelonConfigDetailController();

        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);

        Parent root;
        try (InputStream inputStream = SharpIO.createInputStream("/fxml/MelonConfigDetail.fxml")) {
            root = loader.load(inputStream);
        }

        Properties properties = new Properties();
        properties.put("melonConfig", new File("/dummy"));

        controller.init("/data/User.Csv", properties);

        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setTitle("Config detail");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void stop() throws Exception {
    }
}
