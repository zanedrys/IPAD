
//---------------------------------------------------------
//
// author: thomas diewald
// date: 06.09.2011
//
// example how to use diewald_CV_kit.blobdetection.
// 
// this example needs to have the "dlibs.freenect" library installed!
//
// download: 
//  1) http://thomasdiewald.com/blog/?p=109
//  2) http://thomasdiewald.com/processing/libraries/dLibs_freenect/
//
// the blob detection happens for a list of 3d-points from the kinect.
// only a defined area is tracked, defined by a far/near clipping plane, 
// and a cutted camera-cone.
// the tracked blobs outer contours are drawn into the 3d-sene, 
// and projected onto the z-plane (rotated by 90 degrees).
//
// -------------------------------------------------------
// interaction:
//
// dragged mouse - drag a rectangle to define the detection area
//
// key 'UP'   - lower resolution
// key 'DOWN' - higher resolution
// key 'c'    - print camera orientation settings
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

import processing.opengl.*;
import peasy.*; 

import processing.video.*;




MovieMaker mm;  // Declare MovieMaker object
boolean make_movie = !true;



PFont font;

//-------------------------------------------------------------------
// kinect
Kinect kinect_;                     
Kinect3D k3d_;   
KinectFrameDepth kinect_depth_;     
KinectTilt kinect_tilt_;

// get width/height --> actually its always 640 x 480
int size_x = VIDEO_FORMAT._RGB_.getWidth(); 
int size_y = VIDEO_FORMAT._RGB_.getHeight(); 


//-------------------------------------------------------------------
// blob detection 
BlobDetector blob_detector;
BLOBable_Kinect_3D blobsable_kinect3d;
BoundingBox detection_area;
int detection_resolution = 1;

float clipping_depth = 15;
float clipping_near = 50;
float clipping_far  = clipping_near + clipping_depth;
float clipping_animstep = .1f;

//-------------------------------------------------------------------
// PeasyCam
PeasyCam cam;   

int scale_screen = 1;
public void setup() {

  size(size_x*scale_screen, size_y*scale_screen, OPENGL);

  //--------------------------------------------------------------------------
  // KINECT STUFF - initialization
  kinect_ = new Kinect(0);  //create a main kinect instance with index 0
  kinect_depth_ = new KinectFrameDepth(DEPTH_FORMAT._11BIT_);  // create a depth instance
  kinect_tilt_  = new KinectTilt();  

  k3d_ = new Kinect3D(); // generate a 3d instance
  k3d_.setFrameRate(30); // set framerate

  kinect_depth_.connect(kinect_);  //connect the created depth instance to the main kinect
  k3d_.connect(kinect_);
  kinect_tilt_.connect(kinect_);

  kinect_tilt_.setTiltDegrees(13);
  initPeasyCam();

  //--------------------------------------------------------------------------
  // BLOB DETECTION STUFF - initialization
  blob_detector = new BlobDetector(size_x, size_y);
  blob_detector.setResolution(detection_resolution);
  blob_detector.computeContours(true);
  blob_detector.computeBlobPixels(true);
  blob_detector.setMinMaxPixels(30*30, size_x*size_y);

  blobsable_kinect3d = new BLOBable_Kinect_3D(this);
  blobsable_kinect3d.setKinect3d(k3d_);
  blobsable_kinect3d.setMinMaxDepth(clipping_near, clipping_far);
  blob_detector.setBLOBable(blobsable_kinect3d);


  detection_area = new BoundingBox(150, 0, size_x-150, size_y);
  blob_detector.setDetectingArea(detection_area);

  //--------------------------------------------------------------------------
  //  FONT, FRAMERATE, ...
  font = createFont("Calibri", 14);
  textFont(font);
  frameRate(200);
  
  if( make_movie ){
    startMovie();
  }
}





public void draw() {

  clipping_near += clipping_animstep;
  if ( clipping_near > 200 || clipping_near< 50)
    clipping_animstep = -clipping_animstep;
  clipping_far  = clipping_near + clipping_depth;
  blobsable_kinect3d.setMinMaxDepth(clipping_near, clipping_far);

  blobsable_kinect3d.setMinMaxDepth(200, 300);

  background(255);
  //    drawWKS(100);
  drawPointCloud(5);

  // draw the detection-area
  stroke(0, 0, 0);
  strokeWeight(1);
  noFill();


  drawDetectionBox();


  // set resolution - improves speed a lot
  blob_detector.setResolution(detection_resolution);


  blob_detector.update();


  ArrayList<Blob> blob_list = blob_detector.getBlobs();
  float scale = 100;
  KinectPoint3D kinect_3d[] = k3d_.get3D();
  for (int blob_idx = 0; blob_idx < blob_list.size(); blob_idx++ ) {
    Blob blob = blob_list.get(blob_idx);
    Pixel[] blob_pixels = blob.getPixels();
    stroke(125, 0, 0); 
    strokeWeight(.1);

    for ( Pixel p : blob_pixels ) {
      int k3d_pixel_idx = p.x_ + size_x*p.y_;
      KinectPoint3D k3d_point = kinect_3d[k3d_pixel_idx];
      if ( blobsable_kinect3d.isBLOBable(k3d_pixel_idx, p.x_, p.y_)) {
        float x = k3d_point.x * scale;
        float y = k3d_point.y * scale;
        float z = k3d_point.z * scale;
        point(x, y, z);
      }
    }

    // find bounding-box of detected blob ... and draw it
    float x_min = 0, y_min = 0, z_min = 0, x_max = 0, y_max = 0, z_max = 0;
    if ( blob_pixels.length > 0) {
      float x = 0, y = 0, z = 0;
      int k3d_pixel_idx;
      KinectPoint3D k3d_point;

      for ( Pixel p : blob_pixels ) {
        k3d_pixel_idx = p.x_ + size_x*p.y_;
        k3d_point = kinect_3d[k3d_pixel_idx];
        if ( blobsable_kinect3d.isBLOBable(k3d_pixel_idx, p.x_, p.y_)) {
          x_min = x_max = k3d_point.x * scale;
          y_min = y_max = k3d_point.y * scale;
          z_min = z_max = k3d_point.z * scale;
          break;
        }
      }

      for ( Pixel p : blob_pixels ) {
        k3d_pixel_idx = p.x_ + size_x*p.y_;
        k3d_point = kinect_3d[k3d_pixel_idx];
        if ( blobsable_kinect3d.isBLOBable(k3d_pixel_idx, p.x_, p.y_)) {
          x = k3d_point.x * scale;
          y = k3d_point.y * scale;
          z = k3d_point.z * scale;
          if ( x < x_min ) x_min = x;
          if ( y < y_min ) y_min = y;
          if ( z < z_min ) z_min = z;

          if ( x > x_max ) x_max = x;
          if ( y > y_max ) y_max = y;
          if ( z > z_max ) z_max = z;
        }
      }
      drawBoundingBox3d(x_min, y_min, z_min, x_max, y_max, z_max);
    }


    ArrayList<Contour> contour_list = blob.getContours();
    for (int contour_idx = 0; contour_idx < contour_list.size(); contour_idx++ ) {
      if ( contour_idx > 0 )continue;
      Contour contour = contour_list.get(contour_idx);
      ArrayList<Pixel> contour_pixels =  contour.getPixels();

      stroke(0, 125, 255);
      strokeWeight(3);
      noFill();

      beginShape();
      for ( Pixel p : contour_pixels ) {
        int k3d_pixel_idx = p.x_ + size_x*p.y_;
        KinectPoint3D k3d_point = kinect_3d[k3d_pixel_idx];
        if ( blobsable_kinect3d.isBLOBable(k3d_pixel_idx, p.x_, p.y_)) {
          float x = k3d_point.x * scale;
          float y = k3d_point.y * scale;
          float z = k3d_point.z * scale;
          vertex(x, y, z);
        }
      }
      endShape();



      pushMatrix();
      rotateY(HALF_PI);
      stroke(200, 0, 0);
      strokeWeight(1);
      beginShape();
      for ( Pixel p : contour_pixels ) {
        int k3d_pixel_idx = p.x_ + size_x*p.y_;
        KinectPoint3D k3d_point = kinect_3d[k3d_pixel_idx];
        if ( blobsable_kinect3d.isBLOBable(k3d_pixel_idx, p.x_, p.y_)) {
          float x = k3d_point.x * scale;
          float y = k3d_point.y * scale;
          vertex(x, y, 0);
        }
      }
      endShape();

      stroke(0, 0, 0);
      beginShape();
        vertex(x_min, y_min, 0);
        vertex(x_max, y_min, 0);
        vertex(x_max, y_max, 0);
        vertex(x_min, y_max, 0);
        vertex(x_min, y_min, 0);
      endShape();

      // draw convex hull, on z=0-plane
      ConvexHullDiwi convex_hull = new ConvexHullDiwi();
      convex_hull.update(contour.getPixels());
      noFill();
      stroke(0, 0, 255); 
      strokeWeight(2);
      DoubleLinkedList<Pixel> convex_hull_list = convex_hull.get();
      convex_hull_list.gotoFirst();
//      beginShape();
//        for (int cvh_idx = 0; cvh_idx < convex_hull_list.size()+1; cvh_idx++, convex_hull_list.gotoNext() ) {
//          Pixel p = convex_hull_list.getCurrentNode().get();
//          int k3d_pixel_idx = p.x_ + size_x*p.y_;
//          KinectPoint3D k3d_point = kinect_3d[k3d_pixel_idx];
//          if ( blobsable_kinect3d.isBLOBable(k3d_pixel_idx, p.x_, p.y_)) {
//            float x = k3d_point.x * scale;
//            float y = k3d_point.y * scale;
//            vertex(x, y, 0);
//          }
//        }
//      endShape();
      popMatrix();
    }
  }

  // simple information about framerate, and number of detected blobs
  //    fill(0, 200); noStroke();
  //    rect(0, 0, 150, 50);
  //    printlnNumberOfBlobs(blob_detector);
  //    printlnFPS();
  
  if( mm != null){
    mm.addFrame();  // Add window's pixels to movie
  }
  //println(frameRate);
}




//-------------------------------------------------------------------
//this is maybe not necessary, but is the proper way to close everything
public void dispose() {
  Kinect.shutDown(); 
  super.dispose();
}












