package smarteye;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.StageStyle;


public class Main extends Application {


	@Override
	public void start(Stage stage) {
		
		try {
    Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
   // BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Login.fxml"));

        stage.initStyle(StageStyle.DECORATED);
        stage.setMaximized(false);
		stage.getIcons().add(new Image("img/logo.png"));
		stage.setTitle("SmartEye");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);


	}
}
