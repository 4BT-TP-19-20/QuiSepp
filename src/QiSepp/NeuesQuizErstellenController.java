package QiSepp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class NeuesQuizErstellenController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField maxPunkteTextField, punktePerFrageTextField, frage, quizPassword, quizName;
    @FXML
    Label disNumFrage, displayVergebenePunkte;
    @FXML
    VBox containerAllAnswers, containerAllCheckBox;

    ArrayList<TextField> allCurrentAnswers = new ArrayList<>();
    ArrayList<CheckBox> allCurrentAnswerCheckBox = new ArrayList<>();
    ArrayList<Integer> allPoints = new ArrayList<>();
    ArrayList<String> allData = new ArrayList<>();

    int numFrage = 1;
    int currentFrage = 1;
    int maxPunkte = 0;

    int MAX_ANSWERS = 15;
    int DEAFAULT_FRAGE_PUNKTE = 1;

    public void addAnswer() {
        if (allCurrentAnswers.size() < MAX_ANSWERS){
            TextField textField = new TextField();
            CheckBox checkBox = new CheckBox();
            containerAllCheckBox.getChildren().add(checkBox);
            containerAllAnswers.getChildren().add(textField);
            textField.setPromptText("Gib eine Antort ein");
            checkBox.setText("");
            allCurrentAnswers.add(textField);
            allCurrentAnswerCheckBox.add(checkBox);
            checkBox.setPrefHeight(25);
        }
    }

    public void removeAnswer(){
        if(allCurrentAnswers.size() > 0){
            containerAllCheckBox.getChildren().remove(allCurrentAnswerCheckBox.get(allCurrentAnswerCheckBox.size() - 1));
            containerAllAnswers.getChildren().remove(allCurrentAnswers.get(allCurrentAnswers.size() - 1));
            allCurrentAnswers.remove(allCurrentAnswers.size() - 1);
            allCurrentAnswerCheckBox.remove(allCurrentAnswerCheckBox.size() - 1);
        }
    }

    public void collectData(){
        String data = "";
        data += frage.getText() + ";" + getFragePunkte() + ";";
        for(int i = 0; i < allCurrentAnswers.size(); i++){
            data += allCurrentAnswers.get(i).getText() + ";";
            if(allCurrentAnswerCheckBox.get(i).isSelected() == true){
                data += "true" + ";";
            }else {
                data += "false" + ";";
            }
        }
        allPoints.add(getFragePunkte());
        getFragePunkte();
        changeVergebenPunkte();
        allData.add(data);
    }

    public void collectData(int pos){
        String data = "";
        data += frage.getText() + ";" + getFragePunkte() + ";";
        for(int i = 0; i < allCurrentAnswers.size(); i++){
            data += allCurrentAnswers.get(i).getText() + ";";
            if(allCurrentAnswerCheckBox.get(i).isSelected() == true){
                data += "true" + ";";
            }else {
                data += "false" + ";";
            }
        }
        allPoints.remove(pos);
        allPoints.add(pos, getFragePunkte());
        getFragePunkte();
        changeVergebenPunkte();
        allData.remove(pos);
        allData.add(pos, data);
    }

    public void frageHinzufuegen(){
        if (frage.getText().compareTo("") != 0) {
            if (currentFrage == numFrage && allData.size() < numFrage) {
                collectData();
                numFrage++;
                currentFrage = numFrage;
                disNumFrage.setText("Frage: " + numFrage);
                Clear();
                punktePerFrageTextField.setText("");
                frage.setText("");
            } else {
                collectData(currentFrage - 1);
            }
            System.out.println("                                         ");
            for (int i = 0; i < allData.size(); i++) {
                System.out.println(allData.get(i));
            }
        }

    }

    public void LoadExistingQuiz(String str){
        //splitt the received data into all questions
        String[] data = str.split(";;");

        //set the Quiz name
        quizName.setText(data[0].split(";")[0]);

        //set the Quiz Password
        quizPassword.setText(data[0].split(";")[1]);

        //set the quiz MaxPoints
        maxPunkteTextField.setText(data[0].split(";")[2]);

        //store all question, answer and points
        for (int i = 1; i < data.length; i ++){
            allData.add(data[i] + ";");
            allPoints.add(Integer.parseInt(data[i].split(";")[1]));
        }
        currentFrage = 2;
        numFrage += allData.size();
        back();
        changeMaxPunkte();
    }

    public void forward(){
        frageHinzufuegen();
        currentFrage++;
        if(currentFrage == numFrage) {
            disNumFrage.setText("Frage: " + numFrage);
            Clear();
            punktePerFrageTextField.setText("");
            frage.setText("");
        }else if(currentFrage < numFrage) {
            LoadQuestion();
        }else{
            currentFrage--;
        }
    }


    public void back(){
        frageHinzufuegen();
        if(currentFrage > 1) {
            currentFrage--;
            LoadQuestion();
        }

    }

    public void LoadQuestion(){
        Clear();
        disNumFrage.setText("Frage: " + currentFrage);
        String[] data = allData.get(currentFrage - 1).split(";");
        frage.setText(data[0]);
        punktePerFrageTextField.setText(data[1]);
        for(int i = 2; i < data.length; i += 2){
            TextField textField = new TextField();
            CheckBox checkBox = new CheckBox();
            containerAllCheckBox.getChildren().add(checkBox);
            containerAllAnswers.getChildren().add(textField);
            allCurrentAnswers.add(textField);
            allCurrentAnswerCheckBox.add(checkBox);
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
        allCurrentAnswers.clear();
        allCurrentAnswerCheckBox.clear();
    }


    public void quizSpeichern(){

        frageHinzufuegen();
        File f = new File("Quizze\\" + quizName.getText() + ".txt");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String finalData = quizName.getText() + ";" + quizPassword.getText() + ";" + maxPunkte + ";;";

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
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane, 600, 500, 652,180);
    }

    public void changeMaxPunkte(){
        String punkteStr = (String)maxPunkteTextField.getText();
        if(punkteStr.compareTo("") != 0){
            maxPunkte = Integer.parseInt(punkteStr);
        }
        changeVergebenPunkte();
    }

    public void changeVergebenPunkte(){
        int allcurrentPunkte = 0;
        for (int i = 0; i < allPoints.size(); i++){
            allcurrentPunkte += allPoints.get(i);
        }
        displayVergebenePunkte.setText("Vergebene Punkte: " + allcurrentPunkte + "/" + maxPunkte);
    }

    public void goBack(){
        LoadScreenAnimation("LehrerSelection.fxml");
    }

    public int getFragePunkte(){
        String punkteStr = punktePerFrageTextField.getText();
        int punkte = 0;
        if(punkteStr.compareTo("") != 0){
            punkte = Integer.parseInt(punkteStr);
        }else{
            punkte =   DEAFAULT_FRAGE_PUNKTE;
        }
        return punkte;

    }

}
