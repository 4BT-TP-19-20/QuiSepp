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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class NeuesQuizErstellenController {
    @FXML
    TextField quizName;
    @FXML
    TextField quizPassword;
    @FXML
    TextField maxPunkteTextField, punktePerFrageTextField;
    int maxPunkte = 0;
    @FXML
    Label disNumFrage, displayVergebenePunkte;
    int numFrage = 1;
    int currentFrage = 1;
    @FXML
    TextField frage;
    ArrayList<TextField> allAnswers = new ArrayList<>();
    ArrayList<CheckBox> allAnswerCheckBox = new ArrayList<>();
    @FXML
    VBox containerAllAnswers;
    @FXML
    VBox containerAllCheckBox;
    ArrayList<String> allData = new ArrayList<>();
    @FXML
    Button frageBtn;
    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;

    public void addAnswer() {
        if (allAnswers.size() < 20){
            TextField textField = new TextField();
            CheckBox checkBox = new CheckBox();
            containerAllCheckBox.getChildren().add(checkBox);
            containerAllAnswers.getChildren().add(textField);
            textField.setPromptText("Gib eine Antort ein");
            checkBox.setText("");
            allAnswers.add(textField);
            allAnswerCheckBox.add(checkBox);
            checkBox.setPrefHeight(25);
         }
    }

    public void removeAnswer(){
        if(allAnswers.size() > 0){
            containerAllCheckBox.getChildren().remove(allAnswerCheckBox.get(allAnswerCheckBox.size() - 1));
            containerAllAnswers.getChildren().remove(allAnswers.get(allAnswers.size() - 1));
            allAnswers.remove(allAnswers.size() - 1);
            allAnswerCheckBox.remove(allAnswerCheckBox.size() - 1);
        }
    }

    public void collectData(){
        String data = "";
        data += frage.getText() + ";";
        for(int i = 0; i < allAnswers.size(); i++){
            data += allAnswers.get(i).getText() + ";";
            if(allAnswerCheckBox.get(i).isSelected() == true){
                data += "true" + ";";
            }else {
                data += "false" + ";";
            }
        }
        allData.add(data);
    }

    public void collectData(int pos){
        String data = "";
        data += frage.getText() + ";";
        for(int i = 0; i < allAnswers.size(); i++){
            data += allAnswers.get(i).getText() + ";";
            if(allAnswerCheckBox.get(i).isSelected() == true){
                data += "true" + ";";
            }else {
                data += "false" + ";";
            }
        }
        allData.remove(pos);
        allData.add(pos, data);
    }

    public void frageHinzufuegen(){
        if(currentFrage == numFrage) {

            collectData();
            numFrage++;
            currentFrage = numFrage;
            disNumFrage.setText("Frage: " + numFrage);
            Clear();
            frage.setText("");
        }else {
            collectData(currentFrage - 1);
        }
    }

    public void forward(){
        currentFrage++;
        if(currentFrage == numFrage) {
            disNumFrage.setText("Frage: " + numFrage);
            frageBtn.setText("Frage hinzufügen");
            Clear();
            frage.setText("");
        }else if(currentFrage < numFrage) {
            frageBtn.setText("Frage neu speichern");
            LoadQuestion();
        }else{
            currentFrage--;
        }
    }


    public void back(){
        if(currentFrage > 1) {
            frageBtn.setText("Frage neu speichern");
            currentFrage--;
            LoadQuestion();
        }
    }

    public void LoadQuestion(){
        Clear();
        disNumFrage.setText("Frage: " + currentFrage);
        String[] data = allData.get(currentFrage - 1).split(";");
        frage.setText(data[0]);
        for(int i = 1; i < data.length; i += 2){
            TextField textField = new TextField();
            CheckBox checkBox = new CheckBox();
            containerAllCheckBox.getChildren().add(checkBox);
            containerAllAnswers.getChildren().add(textField);
            allAnswers.add(textField);
            allAnswerCheckBox.add(checkBox);
            checkBox.setPrefHeight(25);
            checkBox.setText("");
            if(data[i + 1].compareTo("true") == 0){
                checkBox.setSelected(true);
            }else {
                checkBox.setSelected(false);
            }
            textField.setText(data[i]);
        }
    }


    public void Clear(){
        containerAllAnswers.getChildren().clear();
        containerAllCheckBox.getChildren().clear();
        allAnswers.clear();
        allAnswerCheckBox.clear();
    }



    public void quizSpeichern(){

        File f = new File("Quizze\\" + quizName.getText() + ".txt");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String finalData = quizName.getText() + ";" + quizPassword.getText() + ";;";

        System.out.println("                                         ");
        for(int i = 0; i < allData.size(); i++){
            finalData += allData.get(i) + ";";
            System.out.println(allData.get(i));
        }

        try (FileWriter writer = new FileWriter(f.getPath());
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(finalData);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
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
            scene.getWindow().setWidth(600);
            scene.getWindow().setHeight(500);
            scene.getWindow().setX(652);
            scene.getWindow().setY(180);
        });
        timeline.play();
    }

    public void changeMaxPunkte(){
        maxPunkte = Integer.parseInt(maxPunkteTextField.getText());
    }

    public void goBack(){
        LoadScreenAnimation("LehrerSelection.fxml");
    }

}
