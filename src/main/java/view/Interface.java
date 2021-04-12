package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Interface extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL url = new File("src\\main\\java\\view\\sample.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        stage.setTitle("Queues simulator");
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
