package com.example.javafxtest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage)  {


        try{
        Parent root = FXMLLoader.load(getClass().getResource("openpositions-view.fxml"));

     //  Parent root = fxmlLoader.load();
          //  Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxtest/confirmWindow.fxml"));
        Scene scene = new Scene(root, 860, 680);
        String css = this.getClass().getResource("Style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            stage.setTitle("Trading app!");
        }
        catch(Exception e)
        {

        }
    }
    public static void main(String[] args) {
        launch();
    }
}