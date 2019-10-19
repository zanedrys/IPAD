//---------------------------------------------------------
//
// author: thomas diewald
// date: 06.09.2011
//
// example how to use diewald_CV_kit.blobdetection.
//
// -------------------------------------------------------
// interaction:
//
// mouseX - defines the brightness treshhold
// dragged mouse - drag a rectangle to define the detection area
//
// key 'UP'   - lower resolution
// key 'DOWN' - higher resolution
// key 'b'   - draw boundingsboxes of blobs
// key 'f'   - fill blobs
//---------------------------------------------------------
import java.util.Locale;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;
import java.util.ArrayList;


PFont font;
PImage sample_img;

int size_x, size_y;
BlobDetector blob_detector;
BoundingBox detection_area;
int detection_resolution = 1;
boolean draw_blobs_boundingsbox  = true;
boolean draw_filled_blobs        = true;

public void setup() {

  //    sample_img = loadImage(image_path + "imageprocessing_gradient_v7_600x600.jpg");
  sample_img = loadImage("imageprocessing_gradient_v9_640x480.jpg");
  size_x = sample_img.width;
  size_y = sample_img.height;
  size(size_x, size_y);

  sample_img.loadPixels();


  blob_detector = new BlobDetector(size_x, size_y);
  blob_detector.setResolution(detection_resolution);
  blob_detector.computeContours(true);
  blob_detector.computeBlobPixels(!true);
  blob_detector.setMinMaxPixels(10*10, size_x*size_y);

  blob_detector.setBLOBable(new BLOBable_GRADIENT(this, sample_img));

  detection_area = new BoundingBox(0, 0, size_x, size_y);
  blob_detector.setDetectingArea(detection_area);

  font = createFont("Calibri", 14);
  textFont(font);
  frameRate(200);
}



public void draw() {
  background(255);

  image(sample_img, 0, 0);

  // draw the detection-area
  stroke(0, 0, 0);
  strokeWeight(1);
  noFill();
  rect(detection_area.xMin(), detection_area.yMin(), detection_area.xSize()-1, detection_area.ySize()-1);


  // set resolution - improves speed a lot
  blob_detector.setResolution(detection_resolution);

  //update the blob-detector with the new pixelvalues
  blob_detector.update();



  // get a list of all the blobs
  ArrayList<Blob> blob_list = blob_detector.getBlobs();

  // iterate through the blob_list
  for (int blob_idx = 0; blob_idx < blob_list.size(); blob_idx++ ) {

    // get the current blob from the blob-list
    Blob blob = blob_list.get(blob_idx);

    // get the list of all the contours from the current blob
    ArrayList<Contour> contour_list = blob.getContours();

    // iterate through the contour_list
    for (int contour_idx = 0; contour_idx < contour_list.size(); contour_idx++ ) {

      // get the current contour from the contour-list
      Contour contour = contour_list.get(contour_idx);

      // get the current boundingbox from the current contour
      BoundingBox bb = contour.getBoundingBox();

      // handle the first contour (outer contour = contour_idx == 0) different to the inner contours
      if ( contour_idx == 0) {

        // draw the boundingbox and blob-id as text
        if ( draw_blobs_boundingsbox ) {
          drawBoundingBox(bb, color(0, 200, 200), 1);
          fill(0, 200, 200);
          text("blob["+blob_idx+"]", bb.xMin(), bb.yMin()- textDescent()*2);
        }

        // draw the contour
        drawContour(contour.getPixels(), color(255, 0, 0), color(255, 0, 255, 100), draw_filled_blobs, 1);
      } 
      else {
        // draw the inner contours, if they have more than 20 vertices
        if ( contour.getPixels().size() > 20) {
          drawContour(contour.getPixels(), color(255, 150, 0), color(0, 100), false, 1);
        }
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




