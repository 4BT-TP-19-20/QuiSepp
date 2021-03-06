package QiSepp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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
    @FXML
    PasswordField passwortField;
    @FXML
    Label alertLabel;

    private Scene currentScene;
    private String lehrerBenutzername = "admin";
    private String lehrerPasswort = "admin123";
    public Client lehrer=new Client();


    public void anmelden() throws IOException {
        String test=passwortField.getText();
        System.out.println(test);
        //checks the username and sets the text to red if it was incorrect
        if(benuzterTextField.getText().compareTo(lehrerBenutzername) != 0){
            alertLabel.setText("Benutzername oder Passwort falsch!");
            return;
        }

        //checks the password and sets the text to red if it was incorrect
        if(passwortField.getText().compareTo(lehrerPasswort) != 0){
            alertLabel.setText("Benutzername oder Passwort falsch!");
            return;
        }
        StarteServer();
        lehrer.start("localhost");
        lehrer.send("admin");

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
