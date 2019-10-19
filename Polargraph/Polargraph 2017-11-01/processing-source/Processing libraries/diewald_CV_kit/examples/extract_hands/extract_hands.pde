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
//---------------------------------------------------------

import java.util.Locale;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;



PFont font;
PImage sample_img;
int size_x, size_y;

BlobDetector blob_detector;

public void setup() {

  String image_path = sketchPath+"/data/";

  sample_img = loadImage(image_path + "diewald_CV_kit_two_hands_640x480.jpg");

  size_x = sample_img.width;
  size_y = sample_img.height;
  size(size_x, size_y);

  sample_img.loadPixels();

  blob_detector = new BlobDetector(size_x, size_y);
  blob_detector.setResolution(1);
  blob_detector.computeContours(true);
  blob_detector.computeBlobPixels(true);
  blob_detector.setMinMaxPixels(10*10, size_x*size_y);

  blob_detector.setBLOBable(new BLOBable_Hands(this, sample_img));

  font = createFont("Calibri", 14);
  textFont(font);
  frameRate(200);
}




public void draw() {
  background(255);

  image(sample_img, 0, 0);

  //update the new imageprocessor with the new pixelvalues
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

        // draw the boundingbox
        drawBoundingBox(bb, color(0), bb.inside(mouseX, mouseY) ? 5 : 1);
        // draw the blob-id
        fill(0);
        text("blob["+blob_idx+"]", bb.xMin(), bb.yMin()- textDescent()*2);


        // draw the contour
        if ( blob_idx == 0)
          drawContour(contour.getPixels(), color(255, 0, 0), color(0, 255, 0, 50), !mousePressed && bb.inside(mouseX, mouseY), 1); 
        if ( blob_idx == 1)
          drawContour(contour.getPixels(), color(255, 0, 0), color(0, 0, 255, 50), !mousePressed && bb.inside(mouseX, mouseY), 1);
      } 
      else {
        // draw the inner contours, if they have more than 20 vertices
        if ( contour.getPixels().size() > 20) {
          drawContour(contour.getPixels(), color(255, 150, 0), color(0, 100), false, 1);
        }
      }
    }
  }


  fill(255, 200); 
  noStroke();
  rect(0, 0, 150, 50);
  printlnNumberOfBlobs(blob_detector);
  printlnFPS();
}


