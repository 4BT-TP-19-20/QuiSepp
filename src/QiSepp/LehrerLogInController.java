package QiSepp;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class LehrerLogInController {

    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField benuzterTextField;
    @FXML
    TextField passwortTextField;

    private Scene currentScene;
    private String lehrerBenutzername = "admin";
    private String lehrerPasswort = "admin123";
    public Client lehrer=new Client();

    public void anmelden(){
        if(benuzterTextField.getText().compareTo(lehrerBenutzername) != 0){
            benuzterTextField.setStyle("-fx-text-fill: red");
            return;
        }

        if(passwortTextField.getText().compareTo(lehrerPasswort) != 0){
            passwortTextField.setStyle("-fx-text-fill: red");
            return;
        }
        StarteServer();
        lehrer.start("localhost");

        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("LehrerSelection.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);

    }

    public void StarteServer(){
        ServerMain serverMain = new ServerMain();
        serverMain.start();
    }

    public void resetBenetzernameField(){
        benuzterTextField.setStyle("-fx-text-fill: black");
    }

    public void resetPasswortField(){
        passwortTextField.setStyle("-fx-text-fill: black");
    }

    public void changeToSchuelerLogIn(){
        Parent newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("SchuelerLogIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);
    }



}
