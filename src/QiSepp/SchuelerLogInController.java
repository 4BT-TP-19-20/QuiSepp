package QiSepp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SchuelerLogInController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField nameTextField;  //To enter the name of the student
    @FXML
    TextField nachnameTextField; //To enter the nachname of the student
    @FXML
    TextField quizPassoworTextField; //to enter the password of the quiz that the student wants to make
    @FXML
    Button schuelerBtn, lehrerBtn, quizStartenBtn;
    @FXML
    Label nameLabel, nachnameLabel;
    @FXML
    TextField serverIP;     //the student has to type in the IP that he receives from the teacher to connect to the server

    private String name;
    private String nachname;
    public Client schüler = new Client();
    private String quiz;

    public void starteQuiz() throws IOException, InterruptedException {
        name = nameTextField.getText();
        nachname = nachnameTextField.getText();
        //Client starts when a student logs in
        StarteClient(serverIP.getText());
    }


    public void changeToLehrerLogIn(){
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("LehrerLogIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);
    }

    //Client wird gestartet und sendet ip und namen an den Serever
    public void StarteClient(String ip) throws IOException, InterruptedException {
        schüler.start(ip);
        schüler.send(name);
        Thread.sleep(1000);
        schüler.send(nachname);
        Thread.sleep(2000);

        SendQuizPasswordToServer();
    }

    //Schüler sendet quizpassword an Server und erhält dann das Quiz zugeschickt
    public void SendQuizPasswordToServer() throws IOException {
        String passwort = quizPassoworTextField.getText();
        schüler.send(passwort);
        quiz=schüler.read();
        System.out.println("Quiz: " + quiz);
        StarteErhaltenesQuiz(quiz);
    }

    //Soblald das Quiz vom server zurückgesendet wird es gestartet und der Schüler kann es ausfüllen
    public void StarteErhaltenesQuiz(String quiz){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent newRoot = null;
        try {
            newRoot = newRoot = fxmlLoader.load(getClass().getResource("DisplayQuizz.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //DisplayQuizzController displayQuizzController=new DisplayQuizzController(schüler);
        DisplayQuizzController displayQuizzController = (DisplayQuizzController) fxmlLoader.getController();
        displayQuizzController.setSchüler(schüler, quiz);
        //displayQuizzController.receiveQuiz(quiz, false, false);
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane, 900, 900, 500, 70);
    }

}
