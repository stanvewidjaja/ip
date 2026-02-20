package iris.ui;

import iris.Iris;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Iris iris = new Iris();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            scene.getStylesheets().add(
                    getClass().getResource("/view/styles.css").toExternalForm()
            );
            System.out.println(getClass().getResource("/view/styles.css"));
            stage.setScene(scene);
            stage.setTitle("Iris");
            MainWindow controller = fxmlLoader.getController();

            controller.setIris(iris);
            controller.showGreeting(iris.init());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

