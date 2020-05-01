package QiSepp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;

public class DisplayAllQuizzesController {

    @FXML
    VBox vboxForQuizzes;
    @FXML
    StackPane root;
    @FXML
    AnchorPane anchorPane;
    ArrayList<String> allData = new ArrayList<>();
    ArrayList<Button> allButtons = new ArrayList<>();

    //displays every quiz that is stored locally on the teachers pc and displays it with a button
    public void initialize(){
        String path = "Quizze\\";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(path + file.getName()));
                    String str = br.readLine();
                    String[] str2 = str.split(";");
                    allData.add(str);
                    Button button = new Button();
                    button.setText(str2[0]);
                    vboxForQuizzes.getChildren().add(button);
                    button.setPrefWidth(vboxForQuizzes.getPrefWidth());
                    allButtons.add(button);
                    //it the button gets pressed the DisplayQuiz Class receives the string from the text file
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            Parent newRoot = null;
                            try {
                                newRoot = newRoot = fxmlLoader.load(getClass().getResource("DisplayQuizz.fxml").openStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            DisplayQuizzController displayQuizzController = (DisplayQuizzController) fxmlLoader.getController();
                            displayQuizzController.receiveQuiz(str, true, false);
                            SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane, 900, 900, 500, 70);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void goBack(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent newRoot = null;
        try {
            newRoot = newRoot = fxmlLoader.load(getClass().getResource("LehrerSelection.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SceneLoader.LoadScreenAnimation(newRoot, root, anchorPane);

    }

}
