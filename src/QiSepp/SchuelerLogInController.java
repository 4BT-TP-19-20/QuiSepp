package QiSepp;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class SchuelerLogInController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField nameTextField;
    @FXML
    TextField nachnameTextField;
    @FXML
    Button schuelerBtn, lehrerBtn, quizStartenBtn;
    @FXML
    Label nameLabel, nachnameLabel;

    private Scene currentScene;
    private String name;
    private String nachname;

    public void starteQuiz(){
        if(nameTextField.getText().equals("")){
           nameTextField.setStyle("-fx-background-color: red");
        }else{
            name = nameTextField.getText();
        }
        if(nachnameTextField.getText().equals("")){
            nachnameTextField.setStyle("-fx-background-color: red");
        }else{
            nachname = nachnameTextField.getText();
        }
    }


    public void changeToLehrerLogIn(){
        LoadScreenAnimation("LehrerLogIn.fxml");
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

    /*public void SetHandler(){
        Scene scene = root.getScene();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                double offset = (double)newSceneWidth - (double)oldSceneWidth;
                schuelerBtn.setPrefWidth(schuelerBtn.getPrefWidth() + offset);
                lehrerBtn.setPrefWidth(lehrerBtn.getPrefWidth() + offset);
                quizStartenBtn.setPrefWidth(quizStartenBtn.getPrefWidth() + offset);
                nameLabel.setPrefWidth(nameLabel.getPrefWidth() + offset);
                nachnameLabel.setPrefWidth(nachnameLabel.getPrefWidth() + offset);
                nameTextField.setPrefWidth(nameTextField.getPrefWidth() + offset);
                nachnameTextField.setPrefWidth(nachnameTextField.getPrefWidth() + offset);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                double offset = (double)newSceneHeight - (double)oldSceneHeight;
                schuelerBtn.setPrefHeight(schuelerBtn.getPrefHeight() + offset);
                lehrerBtn.setPrefHeight(lehrerBtn.getPrefHeight() + offset);
                quizStartenBtn.setPrefHeight(quizStartenBtn.getPrefHeight() + offset);
                nameLabel.setPrefHeight(nameLabel.getPrefHeight() + offset);
                nachnameLabel.setPrefHeight(nachnameLabel.getPrefHeight() + offset);
                nameTextField.setPrefHeight(nameTextField.getPrefHeight() + offset);
                nachnameTextField.setPrefHeight(nachnameTextField.getPrefHeight() + offset);
            }
        });
    }*/

}
