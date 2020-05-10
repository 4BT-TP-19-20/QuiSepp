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
import javafx.scene.text.Font;

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
    Client schüler;

    //*properties to display the quiz*//
    //if true (only for teacher): it gets created a button to edit the quiz;
    //if false (only students): the student can send the result back to server and view quiz
    boolean canEditQuiz = false;

    //if false the student can answer the question and the input from checkboxes gets stored
    //if true the user can view the right answers to the question but not edit or change something
    boolean canViewCorrectAnswers = false;
    int currFrage = 1;

    //loads previous question
    public void zurueck(){
        if(currFrage > 1){
            currFrage--;
            ClearDisplayingQuestion();
            displayCurrentFrage();
        }
    }

    //loads next question
    public void naechste(){
        if(currFrage < allQuestions.length){
            currFrage++;
            ClearDisplayingQuestion();
            displayCurrentFrage();
        }
    }

    //*only if canViewCorrectAnswers == false && canEditQuiz == false*//
    //gets called to store the answers when the student makes the quiz
    public void CheckBoxChanged(int which, boolean isSelected, int pos){
        //data is the current string where all answers get stored
        String[] data = allAnswersPerQuestion[pos].split(";");
        if(isSelected == true) {
            data[which] = "true";
        }else {
            data[which] = "false";
        }
        //replaces the string with the input of the student
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

    //gets called:
    // -when the teacher loads an existin quiz
    // -when the student receives a quiz to solve
    public void receiveQuiz(String originalQuiz, boolean canEditQuiz, boolean canViewCorrectAnswers){
        //set Properties
        this.displayedQuiz = originalQuiz;
        this.canEditQuiz = canEditQuiz;
        this.canViewCorrectAnswers = canViewCorrectAnswers;
        SetUpNewQuiz();
    }

    //gets called:
    // -when the student has filled in the quiz and has pressed send quiz
    public void receiveQuiz(String originalQuiz, String userQuiz, boolean canEditQuiz, boolean canViewCorrectAnswers){
        //set Properties
        this.displayedQuiz = userQuiz;
        this.originalQuiz = originalQuiz;
        this.canEditQuiz = canEditQuiz;
        this.canViewCorrectAnswers = canViewCorrectAnswers;
        SetUpNewQuiz();
    }


    //loads the new quiz
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


    //loads the new quiz
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

            if(!canViewCorrectAnswers && canEditQuiz == false) {
                String[] str2 = allAnswersPerQuestion[i - 1].split(";");
                allAnswersPerQuestion[i - 1] = "";
                for (int j = 0; j < str2.length; j += 2) {
                    str2[j + 1] = "false";
                    allAnswersPerQuestion[i - 1] += str2[j] + ";" + str2[j + 1] + ";";
                }
            }
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


    //*only if canViewResults == false*//
    //creates a button:
    // -if canEditquiz == true the teacher can edit the quiz and it opens a new window when button pressed
    // -if canEditquiz == false the student can send and view the results by pressing button
    public void SetButton(String str){
        //creates a button at the pos of the holder of  the fxmlfile
        Button editButton = new Button();
        editButton.setPrefHeight(buttonHolder.getPrefHeight());
        editButton.setPrefWidth(buttonHolder.getPrefWidth());
        editButton.setLayoutX(buttonHolder.getLayoutX());
        editButton.setLayoutY(buttonHolder.getLayoutY());
        anchorPane.getChildren().add(editButton);

        //set The button to edit the quiz
        if(canEditQuiz){
            editButton.setText("Quiz\nBearbeiten");
            //when button pressed it loads the quiz to neuesQuizErstellenController and you can edit it
            editButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    Parent newRoot = null;
                    try {
                        newRoot = fxmlLoader.load(getClass().getResource("NeuesQuizErstellen.fxml").openStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    NeuesQuizErstellenController neuesQuizErstellenController = (NeuesQuizErstellenController) fxmlLoader.getController();
                    neuesQuizErstellenController.LoadExistingQuiz(str);
                    SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);

                }
            });

            //set the button to send the quiz and view it
        }else if(!canViewCorrectAnswers){
            editButton.setText("Quiz\nAbgeben");
            //when pressed sends back the results to server and you can view the result of the quiz
            editButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PackToFinalString();
                    try {
                        SendBackQuizResults(finalQuiz);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    Parent newRoot = null;
                    try {
                        newRoot = fxmlLoader.load(getClass().getResource("DisplayQuizz.fxml").openStream());
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

    public void setSchüler(Client student, String quiz){
        schüler=student;
        receiveQuiz(quiz, false, false);
    }


    //sends the quiz that the student filled in back to the teacher
    public void SendBackQuizResults(String quiz) throws IOException {
        schüler.send(quiz);
    }


    public void ClearDisplayingQuestion(){
        containerAnswers.getChildren().clear();
        containerCheckboxes.getChildren().clear();
    }


    //creates a final string form all current stored questions, answers and points
    public void PackToFinalString(){
        finalQuiz = "";
        finalQuiz += strQuizName + ";" + strQuizPassword + ";" + intMaxPoints + ";;";
        for (int i = 0; i < allQuestions.length; i++){
            finalQuiz += allQuestions[i] + ";" + allPointsPerQuestion[i] + ";" +allAnswersPerQuestion[i] + ";";
        }
    }


    //loads the question, answers and points form the string and displays it
    //the checkbox gets only ticked:
    // -if canViewResults == false: checkbox bearbeiten == true; checkbox displays questions == false
    // -if canEdit == true || canViewResults == true: checkbox bearbeiten == false; checkbox displays questions == true
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
            checkBox.setFont(new Font(16));
            checkBox.setPrefHeight(25);
            label.setFont(new Font(16));
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
                //adds a listener to every new Checkbox that calls a funktion to store the userInput
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        CheckBoxChanged(finalI + 1, checkBox.isSelected(), currFrage - 1);
                    }
                });
            }
            //displays the incorrect answers from a finished quiz (only student)
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


    //*only if canViewResults == true && canEdit == false*//
    //calculates the sum of all points to display the end point of the student
    public void CalculateCorrectPoints(){
        String[] data = originalQuiz.split(";;");
        float points = 0;
        for (int i = 1; i < data.length; i++){
            points += CalculateCorrectPointPerQuestion(i);
        }
        displayQuizPunkte.setText("Quiz Punkte: " + df2.format(points) + "/" + intMaxPoints);
        receivedPoints = points;
    }


    //*only if canViewResults == true && canEdit == false*//
    //calculates the points the student gets from each question
    //example:
    // -maxpoint for question = 9
    // -amount of answers = 3
    // -answer1 = false; answer2 = true; answer 3 = false;
    // -for each correct answer he gets: 9 (maxpoints for questions) / 3 (amount of answers) = 3 points
    // -if the student would answer: -answer1 = true; answer2 = true; answer 3 = false
    //  he would get 6/9 points
    public float CalculateCorrectPointPerQuestion(int currFrage){
        String[] data = originalQuiz.split(";;");
        int points = allPointsPerQuestion[currFrage - 1];
        int trueAnswers = 0;
        int allAnswers = allAnswersPerQuestion[currFrage - 1].split(";").length;
        float finalPoints = (float)points / ((float)allAnswers/2);
        for (int i = 0; i < allAnswers; i += 2){
            if(CheckForfalseAnswers(i, currFrage) == false){
                trueAnswers++;
            }
        }
        finalPoints *= trueAnswers;
        displayPunktePerFrage.setText("Punkte: " + df2.format(finalPoints) + "/" + allPointsPerQuestion[currFrage - 1]);
        return finalPoints;
    }


    //*only if canViewResults == true && canEdit == false*//
    //reads the string with all answers and checks if the original quiz would be the same as the user filled in quiz
    public boolean CheckForfalseAnswers(int pos, int currFrage){
        String[] data = originalQuiz.split(";;");
        String[] allAnswersdata = allAnswersPerQuestion[currFrage - 1].split(";");
        if(allAnswersdata[pos + 1].compareTo(data[currFrage].split(";")[pos + 3]) != 0){
            return true;
        }
        return false;
    }

    public void CalculateVorgeschNote(){

    }

}
