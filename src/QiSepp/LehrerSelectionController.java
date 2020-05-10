package QiSepp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class LehrerSelectionController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;

    // -the teacher can create a empty quiz and store it in a file (if you want to reopen a existing quiz you have to use display all quiz
    public void btnNeuesQuiz(){
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("NeuesQuizErstellen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane, 900, 900, 500, 70);
    }

    // -displays all localy in a textfile stored quizzes
    // -if you are a teacher you can select the quiz so that it gets displayed
    //  and you get a button in the right upper corner to edit the quiz
    public void btnAlleQuiz(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent newRoot = null;
        try {
            newRoot = fxmlLoader.load(getClass().getResource("DisplayAllQuizzes.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        DisplayAllQuizzesController displayAllQuizzesController = fxmlLoader.getController();
        displayAllQuizzesController.display("Quizze\\", true, false);
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);
    }

    public void btnErhalteneQuiz(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent newRoot = null;
        try {
            newRoot = fxmlLoader.load(getClass().getResource("DisplayAllQuizzes.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        DisplayAllQuizzesController displayAllQuizzesController = fxmlLoader.getController();
        displayAllQuizzesController.display("QuizzeErhalten\\", true, false);
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
