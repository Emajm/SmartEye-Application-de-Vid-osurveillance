package smarteye;

import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.control.ProgressIndicator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.TilePane;



import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;

import smarteye.DetectionVisage;
import smarteye.Base2Donnees;
import smarteye.Communication;

import static java.lang.Thread.sleep;

public class HomeController {



	public String filePath="./faces";



	@FXML
	private Button startCam;
	@FXML
	private Button stopBtn;
	@FXML
	private Button motionBtn;
	@FXML
	private Button saveBtn;
	@FXML
	private Button recogniseBtn;
	@FXML
	private Button stopRecBtn;
	@FXML
	private ImageView frame;


	@FXML
	private TitledPane dataPane;
	@FXML
	private TextField prenom;
	@FXML
	private TextField nom;
	@FXML
	private TextField code_image;
	@FXML
	private TextField tel;
	@FXML
	private TextField mail;
	@FXML
	private TextField age;
	@FXML
	public ListView<String> logList;
	@FXML
	public ListView<String> output;
	@FXML
	public ProgressIndicator pb;
	@FXML
	public Label savedLabel;
	@FXML
	public Label warning;
	@FXML
	public Label title;
	@FXML
	public TilePane tile;



	DetectionVisage faceDetect = new DetectionVisage();
	Enregistrement enr = new Enregistrement();
	private Thread enrrThread;



	Base2Donnees dbase = new Base2Donnees();


	ArrayList<String> user = new ArrayList<String>();
	ImageView imageView1;

	public static ObservableList<String> event = FXCollections.observableArrayList();
	public static ObservableList<String> outEvent = FXCollections.observableArrayList();

	public boolean enabled = false;
	public boolean isDBready = false;



	public void putOnLog(String data) {

		Instant now = Instant.now();

		String logs = now.toString() + ":\n" + data;

		event.add(logs);

		logList.setItems(event);

	}

	@FXML
	protected void startCamera() throws SQLException {


		/////////////////////////

		enrrThread = new Thread(enr);
		enrrThread.start();

		faceDetect.init();
		//enr.init();
		faceDetect.setFrame(frame);
		faceDetect.start();
		////////////////////////

		if (!dbase.init()) {

			putOnLog("Erreur: echec de la connexion a la base de donnees");

		} else {
			isDBready = true;
			putOnLog("Succes: connexion a la base de donnees est reussie");
		}


		startCam.setVisible(false);

		stopBtn.setVisible(true);


		motionBtn.setDisable(false);
		saveBtn.setDisable(false);

		if (isDBready) {
			recogniseBtn.setDisable(false);
		}

		dataPane.setDisable(false);





		if (stopRecBtn.isDisable()) {
			stopRecBtn.setDisable(false);
		}
		//*******************************************************************************************


		tile.setPadding(new Insets(15, 15, 55, 15));
		tile.setHgap(30);

		//**********************************************************************************************


		String path = filePath;

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();


		for (final File file : listOfFiles) {

			imageView1 = createImageView(file);
			tile.getChildren().addAll(imageView1);
		}
		putOnLog(" La camera est demarree");

		//**********************************************************************************************
	}
	int count = 0;

	@FXML
	protected void faceRecognise() {


		faceDetect.setIsRecFace(true);


		recogniseBtn.setText("Obtenir les donnees de visage");


		user = faceDetect.getOutput();

		if (count > 0) {


			String t = "________ Informations de : " + user.get(1) + " " + user.get(2) + " ________";

			outEvent.add(t);

			String n1 = "Prenom \t\t:\t" + user.get(1);

			outEvent.add(n1);

			output.setItems(outEvent);

			String n2 = "Nom \t\t:\t" + user.get(2);

			outEvent.add(n2);

			output.setItems(outEvent);

			String fc = "Code visage \t\t:\t" + user.get(0);

			outEvent.add(fc);

			output.setItems(outEvent);

			String r = "Tel \t\t\t:\t+212" + user.get(3);

			outEvent.add(r);

			output.setItems(outEvent);

			String a = "Age \t\t\t\t:\t" + user.get(4);

			outEvent.add(a);

			output.setItems(outEvent);
			String s = "Mail \t\t\t:\t" + user.get(5);

			outEvent.add(s);

			output.setItems(outEvent);

		}

		count++;

		putOnLog("Reconnaissance faciale activee");

		stopRecBtn.setDisable(false);

	}

	@FXML
	protected void stopRecognise() {

		faceDetect.setIsRecFace(false);
		faceDetect.clearOutput();

		this.user.clear();

		recogniseBtn.setText("Reconnaître le visage");

		stopRecBtn.setDisable(true);

		putOnLog("Reconnaissance faciale desactivee");

	}

	@FXML
	protected void startMotion() {

		faceDetect.setMotion(true);
		putOnLog("Detecteur de mouvement active");

	}

	@FXML
	protected void saveFace() throws SQLException {


		if (prenom.getText().trim().isEmpty() || tel.getText().trim().isEmpty() || code_image.getText().trim().isEmpty()) {

			new Thread(() -> {

				try {
					warning.setVisible(true);

					Thread.sleep(2000);

					warning.setVisible(false);

				} catch (InterruptedException ex) {
				}

			}).start();

		} else {

			pb.setVisible(true);

			savedLabel.setVisible(true);

			new Thread(() -> {

				try {

					faceDetect.setPrenom(prenom.getText());

					faceDetect.setPrenom(prenom.getText());
					faceDetect.setNom(nom.getText());
					faceDetect.setAge(Integer.parseInt(age.getText()));
					faceDetect.setCode_image(Integer.parseInt(code_image.getText()));
					faceDetect.setMail(mail.getText());
					faceDetect.setTel(tel.getText());

					dbase.setPrenom(prenom.getText());
					dbase.setNom(nom.getText());
					dbase.setAge(Integer.parseInt(age.getText()));
					dbase.setCode_image(Integer.parseInt(code_image.getText()));
					dbase.setMail(mail.getText());
					dbase.setTel(tel.getText());

					dbase.insert();

					javafx.application.Platform.runLater(new Runnable(){

						@Override
						 public void run() {
							pb.setProgress(100);
						 }
						 });




					savedLabel.setVisible(true);
					Thread.sleep(2000);

					javafx.application.Platform.runLater(new Runnable(){

						@Override
						 public void run() {
							pb.setVisible(false);
						 }
						 });







					javafx.application.Platform.runLater(new Runnable(){

						@Override
						 public void run() {
					 savedLabel.setVisible(false);
						 }
						 });

				} catch (InterruptedException ex) {
				}

			}).start();

			faceDetect.setSaveFace(true);

		}

	}

	@FXML
	protected void stopCam() throws SQLException {

		faceDetect.stop();

		startCam.setVisible(true);
		stopBtn.setVisible(false);

		/* this.saveFace=true; */

		putOnLog("La camera est arrêtee");

		recogniseBtn.setDisable(true);
		saveBtn.setDisable(true);
		dataPane.setDisable(true);
		stopRecBtn.setDisable(true);

		enr.stop();



		dbase.db_close();
		putOnLog("Connexion a la base de donnees fermee");
		isDBready=false;
	}















	private ImageView createImageView(final File imageFile) {

		try {
			final Image img = new Image(new FileInputStream(imageFile), 120, 0, true, true);
			imageView1 = new ImageView(img);

			imageView1.setStyle("-fx-background-color: BLACK");
			imageView1.setFitHeight(120);

			imageView1.setPreserveRatio(true);
			imageView1.setSmooth(true);
			imageView1.setCache(true);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return imageView1;
	}


	@FXML
	    private void About(javafx.event.ActionEvent e)throws IOException
    {


        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("About.fxml"));
        primaryStage.setTitle("SmartEye | A propos");
		primaryStage.getIcons().add(new Image("img/logo.png"));
        Scene scene = new Scene(root,600,402);
        //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
