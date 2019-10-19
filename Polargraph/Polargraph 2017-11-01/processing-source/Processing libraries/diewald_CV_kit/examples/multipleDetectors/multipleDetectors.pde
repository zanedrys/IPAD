//---------------------------------------------------------
//
// author: thomas diewald
// date: 12.11.2011
//
// example how to use diewald_CV_kit.blobdetection.
//
// each blobdetector (one for red colors, the other one for green color) 
// tries to find blobs defined by the BLOBable-interface.
// -------------------------------------------------------
// interaction:
//
//---------------------------------------------------------
import java.util.Locale;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;


PFont font;
PGraphics sample_img;
int size_x = 640, size_y = 480;

BlobDetector blob_detector[] = new BlobDetector[2];
BLOBable blobables[]         = new BLOBable[2];

public void setup() {

  size(size_x, size_y);

  sample_img = createGraphics(size_x, size_y, JAVA2D);
  sample_img.loadPixels();

  blobables[0] = new BLOBable_redBlobs(sample_img);
  blobables[1] = new BLOBable_greenBlobs(sample_img);

  for (int i = 0; i < blob_detector.length; i++) {
    blob_detector[i] = new BlobDetector(size_x, size_y);
    blob_detector[i].setResolution(1);
    blob_detector[i].computeContours(true);
    blob_detector[i].computeBlobPixels(true);
    blob_detector[i].setMinMaxPixels(10*10, size_x*size_y);

    blob_detector[i].setBLOBable(blobables[i]);
  }

  font = createFont("Calibri", 14);
  textFont(font);
  frameRate(200);
}




public void draw() {
  background(255);
  updateImage();
  image(sample_img, 0, 0);

  for (int i = 0; i < blob_detector.length; i++) {
    runBlobDetector(blob_detector[i]);
  }
}


public void runBlobDetector(BlobDetector bd) {
  //update the new imageprocessor with the new pixelvalues
  bd.update();
  // get a list of all the blobs
  ArrayList<Blob> blob_list = bd.getBlobs();
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
      // draw the boundingbox
      drawBoundingBox(bb, color(0), 1);
      // draw the blob-id
      fill(0);
      text("blob["+blob_idx+"]", bb.xMin(), bb.yMin()- textDescent()*2);
      drawContour(contour.getPixels(), color(0, 100), color(0, 0, 0, 255), false, 10);
    }
  }
}











// updateImage() simulates two colored ellipses oving around
// inside an Image (PGraphics in this case).

float r_red = 150;
float r_green = 100;
PVector red_dir   = new PVector(random(5), random(5));
PVector red_pos   = new PVector(r_red, r_red);
PVector green_dir = new PVector(random(5), random(5));
PVector green_pos = new PVector(r_green, r_green);
public void updateImage() {

  if ( red_pos.x  +red_dir.x   < r_red   || red_pos.x  +red_dir.x   > size_x-r_red)   red_dir.x   =  -red_dir.x;
  if ( red_pos.y  +red_dir.y   < r_red   || red_pos.y  +red_dir.y   > size_y-r_red)   red_dir.y   =  -red_dir.y;
  if ( green_pos.x+green_dir.x < r_green || green_pos.x+green_dir.x > size_x-r_green) green_dir.x =  -green_dir.x;
  if ( green_pos.y+green_dir.y < r_green || green_pos.y+green_dir.y > size_y-r_green) green_dir.y =  -green_dir.y;

  red_pos.add(red_dir);
  green_pos.add(green_dir);

  sample_img.beginDraw();
  sample_img.background(255);
  sample_img.noStroke();

  sample_img.fill(255, 0, 0);
  sample_img.ellipse(red_pos.x, red_pos.y, r_red*2, r_red*2);

  sample_img.fill(0, 255, 0);
  sample_img.ellipse(green_pos.x, green_pos.y, r_green*2, r_green*2);
  sample_img.endDraw();
}
