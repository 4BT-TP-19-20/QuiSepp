package QiSepp;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class DisplayQuizzController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    @FXML
    Label displayFrageNum, displayFrage, quizName;
    @FXML
    VBox containerCheckboxes, containerAnswers;
    ArrayList<String> answers = new ArrayList<>();
    String[] allQuestions;
    String[] allAnswers;
    int currFrage = 0;
    boolean canEditQuiz = false;
    boolean canAnswerQuiz = false;
    

    public void zurueck(){

    }

    public void naechste(){

    }

    public void goBack() {
        LoadScreenAnimation("LehrerSelection.fxml");
    }

    public void receiveQuiz(String str, boolean canEditQuiz, boolean canAnswerQuiz){
        this.canAnswerQuiz = canAnswerQuiz;
        this.canEditQuiz = canEditQuiz;
        String[] data = str.split(";;");
        quizName.setText(data[0].split(";")[0]);
        allQuestions = new String[data.length - 1];
        allAnswers = new String[data.length - 1];
        for (int i = 1; i < data.length; i ++){
            allQuestions[i - 1] = data[i];
            System.out.println(allQuestions[i - 1]);
        }
        currFrage = 0;
        addAnswer();
    }

    public void addAnswer() {
        String[] question = allQuestions[currFrage].split(";");
        displayFrage.setText(question[0]);
        displayFrageNum.setText("Frage: " + (currFrage+1));
        for(int i = 1; i < question.length; i += 2){
            System.out.println(question[i]);
            TextField textField = new TextField();
            CheckBox checkBox = new CheckBox();
            containerCheckboxes.getChildren().add(checkBox);
            containerAnswers.getChildren().add(textField);
            checkBox.setText("");
            checkBox.setPrefHeight(25);
            textField.setText(question[i]);
            if(canAnswerQuiz == false) {
                if (question[i + 1].compareTo("true") == 0) {
                    checkBox.setSelected(true);
                } else {
                    checkBox.setSelected(false);
                }
            }
        }

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
