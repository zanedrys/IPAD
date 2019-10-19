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
//---------------------------------------------------------
import java.util.Locale;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;



PFont font;
PImage sample_img;
int size_x, size_y;

BlobDetector blob_detector;
int scale = 1;
public void setup() {

  String image_path = sketchPath+"/data/";

  sample_img = loadImage(image_path + "diewald_CV_kit_lettersPROCESSING.jpg");

  size_x = sample_img.width;
  size_y = sample_img.height;
  size(size_x*scale, size_y*scale);

  sample_img.loadPixels();

  blob_detector = new BlobDetector( size_x, size_y);
  blob_detector.setResolution(1);
  blob_detector.computeContours(true);
  blob_detector.computeBlobPixels(true);
  blob_detector.setMinMaxPixels(10*10, size_x*size_y);

  blob_detector.setBLOBable(new BLOBable_Letters(this, sample_img));

  font = createFont("Calibri", 14);
  textFont(font);
  frameRate(200);
}




public void draw() {
  scale(scale);
  background(255);
  println("");
  image(sample_img, 0, 0);

  blob_detector.setResolution(1);

  //update the blob_detector with the new pixelvalues
  blob_detector.update();

  // get a list of all the blobs
  ArrayList<Blob> blob_list = blob_detector.getBlobs();


  // iterate through the blob_list
  for (int blob_idx = 0; blob_idx < blob_list.size(); blob_idx++ ) {

    // get the current blob from the blob-list
    Blob blob = blob_list.get(blob_idx);


    int number_of_pixels = blob.getNumberOfPixels();
    //      System.out.println("blob["+blob_idx+"] pixels = "+number_of_pixels);


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
        drawBoundingBox(bb, color(0), 1);  
        // draw the blob-id
        fill(0);
        text("blob["+blob_idx+"]", bb.xMin(), bb.yMin()- textDescent()*2);

        // draw the contour
        drawContour(contour.getPixels(), color(255, 0, 0), color(0, 125, 125, 80), !false, 1); 

        //          // generate a convex hull object
        //          ConvexHullDiwi convex_hull = new ConvexHullDiwi();
        //          // calculate the convex hull, based on the contour-list
        //          convex_hull.update(contour.getPixels());
        //          //draw the convex hull
        //          drawConvexHull(convex_hull, color(0, 255, 0), 1);
        //
        //          float convex_hull_area = Polyline.AREA(convex_hull);
        //          float percentage = number_of_pixels/convex_hull_area;
        //          println("blob["+blob_idx+"] convex_hull_area "+ convex_hull_area);
        //          println("blob["+blob_idx+"] percentage "+ (int)(percentage*100));
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



