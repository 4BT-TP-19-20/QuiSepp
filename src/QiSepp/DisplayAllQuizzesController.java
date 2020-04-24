package QiSepp;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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

    public void initialize(){
        String path = "D:\\Informatk\\Programme\\spiel\\src\\QuiSepp\\src\\QiSepp\\Quizzes";
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
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                LoadScreenAnimation("DisplayQuizz.fxml", str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    public void LoadScreenAnimation(String fxmlFile, String data) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent newRoot = newRoot = fxmlLoader.load(getClass().getResource(fxmlFile).openStream());

        DisplayQuizzController displayQuizzController = (DisplayQuizzController)fxmlLoader.getController();
        displayQuizzController.receiveQuiz(data, true, false);

        Scene scene = anchorPane.getScene();
        newRoot.translateXProperty().set(scene.getWidth());
        root.getChildren().add(newRoot);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(newRoot.translateXProperty(),0 , Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(t -> {
            root.getChildren().remove(anchorPane);
        });
        timeline.play();
    }
}
