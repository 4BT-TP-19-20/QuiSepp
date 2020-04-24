package QiSepp;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class LehrerLogInController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField benuzterTextField;
    @FXML
    TextField passwortTextField;

    private Scene currentScene;
    private String lehrerBenutzername = "";
    private String lehrerPasswort = "";

    public void anmelden(){
        if(benuzterTextField.getText().compareTo(lehrerBenutzername) != 0){
            benuzterTextField.setStyle("-fx-text-fill: red");
            return;
        }

        if(passwortTextField.getText().compareTo(lehrerPasswort) != 0){
            passwortTextField.setStyle("-fx-text-fill: red");
            return;
        }
        LoadScreenAnimation("LehrerSelection.fxml");

    }

    public void resetBenetzernameField(){
        benuzterTextField.setStyle("-fx-text-fill: black");
    }

    public void resetPasswortField(){
        passwortTextField.setStyle("-fx-text-fill: black");
    }

    public void changeToSchuelerLogIn(){
        LoadScreenAnimation("SchuelerLogIn.fxml");
    }

    public void LoadScreenAnimation(String fxmlFile){
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
        });
        timeline.play();
    }


}
