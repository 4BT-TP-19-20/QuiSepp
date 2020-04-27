package QiSepp;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("SchuelerLogIn.fxml").openStream());
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
        SchuelerLogInController schuelerLogInController = (SchuelerLogInController) fxmlLoader.getController();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
