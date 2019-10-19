
//---------------------------------------------------------
//
// author: thomas diewald
// date: 06.09.2011
//
// example how to use diewald_CV_kit.blobdetection.
// 
// this example needs to have the dlibs.freenect library installed!
//
// download: 
//  1) http://thomasdiewald.com/blog/?p=109
//  2) http://thomasdiewald.com/processing/libraries/dLibs_freenect/
//
// -------------------------------------------------------
// interaction:
//
// dragged mouse - drag a rectangle to define the detection area
//
// key 'UP'   - lower resolution
// key 'DOWN' - higher resolution
// key 'b'   - draw boundingsboxes of blobs
// key 'f'   - fill blobs
//
//---------------------------------------------------------
import java.util.Locale;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;

import dLibs.freenect.*;
import dLibs.freenect.constants.*;
import dLibs.freenect.interfaces.*;
import dLibs.freenect.toolbox.*;



PFont font;

//-------------------------------------------------------------------
// kinect
Kinect kinect_;                     
Kinect3D k3d_;
KinectFrameVideo kinect_video_;     
KinectFrameDepth kinect_depth_;     
KinectTilt kinect_tilt_;

// get width/height --> actually its always 640 x 480
int size_x = VIDEO_FORMAT._RGB_.getWidth(); 
int size_y = VIDEO_FORMAT._RGB_.getHeight(); 
PImage video_frame_, depth_frame_;  // images

//-------------------------------------------------------------------
// blob detection 
BlobDetector blob_detector;
BoundingBox detection_area;
int detection_resolution = 1;
boolean draw_blobs_boundingsbox  = true;
boolean draw_filled_blobs        = true;

public void setup() {

  size(size_x, size_y*2);


  //--------------------------------------------------------------------------
  // KINECT STUFF - initialization
  kinect_ = new Kinect(0);  //create a main kinect instance with index 0

    kinect_video_ = new KinectFrameVideo(VIDEO_FORMAT._RGB_  );      // create a video instance, RGB
  kinect_depth_ = new KinectFrameDepth(DEPTH_FORMAT._11BIT_);      // create a depth instance
  k3d_          = new Kinect3D();                                  // create a 3d instance
  kinect_tilt_  = new KinectTilt();                                // create a Tilt instance

  kinect_video_.setFrameRate(30);
  kinect_depth_.setFrameRate(30);
  k3d_.setFrameRate(30);


  kinect_video_.connect(kinect_);  // connect the created video instance to the main kinect
  kinect_depth_.connect(kinect_);  // connect the created depth instance to the main kinect
  k3d_         .connect(kinect_);  // connect 3d to Kinect
  kinect_tilt_ .connect(kinect_);  // connect Tilt to Kinect

  kinect_tilt_.setTiltDegrees(10);  // set tilt degrees

  // create a PImage for video/depth
  video_frame_ = createImage(VIDEO_FORMAT._RGB_  .getWidth(), VIDEO_FORMAT._RGB_     .getHeight(), RGB);
  depth_frame_ = createImage(DEPTH_FORMAT._11BIT_.getWidth(), DEPTH_FORMAT._11BIT_   .getHeight(), RGB);


  //--------------------------------------------------------------------------
  // BLOB DETECTION STUFF - initialization
  blob_detector = new BlobDetector(size_x, size_y);
  blob_detector.setResolution(detection_resolution);
  blob_detector.computeContours(true);
  blob_detector.computeBlobPixels(!true);
  blob_detector.setMinMaxPixels(10*10, size_x*size_y);

  blob_detector.setBLOBable(new BLOBable_Kinect_3D(this).setKinect3d(k3d_));

  detection_area = new BoundingBox(0, 0, size_x, size_y);
  detection_area = new BoundingBox(200, 200, 300, 300);
  blob_detector.setDetectingArea(detection_area);

  //--------------------------------------------------------------------------
  //  FONT, FRAMERATE, ...
  font = createFont("Calibri", 14);
  textFont(font);
  frameRate(200);
}





public void draw() {
  assignPixels( video_frame_, kinect_video_);
  assignPixels( depth_frame_, kinect_depth_);


  image(depth_frame_, 0, 0);
  image(video_frame_, 0, size_y);


  // draw the detection-area
  stroke(0, 0, 0);
  strokeWeight(1);
  noFill();
  rect(detection_area.xMin(), detection_area.yMin(), detection_area.xSize()-1, detection_area.ySize()-1);

  // set resolution - improves speed a lot
  blob_detector.setResolution(detection_resolution);


  blob_detector.update();


  ArrayList<Blob> blob_list = blob_detector.getBlobs();

  for (int blob_idx = 0; blob_idx < blob_list.size(); blob_idx++ ) {
    Blob blob = blob_list.get(blob_idx);
    ArrayList<Contour> contour_list = blob.getContours();
    for (int contour_idx = 0; contour_idx < contour_list.size(); contour_idx++ ) {
      Contour contour = contour_list.get(contour_idx);
      BoundingBox bb = contour.getBoundingBox();

      // draw the outer contours
      if ( contour_idx == 0) {

        if ( draw_blobs_boundingsbox ) {
          drawBoundingBox(bb, color(0), 2);
          fill(0);
          text("blob["+blob_idx+"]", bb.xMin(), bb.yMin()- textDescent()*2);
        }

        drawContour(contour.getPixels(), color(255), color(0, 150), draw_filled_blobs, 2);
      } 
      else {
        // draw the inner contours, if they have more than 20 vertices
        //          if( contour.getPixels().size() > 20){
        //            drawContour(contour.getPixels(), color(255, 150,0), color(0, 100), false, 1);
        //          }
      }
    }
  }

  // simple information about framerate, and number of detected blobs
  fill(0, 200); 
  noStroke();
  rect(0, 0, 150, 50);
  printlnNumberOfBlobs(blob_detector);
  printlnFPS();
}





//-------------------------------------------------------------------
public void assignPixels(PImage img, Pixelable kinect_dev) {
  img.loadPixels();
  img.pixels = kinect_dev.getPixels();  // assign pixels of the kinect device to the image
  img.updatePixels();
}


//-------------------------------------------------------------------
//this is maybe not necessary, but is the proper way to close everything
public void dispose() {
  Kinect.shutDown(); 
  super.dispose();
}

