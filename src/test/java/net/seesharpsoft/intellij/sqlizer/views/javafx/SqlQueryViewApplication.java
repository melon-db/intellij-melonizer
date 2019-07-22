package net.seesharpsoft.intellij.sqlizer.views.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.seesharpsoft.commons.util.SharpIO;
import net.seesharpsoft.intellij.plugins.sqlizer.views.javafx.SqlQueryViewController;
import net.seesharpsoft.melon.Constants;
import net.seesharpsoft.melon.jdbc.MelonConnection;
import net.seesharpsoft.melon.jdbc.MelonDbDriver;
import net.seesharpsoft.melon.jdbc.MelonDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class SqlQueryViewApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MelonDbDriver.load();

        Properties properties = new Properties();
        properties.put(Constants.PROPERTY_CONFIG_FILE, "/standalone/config01.yaml");
        connection = DriverManager.getConnection("jdbc:melondb:test", properties);

        FXMLLoader loader = new FXMLLoader();
        loader.setController(new SqlQueryViewController(connection));

        Parent root;
        try (InputStream inputStream = SharpIO.createInputStream("/fxml/SqlQueryView.fxml")) {
            root = loader.load(inputStream);
        }
        // Parent root = loader.load(getClass().getResource("/net/seesharpsoft/intellij/plugins/sqlizer/views/javafx/SqlQueryView.fxml"));
        // Parent root = new Group();

        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setTitle("SqlQueryView Standalone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void stop() throws Exception {
        connection.close();
    }
}
