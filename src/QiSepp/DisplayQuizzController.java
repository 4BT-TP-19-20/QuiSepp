package QiSepp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.text.DecimalFormat;

public class DisplayQuizzController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    @FXML
    Label displayFrageNum, displayFrage, displayQuizName, displayPunktePerFrage, displayQuizPassword, displayQuizPunkte;
    @FXML
    VBox containerCheckboxes, containerAnswers;
    @FXML
    Pane buttonHolder;

    //store the whole Quiz from the received string
    String displayedQuiz = "";
    String originalQuiz = "";
    String finalQuiz =  "";
    String[] allQuestions;
    String[] allAnswersPerQuestion;
    int[] allPointsPerQuestion;
    String strQuizName = null;
    String strQuizPassword = null;
    int intMaxPoints = 0;
    float receivedPoints = 0;

    //properties to display the quiz
    boolean canEditQuiz = false; //if the user can press the button to go to the edit screen (only for teacher) or if the user can send the result back (only for students)
    boolean canViewCorrectAnswers = false; //if the user can see the results of the quiz
    int currFrage = 1;

    public void zurueck(){
        if(currFrage > 1){
            currFrage--;
            ClearDisplayingQuestion();
            displayCurrentFrage();
        }
    }

    public void naechste(){
        if(currFrage < allQuestions.length){
            currFrage++;
            ClearDisplayingQuestion();
            displayCurrentFrage();
        }
    }

    public void CheckBoxChanged(int which, boolean isSelected, int pos){
        String[] data = allAnswersPerQuestion[pos].split(";");
        if(isSelected == true) {
            data[which] = "true";
        }else {
            data[which] = "false";
        }
        allAnswersPerQuestion[pos] = "";
        for (int i = 0; i < data.length; i++){
            allAnswersPerQuestion[pos] += data[i] + ";";
        }
    }

    public void goBack() {
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("SchuelerLogIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane, 600, 500, 652,180);
    }

    public void receiveQuiz(String originalQuiz, boolean canEditQuiz, boolean canViewCorrectAnswers){
        //set Properties
        this.displayedQuiz = originalQuiz;
        this.canEditQuiz = canEditQuiz;
        this.canViewCorrectAnswers = canViewCorrectAnswers;
        SetUpNewQuiz();
    }

    public void receiveQuiz(String originalQuiz, String userQuiz, boolean canEditQuiz, boolean canViewCorrectAnswers){
        //set Properties
        this.displayedQuiz = userQuiz;
        this.originalQuiz = originalQuiz;
        this.canEditQuiz = canEditQuiz;
        this.canViewCorrectAnswers = canViewCorrectAnswers;
        SetUpNewQuiz();
    }


    public void SetGenaralValues(String[] data){
        //set the Quiz name
        strQuizName = data[0].split(";")[0];
        displayQuizName.setText(strQuizName);

        //set the Quiz Password
        strQuizPassword = data[0].split(";")[1];
        displayQuizPassword.setText("Zugang: " + strQuizPassword);

        //set the size of the question, answer and points
        allQuestions = new String[data.length - 1];
        allAnswersPerQuestion = new String[data.length - 1];
        allPointsPerQuestion = new int[data.length - 1];

    }


    public void SetUpNewQuiz() {
        String[] data = displayedQuiz.split(";;");
        SetGenaralValues(data);

        //store all question, answer and points
        for (int i = 1; i < data.length; i ++){
            allQuestions[i - 1] = data[i].split(";")[0];
            allPointsPerQuestion[i - 1] =  Integer.parseInt(data[i].split(";")[1]);
            allAnswersPerQuestion[i -1] = "";
            for (int j = 2; j < data[i].split(";").length; j++){
                allAnswersPerQuestion[i - 1] += data[i].split(";")[j] + ";";
            }

            if(!canViewCorrectAnswers) {
                String[] str2 = allAnswersPerQuestion[i - 1].split(";");
                allAnswersPerQuestion[i - 1] = "";
                for (int j = 0; j < str2.length; j += 2) {
                    str2[j + 1] = "false";
                    allAnswersPerQuestion[i - 1] += str2[j] + ";" + str2[j + 1] + ";";
                }
            }
            System.out.println(allAnswersPerQuestion[i - 1]);
            System.out.println(allQuestions[i - 1]);
            System.out.println(allPointsPerQuestion[i - 1]);
        }
        if(!canViewCorrectAnswers) {
            SetButton(displayedQuiz);
        }
        displayCurrentFrage();

        //set the quiz MaxPoints
        intMaxPoints = Integer.parseInt(data[0].split(";")[2]);
        if(canViewCorrectAnswers){
            CalculateCorrectPoints();
        }else {
            displayQuizPunkte.setText("Quiz Punkte: " + intMaxPoints);
        }
    }

    public void SetButton(String str){
        Button editButton = new Button();
        editButton.setPrefHeight(buttonHolder.getPrefHeight());
        editButton.setPrefWidth(buttonHolder.getPrefWidth());
        editButton.setLayoutX(buttonHolder.getLayoutX());
        editButton.setLayoutY(buttonHolder.getLayoutY());
        anchorPane.getChildren().add(editButton);

        //set The button to edit/send the quiz
        if(canEditQuiz){
            editButton.setText("Quiz\nBearbeiten");
            editButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    Parent newRoot = null;
                    try {
                        newRoot = newRoot = fxmlLoader.load(getClass().getResource("NeuesQuizErstellen.fxml").openStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    NeuesQuizErstellenController neuesQuizErstellenController = (NeuesQuizErstellenController) fxmlLoader.getController();
                    neuesQuizErstellenController.LoadExistingQuiz(str);
                    SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);

                }
            });
        }else if(!canViewCorrectAnswers){
            editButton.setText("Quiz\nAbgeben");
            editButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PackToFinalString();
                    System.out.println(finalQuiz);
                    SendBackQuizResults(finalQuiz);

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    Parent newRoot = null;
                    try {
                        newRoot = newRoot = fxmlLoader.load(getClass().getResource("DisplayQuizz.fxml").openStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DisplayQuizzController displayQuizzController = (DisplayQuizzController) fxmlLoader.getController();
                    displayQuizzController.receiveQuiz(str, finalQuiz,false, true);
                    SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);

                }
            });
        }
    }


    public void SendBackQuizResults(String quiz){

    }


    public void ClearDisplayingQuestion(){
        containerAnswers.getChildren().clear();
        containerCheckboxes.getChildren().clear();
    }

    public void PackToFinalString(){
        finalQuiz = "";
        finalQuiz += strQuizName + ";" + strQuizPassword + ";" + intMaxPoints + ";;";
        for (int i = 0; i < allQuestions.length; i++){
            finalQuiz += allQuestions[i] + ";" + allPointsPerQuestion[i] + ";" +allAnswersPerQuestion[i] + ";";
        }
    }

    public void displayCurrentFrage() {
        //display question
        displayFrage.setText(allQuestions[currFrage - 1]);

        //display all answers of the question
        String[] allCurrentAnswers = allAnswersPerQuestion[currFrage - 1].split(";");
        for(int i = 0; i < allCurrentAnswers.length; i += 2){
            Label label = new Label();
            CheckBox checkBox = new CheckBox();

            containerCheckboxes.getChildren().add(checkBox);
            containerAnswers.getChildren().add(label);

            checkBox.setText("");
            checkBox.setPrefHeight(25);
            label.setText(allCurrentAnswers[i]);
            label.setPrefHeight(25);
            if(canEditQuiz || canViewCorrectAnswers) {
                if (allCurrentAnswers[i + 1].compareTo("true") == 0) {
                    checkBox.setSelected(true);
                    checkBox.setDisable(true);
                } else {
                    checkBox.setSelected(false);
                    checkBox.setDisable(true);
                }
            }else{
                if (allCurrentAnswers[i + 1].compareTo("true") == 0) {
                    checkBox.setSelected(true);
                } else {
                    checkBox.setSelected(false);
                }
                int finalI = i;
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        CheckBoxChanged(finalI + 1, checkBox.isSelected(), currFrage - 1);
                    }
                });
            }
            if(canViewCorrectAnswers){
                if(CheckForfalseAnswers(i, currFrage) == true){
                    label.setStyle("-fx-background-color: RED");
                }
            }
        }

        //display points of the question
        if(canViewCorrectAnswers){
            CalculateCorrectPointPerQuestion(currFrage);
        }else {
            displayPunktePerFrage.setText("Punkte: " + allPointsPerQuestion[currFrage - 1]);
        }
        //display number of question
        displayFrageNum.setText("Frage: " + currFrage);

    }
    public static DecimalFormat df2 = new DecimalFormat("#.##");

    public void CalculateCorrectPoints(){
        String[] data = originalQuiz.split(";;");
        float points = 0;
        for (int i = 1; i < data.length; i++){
            points += CalculateCorrectPointPerQuestion(i);
        }
        displayQuizPunkte.setText("Quiz Punkte: " + df2.format(points) + "/" + intMaxPoints);
        receivedPoints = points;
    }

    public float CalculateCorrectPointPerQuestion(int currFrage){
        String[] data = originalQuiz.split(";;");
        int points = allPointsPerQuestion[currFrage - 1];
        int trueAnswers = 0;
        int allAnswers = allAnswersPerQuestion[currFrage - 1].split(";").length;
        float finalPoints = (float)points / ((float)allAnswers/2);
        System.out.println(allAnswers);
        for (int i = 0; i < allAnswers; i += 2){
            if(CheckForfalseAnswers(i, currFrage) == false){
                trueAnswers++;
            }
        }
        finalPoints *= trueAnswers;
        displayPunktePerFrage.setText("Punkte: " + df2.format(finalPoints) + "/" + allPointsPerQuestion[currFrage - 1]);
        return finalPoints;
    }

    public boolean CheckForfalseAnswers(int pos, int currFrage){
        String[] data = originalQuiz.split(";;");
        String[] allAnswersdata = allAnswersPerQuestion[currFrage - 1].split(";");
        if(allAnswersdata[pos + 1].compareTo(data[currFrage].split(";")[pos + 3]) != 0){
            System.out.println(allAnswersdata[pos]);
            return true;
        }
        return false;
    }

    public void CalculateVorgeschNote(){

    }

}
