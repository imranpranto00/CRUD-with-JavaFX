package com.imranpranto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.imranpranto.data.DBConnection;

/**
 * JavaFX App
 */
public class App extends Application {
    private StudentController sController = new StudentController();
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("student"));
        stage.setTitle("JAVAFX CRUD APPLICATION");
        sController.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        db.getDBConn();
        System.out.println("Connection status:"+ db.getCon());
        launch();
        
    }

}