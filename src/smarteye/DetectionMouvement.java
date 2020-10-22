package smarteye;


import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import smarteye.Mail;
import smarteye.Communication;


public class DetectionMouvement {


    public void init(  IplImage frame,Graphics2D g2 ) throws Exception {

    	OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage image = null;
        IplImage prevImage = null;
        IplImage diff = null;

        CanvasFrame canvasFrame = new CanvasFrame("Detecteur de mouvement");
        canvasFrame.setCanvasSize(frame.width(), frame.height());

        CvMemStorage storage = CvMemStorage.create();

        while (canvasFrame.isVisible() && (frame != null)) {
            cvClearMemStorage(storage);

            cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
            if (image == null) {
                image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(frame, image, CV_RGB2GRAY);
            } else {
                prevImage = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                prevImage = image;
                image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(frame, image, CV_RGB2GRAY);
            }

            if (diff == null) {
                diff = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            }

            if (prevImage != null) {

                cvAbsDiff(image, prevImage, diff);

                cvThreshold(diff, diff, 64, 255,CV_THRESH_BINARY);


                canvasFrame.showImage(converter.convert(diff));





                CvSeq contour = new CvSeq(null);
                cvFindContours(diff, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);

                while (contour != null && !contour.isNull()) {
                    if (contour.elem_size() > 7.5) {
                        CvBox2D box = cvMinAreaRect2(contour, storage);

                    	g2.setColor(Color.RED);

        				g2.setFont(new Font("Arial Black", Font.BOLD, 20));

        				String name = "Mouvement detecte!";

        				g2.drawString(name, (int) (50), (50));
                        //Mail.send("devbetatest77@gmail.com","L2H98RedTeam77","choukri17ayoub@gmail.com","Mouvv","How r u?");
                        //Thread.sleep(3000);



                        if (box != null) {
                            CvPoint2D32f center = box.center();
                            CvSize2D32f size = box.size();

                        }
                    }
                    contour = contour.h_next();
                }
            }
        }

        canvasFrame.dispose();
    }


}
