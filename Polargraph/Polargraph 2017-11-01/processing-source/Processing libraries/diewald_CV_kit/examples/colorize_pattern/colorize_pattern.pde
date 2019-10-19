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

import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;


PImage sample_img;
PImage result;
int size_x, size_y;

BlobDetector blob_detector;

public void setup() {

  String image_path = sketchPath+"/data/";

//  sample_img = loadImage(image_path + "diewald_CV_kit_pattern4colorizer_v1.jpg");
//  sample_img = loadImage(image_path + "diewald_CV_kit_pattern4colorizer_v2.jpg");
//  sample_img = loadImage(image_path + "diewald_CV_kit_pattern4colorizer_v3.jpg");
//  sample_img = loadImage(image_path + "diewald_CV_kit_pattern4colorizer_v4.jpg");
//  sample_img = loadImage(image_path + "diewald_CV_kit_pattern4colorizer_v5.jpg");
//  sample_img = loadImage(image_path + "diewald_CV_kit_pattern4colorizer_v6.jpg");
  sample_img = loadImage(image_path + "diewald_CV_kit_mcescher_dogs.jpg");


  size_x = sample_img.width;
  size_y = sample_img.height;
  result = createImage(size_x, size_y, RGB);
  size(size_x, size_y);

  sample_img.loadPixels();

  blob_detector = new BlobDetector(size_x, size_y);
  blob_detector.setResolution(1);
  blob_detector.computeContours(!true);
  blob_detector.computeBlobPixels(!true);
  blob_detector.setMinMaxPixels(0, size_x*size_y);

  blob_detector.setBLOBable(new BLOBable_Colorizer(this, sample_img));

  frameRate(200);
  //    noLoop();
  image(sample_img, 0, 0);
}




public void draw() {

  if ( !keyPressed ) {
    //update the new imageprocessor with the new pixelvalues
    blob_detector.update();

    result.loadPixels();
    Pixel pixel_list[][] = blob_detector.getPixels();

    int default_col = color(0);
    Pixel p;
    Blob blob;
    PixelColor col;
    int col_as_int;
    for (int y = 0; y < size_y; y++) {
      for (int x = 0; x < size_x; x++) {
        p = pixel_list[y][x];
        blob = p.getBlob();
        if ( blob != null ) {
          col = blob.getColor();
          col_as_int = col.getCol();
          result.pixels[y*size_x+x] = col_as_int;
        }
        else {
          result.pixels[y*size_x+x] = default_col;
        }
      }
    }
    result.updatePixels();
    image(result, 0, 0);
  } 
  else {
    image(sample_img, 0, 0);
  }

  fill(0, 200); 
  noStroke();
  rect(0, 0, 150, 50);
  printlnNumberOfBlobs(blob_detector);
  printlnFPS();
}


