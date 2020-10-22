package smarteye;

import smarteye.ReconnaissanceVisage;
import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.FlyCapture2.ImageMetadata;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.helper.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import smarteye.Base2Donnees;
import smarteye.DetectionMouvement;

import smarteye.Communication;

public class DetectionVisage implements Runnable {

	Base2Donnees dbase = new Base2Donnees();
	ArrayList<String> user;

	ReconnaissanceVisage Vreconn = new ReconnaissanceVisage();
	DetectionMouvement detectionMv = new DetectionMouvement();
	OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
	Java2DFrameConverter paintConverter = new Java2DFrameConverter();
	ArrayList<String> output = new ArrayList<String>();

	@FXML
	public Label ll;
	private Exception exception = null;


	public String classiferName;
	public File classifierFile;


	public boolean saveFace = false;
	public boolean isRecFace = false;

	public boolean isMotion = false;

	private boolean stop = false;

	private CvHaarClassifierCascade classifier = null;

	private CvHaarClassifierCascade classifierSideFace = null;

	public CvMemStorage storage = null;
	private FrameGrabber grabber = null;
	private IplImage grabbedImage = null, temp, grayImage = null, smallImage = null;

	public ImageView frames;


	private CvSeq faces = null;





	int recogniseCode;
	public int code_image;
	public String tel;
	public int age;

	public String prenom; 
	public String nom; 
	public String mail; 
	public String name;

	public void init() {
		Vreconn.init();
		setClassifier("haar/haarcascade_frontalface_alt.xml");
		setClassifierSideFace("haar/haarcascade_profileface.xml");
	}

	public void start() {
		try {
			new Thread(this).start();
		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}
	}

	public void run() {
		try {
			try {
				grabber = OpenCVFrameGrabber.createDefault(0); 

				grabber.setImageWidth(700);
				grabber.setImageHeight(700);
				grabber.start();

				grabbedImage = grabberConverter.convert(grabber.grab());

				storage = CvMemStorage.create();
			} catch (Exception e) {
				if (grabber != null)
					grabber.release();
				grabber = new OpenCVFrameGrabber(0);
				grabber.setImageWidth(700);
				grabber.setImageHeight(700);
				grabber.start();
				grabbedImage = grabberConverter.convert(grabber.grab());

			}
			int count = 15;
			grayImage = cvCreateImage(cvGetSize(grabbedImage), 8, 1); 

			
			smallImage = cvCreateImage(cvSize(grabbedImage.width() / 4, grabbedImage.height() / 4), 8, 1);

			stop = false;

			while (!stop && (grabbedImage = grabberConverter.convert(grabber.grab())) != null) {

				Frame frame = grabberConverter.convert(grabbedImage);
				BufferedImage image = paintConverter.getBufferedImage(frame, 2.2 / grabber.getGamma());
				Graphics2D g2 = image.createGraphics();

				if (faces == null) {
					cvClearMemStorage(storage);

					
					temp = cvCreateImage(cvGetSize(grabbedImage), grabbedImage.depth(), grabbedImage.nChannels());

					cvCopy(grabbedImage, temp);

					cvCvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
					cvResize(grayImage, smallImage, CV_INTER_AREA);

					
					faces = cvHaarDetectObjects(smallImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
					

					CvPoint org = null;
					if (grabbedImage != null) {













						if (faces.total() == 0) {
							faces = cvHaarDetectObjects(smallImage, classifierSideFace, storage, 1.1, 3,
									CV_HAAR_DO_CANNY_PRUNING);

						}

						if (faces != null) {
							g2.setColor(Color.green);
							g2.setStroke(new BasicStroke(2));
							int total = faces.total();

							for (int i = 0; i < total; i++) {

								
								CvRect r = new CvRect(cvGetSeqElem(faces, i));
								g2.drawRect((r.x() * 4), (r.y() * 4), (r.width() * 4), (r.height() * 4));

								CvRect re = new CvRect((r.x() * 4), r.y() * 4, (r.width() * 4), r.height() * 4);

								cvSetImageROI(temp, re);

								

								org = new CvPoint(r.x(), r.y());

								if (isRecFace) {
									String names="Personne inconnue";
									this.recogniseCode = Vreconn.recognize(temp);

									

									if(recogniseCode != -1)
									{
										dbase.init();
										user = new ArrayList<String>();
										user = dbase.getUser(this.recogniseCode);
										this.output = user;

										names = user.get(1) + " " + user.get(2);
									}

									
									g2.setColor(Color.WHITE);
									g2.setFont(new Font("Arial Black", Font.BOLD, 20));

									g2.drawString(names, (int) (r.x() * 6.5), r.y() * 4);

								}

								if (saveFace) { 
									
									String fName = "faces/" + code_image + "-" + prenom + "_" + nom + "_" + count + ".jpg";
									cvSaveImage(fName, temp);
									count++;

								}

							}
							this.saveFace = false;
							faces = null;
						}

						WritableImage showFrame = SwingFXUtils.toFXImage(image, null);

						javafx.application.Platform.runLater(new Runnable(){

							@Override
							 public void run() {
							frames.setImage(showFrame);
							 }
							 });

						if (isMotion) {
							new Thread(() -> {

								try {

									detectionMv.init(grabbedImage, g2);

								} catch (InterruptedException ex) {
								} catch (Exception e) {

									e.printStackTrace();
								}

							}).start();

						}
						isMotion = false;

					}
					cvReleaseImage(temp);
				}

			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}
	}

	public void stop() {
		stop = true;

		grabbedImage = grayImage = smallImage = null;
		try {
			grabber.stop();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {

			e.printStackTrace();
		}
		try {
			grabber.release();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {

			e.printStackTrace();
		}
		grabber = null;
	}

	public void setClassifier(String name) {

		try {

			setClassiferName(name);
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			
			Loader.load(opencv_objdetect.class);
			classifier = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}






	public void setClassifierSideFace(String name) {

		try {

			classiferName = name;
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			
			Loader.load(opencv_objdetect.class);
			classifierSideFace = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}







	public String getClassiferName() {
		return classiferName;
	}

	public void setClassiferName(String classiferName) {
		this.classiferName = classiferName;
	}












	public void destroy() {
	}

	public boolean isMotion() {
		return isMotion;
	}

	public void setMotion(boolean isMotion) {
		this.isMotion = isMotion;
	}

	public ArrayList<String> getOutput() {
		return output;
	}

	public void clearOutput() {
		this.output.clear();
	}

	public void setOutput(ArrayList<String> output) {
		this.output = output;
	}

	public int getRecogniseCode() {
		return recogniseCode;
	}

	public void setRecogniseCode(int recogniseCode) {
		this.recogniseCode = recogniseCode;
	}

	public int getCode_image() {
		return code_image;
	}

	public void setCode_image(int code_image) {
		this.code_image = code_image;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setFrame(ImageView frame) {
		this.frames = frame;
	}

	public void setSaveFace(Boolean f) {
		this.saveFace = f;
	}

	public Boolean getIsRecFace() {
		return isRecFace;
	}

	public void setIsRecFace(Boolean isRecFace) {
		this.isRecFace = isRecFace;
	}


}
