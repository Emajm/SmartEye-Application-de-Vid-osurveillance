package smarteye;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
//import utils.ConnectionUtil;
import smarteye.Communication;

public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == btnSignin) {

            if (logIn().equals("Success")) {
                try {

   
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();

                    Stage primaryStage = new Stage();
                    BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Home.fxml"));
                    Scene scene = new Scene(root,1350,720);
                    scene.getStylesheets().add(getClass().getResource("home.css").toExternalForm());
                    primaryStage.getIcons().add(new Image("img/logo.png"));
                    primaryStage.setTitle("SmartEye | Bienvenue ");
                    primaryStage.setScene(scene);
                    primaryStage.show();




                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    public LoginController() {}


    private String logIn() {
        String status = "Success";
        String user = txtUsername.getText();
        String password = txtPassword.getText();
        if(user.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Identifiants vides");
            status = "Error";
        } else {
            try {
                if (!user.contentEquals("user")  || !password.contentEquals("choukri") ) {
                    setLblError(Color.TOMATO, "Entrez le bon nom d'utilisateur / mot de passe");
                    status = "Error";
                } else {
                    setLblError(Color.GREEN, "Connexion réussie..Redirection ..");
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        
        return status;
    }
    
    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }
}
