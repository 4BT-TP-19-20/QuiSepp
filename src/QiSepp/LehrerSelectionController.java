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
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("NeuesQuizErstellen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane, 900, 900, 500, 70);
    }

    public void btnAlleQuiz(){
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("DisplayAllQuizzes.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);
    }

    public void goBack(){
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("LehrerLogIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);
    }

}
