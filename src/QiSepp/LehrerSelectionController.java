package QiSepp;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class LehrerSelectionController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;

    public void btnNeuesQuiz(){
        LoadScreenAnimation("NeuesQuizErstellen.fxml", true);
    }

    public void LoadScreenAnimation(String fxmlFile, boolean rescaleWindow){
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource(fxmlFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = anchorPane.getScene();
        newRoot.translateXProperty().set(scene.getWidth());
        root.getChildren().add(newRoot);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(newRoot.translateXProperty(),0 , Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(t -> {
            root.getChildren().remove(anchorPane);
            if(rescaleWindow == true) {
                scene.getWindow().setWidth(900);
                scene.getWindow().setHeight(900);
                scene.getWindow().setX(500);
                scene.getWindow().setY(70);
            }
        });
        timeline.play();
    }

    public void btnAlleQuiz(){
        LoadScreenAnimation("DisplayAllQuizzes.fxml", false);
    }

    public void goBack(){
        LoadScreenAnimation("LehrerLogIn.fxml", false);
    }

}
