package matchscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Starter-Class for JavaFX-Application.
 *
 */
public class MatchSchedulerApplication extends Application {

    private static Stage stage;

    private void setStage(Stage stage){
        MatchSchedulerApplication.stage = stage;
    }
    static public Stage getStage(){
        return MatchSchedulerApplication.stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(MatchSchedulerApplication.class.getResource("scheduler-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("league schedule generator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}