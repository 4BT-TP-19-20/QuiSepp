package QiSepp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    TextField quizPasswordTextField; //to enter the password of the quiz that the student wants to make
    @FXML
    Button schuelerBtn, lehrerBtn, quizStartenBtn;
    @FXML
    Label nameLabel, nachnameLabel, keyAlert, ipAlert, namenAlert;
    @FXML
    TextField serverIP;     //the student has to type in the IP that he receives from the teacher to connect to the server
    @FXML
    ImageView QuiSeppLogoIV;

    private String name;
    private String nachname;
    public Client schüler = new Client();
    private String quiz;
    public String logoPath = "Resources\\QuiSeppLogo.png";
    //Image logoImage = new Image(logoPath);

    public void initialize(){

        //QuiSeppLogoIV.setImage(logoImage);
    }

    public void starteQuiz() throws IOException, InterruptedException {
        if(nameTextField.getText().equals("") || nameTextField.getText().equals(" ")){
            namenAlert.setText("Bitte Name angeben!");
            return;
        }

        if(nachnameTextField.getText().equals("") || nachnameTextField.getText().equals(" ")){
            namenAlert.setText("Bitte Name angeben!");
            return;
        }else{
            namenAlert.setText("");
        }

        if(serverIP.getText().equals("") || serverIP.getText().equals(" ")){
            ipAlert.setText("Bitte IP angeben!");
            return;
        }else{
            serverIP.setText("");
        }
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

    //Client wird gestartet und sendet ip und namen an den Server
    public void StarteClient(String ip) throws IOException, InterruptedException {
        schüler.start(ip);
        schüler.send(name);
        Thread.sleep(1000);
        schüler.send(nachname);
        Thread.sleep(2000);

        SendQuizPasswordToServer();
    }

    //Schüler sendet quizpassword an Server und erhält dann das Quiz zugeschickt
    public void SendQuizPasswordToServer() throws IOException, InterruptedException {
        String passwort = quizPasswordTextField.getText();
        schüler.send(passwort);
        quiz=schüler.read();
        if(quiz.equals("Falsch")){
            keyAlert.setText("Falscher Key!");
            starteQuiz();
        }
        System.out.println("Quiz: " + quiz);
        StarteErhaltenesQuiz(quiz);
    }

    //Soblald das Quiz vom server zurückgesendet wird es gestartet und der Schüler kann es ausfüllen
    public void StarteErhaltenesQuiz(String quiz){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent newRoot = null;
        try {
            newRoot = fxmlLoader.load(getClass().getResource("DisplayQuizz.fxml").openStream());
            newRoot.getStylesheets().add(getClass().getResource("DisplayQuizzStyleSheet.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
        DisplayQuizzController displayQuizzController = (DisplayQuizzController) fxmlLoader.getController();
        displayQuizzController.setSchüler(schüler, quiz);
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane, 900, 900, 500, 70);
    }


}
