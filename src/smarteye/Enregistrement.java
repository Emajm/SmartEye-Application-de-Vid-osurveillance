package smarteye;

import org.opencv.core.Core;  
import org.opencv.core.Mat;  
import org.opencv.core.Size;  
import org.opencv.videoio.VideoCapture;  
import org.opencv.videoio.VideoWriter;  
import org.opencv.videoio.Videoio;
import smarteye.Communication;  

public class Enregistrement implements Runnable {

public static boolean exe = true;
	public void run() {

		try {

            //traitement
                System.out.println ("Nous sommes dans run");
				Enregistrement.exe=true;
                int compteur=0;
                while (Enregistrement.exe) {  
                       System.out.println ("capture : "+compteur);
                       //traitement 
                       compteur++;
					   Thread.currentThread().sleep(66);
		            }   
            //traitement  TEST THREAD


            	
            	/*
                ///////////////////////////////////////////////////////
		        System.loadLibrary (Core.NATIVE_LIBRARY_NAME);  
		        VideoCapture    camera =   new  VideoCapture ( 0 );  
		          
		        VideoWriter    writer =   new  VideoWriter ( "c:/new/test1.avi" , VideoWriter.fourcc(  'M', 'J', 'P', 'G') ,  15 ,  new  Size(640 ,400),  true ) ;  
		          
		        camera.set (Videoio.CV_CAP_PROP_FRAME_WIDTH,  640 );  
		        camera.set (Videoio.CV_CAP_PROP_FRAME_HEIGHT,  400 );  
		          
		        if (! camera.isOpened ()) {  
		            System.out.println ( "the Error" );  
		        }  
		        else  {  
		            int  index =  0 ;  
		            Mat  frame =   new  Mat ();  
		                 
		            while ( true ) {  
		                if  (camera.read (frame)) { 
		                    System.out.println ( "Captured the Frame the Width"  + frame.width () +  "the Height"  + frame.height ());  
		                    writer.write(frame);  
		                    Thread.currentThread().sleep(66);  
		                    index ++;  
		                }  
		  
		                if  (index>  100 ) {  
		                    break ;  
		                }  
		                  
		                frame.release ();  
		            }     
		        }  
		        writer.release ();  
		        camera.release ();  

				///////////////////////////////////////////////////////
                */



                ///////////////////////////////////////////////////////

		} catch (Exception e) {
			/*if (exception == null) {
				exception = e;

			}*/
            System.out.println ( "Exept" ); 
		}
	}


	public void stop() { Enregistrement.exe=false; }


}
