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
    TextField quizPassoworTextField;
    @FXML
    Button schuelerBtn, lehrerBtn, quizStartenBtn;
    @FXML
    Label nameLabel, nachnameLabel;
    @FXML
    TextField serverIP;

    private Scene currentScene;
    private String name;
    private String nachname;
    public Client schüler=new Client();
    private String quiz;

    public void starteQuiz() throws IOException, InterruptedException {
        name = nameTextField.getText();
        nachname = nachnameTextField.getText();
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

    public void StarteClient(String ip) throws IOException, InterruptedException {
        schüler.start(ip);
        schüler.send(name);
        schüler.send(nachname);
        Thread.sleep(2000);

        SendQuizPasswordToServer();
    }

    public void SendQuizPasswordToServer() throws IOException {
        String passwort = quizPassoworTextField.getText();
        schüler.send(passwort);
        quiz=schüler.read();
        System.out.println("Quiz: " + quiz);
    }


}
