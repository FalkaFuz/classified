package sk.zuzmat.classified.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by zuz on 12-May-16.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/MainOverview.fxml"), ResourceBundle.getBundle("localization/bundle"));
        BorderPane pane = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(pane);
        stage.setScene(scene);

        stage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
