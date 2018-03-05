import Exceptions.OSNotSupportedError;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by olivier on 16-10-08.
 */
public class Main extends Application {


    public static void main(String[] args) throws IOException, OSNotSupportedError {

        launch(args);

    }

    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"), loadDefaultLanguage());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

    private static ResourceBundle loadDefaultLanguage() {
        Locale locale = new Locale("en", "CA");
        return ResourceBundle.getBundle("english", locale);
    }


}