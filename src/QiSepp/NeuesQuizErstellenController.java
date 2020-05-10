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
    @FXML Label feedbackLabel;

    ArrayList<TextField> allCurrentAnswers = new ArrayList<>();
    ArrayList<CheckBox> allCurrentAnswerCheckBox = new ArrayList<>();
    ArrayList<Integer> allPoints = new ArrayList<>();
    ArrayList<String> allData = new ArrayList<>();

    int numFrage = 1;
    int currentFrage = 1;
    int maxPunkte = 0;

    int MAX_ANSWERS = 15;
    int DEAFAULT_FRAGE_PUNKTE = 1;

    //adds a checkbox and a Textfield to the quiz-question
    public void addAnswer() {
        if (allCurrentAnswers.size() < MAX_ANSWERS){
            TextField textField = new TextField();
            CheckBox checkBox = new CheckBox();
            containerAllCheckBox.getChildren().add(checkBox);
            containerAllAnswers.getChildren().add(textField);
            textField.setPromptText("Geben Sie eine Antwort ein");
            checkBox.setText("");
            allCurrentAnswers.add(textField);
            allCurrentAnswerCheckBox.add(checkBox);
            checkBox.setPrefHeight(25);
        }
    }

    //removes the last answer
    public void removeAnswer(){
        if(allCurrentAnswers.size() > 0){
            containerAllCheckBox.getChildren().remove(allCurrentAnswerCheckBox.get(allCurrentAnswerCheckBox.size() - 1));
            containerAllAnswers.getChildren().remove(allCurrentAnswers.get(allCurrentAnswers.size() - 1));
            allCurrentAnswers.remove(allCurrentAnswers.size() - 1);
            allCurrentAnswerCheckBox.remove(allCurrentAnswerCheckBox.size() - 1);
        }
    }

    //collects the question, the answers, the checkbox and the points of the question and stores it at the end of the arraylist
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

    //collects the same data as the other funktion but replaces the string in the arraylist (if you need to improve some older questions)
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

    //gets called everytime you press press next question or previous question and stroes the whole question
    public void frageHinzufuegen(){
        //gets only called if the question text isnt empty
        if (frage.getText().compareTo("") != 0) {
            //you can only add a empty question at the end of the quiz
            if (currentFrage == numFrage && allData.size() < numFrage) {
                //stores the question, the answers, the checkbox and the points
                collectData();
                numFrage++;
                currentFrage = numFrage;
                disNumFrage.setText("Frage: " + numFrage);
                //adds an empty question
                Clear();
                punktePerFrageTextField.setText("");
                frage.setText("");
            } else {
                //if the user is not at the end ot the quiz (to add a empty question) he can edit a existing question that gets auto stored
                collectData(currentFrage - 1);
            }
        }

    }

    //loads a quiz when a teacher wants to edit it
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

    //goes to the next question (you can only have one empty question at the end)
    public void forward(){
        frageHinzufuegen();
        currentFrage++;
        //if you are at the end of all questions it adds an empty question
        if(currentFrage == numFrage) {
            disNumFrage.setText("Frage: " + numFrage);
            Clear();
            punktePerFrageTextField.setText("");
            frage.setText("");
            //if you are not at the end of all questions it loads the allready filled in question
        }else if(currentFrage < numFrage) {
            LoadQuestion();
            //if you press forward, but you are at the end of the questions
        }else{
            currentFrage--;
        }
    }

    //goes to the previous question
    public void back(){
        frageHinzufuegen();
        if(currentFrage > 1) {
            currentFrage--;
            LoadQuestion();
        }

    }

    //if you go back to already existing questions they get displayed and you can edit them
    public void LoadQuestion(){
        //cleares the old question so that it is like an empty question
        Clear();
        disNumFrage.setText("Frage: " + currentFrage);
        //data stores the questions, the answers, the point and the checkbox input of the question the user wants to display
        String[] data = allData.get(currentFrage - 1).split(";");
        //display the question
        frage.setText(data[0]);
        //display the points of the question
        punktePerFrageTextField.setText(data[1]);
        //loops through all answers and displays them
        for(int i = 2; i < data.length; i += 2){
            TextField textField = new TextField();
            CheckBox checkBox = new CheckBox();
            containerAllCheckBox.getChildren().add(checkBox);
            containerAllAnswers.getChildren().add(textField);
            allCurrentAnswers.add(textField);
            allCurrentAnswerCheckBox.add(checkBox);
            checkBox.setPrefHeight(25);
            checkBox.setText("");
            //ticks the checkbox if true
            if(data[i + 1].compareTo("true") == 0){
                checkBox.setSelected(true);
            }else {
                checkBox.setSelected(false);
            }
            //display question
            textField.setText(data[i]);
        }
    }

    //deletes all the stuff of the current question like textfield and checkboxes
    public void Clear(){
        containerAllAnswers.getChildren().clear();
        containerAllCheckBox.getChildren().clear();
        allCurrentAnswers.clear();
        allCurrentAnswerCheckBox.clear();
    }

    //stores the whole quiz in a text file
    public void quizSpeichern(){
        //adds the last question to the arraylist of questions
        frageHinzufuegen();
        //receive the path where to store the quizzes
        File f = new File("Quizze\\" + quizName.getText() + ".txt");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //creates the final string where the whole quiz gets stored
        String finalData = quizName.getText() + ";" + quizPassword.getText() + ";" + maxPunkte + ";;";

        for(int i = 0; i < allData.size(); i++){
            finalData += allData.get(i) + ";";
        }
        //wirtes the string into the textfile
        try (FileWriter writer = new FileWriter(f.getPath());
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(finalData);
            feedbackLabel.setText("Speichern erfolgreich!");
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

    //gets auto called if the user types a new nuber into textfield
    public void changeMaxPunkte(){
        String punkteStr = (String)maxPunkteTextField.getText();
        if(punkteStr.compareTo("") != 0){
            maxPunkte = Integer.parseInt(punkteStr);
        }
        changeVergebenPunkte();
    }

    //gets auto called if the user types a new nuber into textfield next to the question
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

    //returns the point from the textfield next to the question if the user doesnt input a number the deafault gets stored
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
