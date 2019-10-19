
//---------------------------------------------------------
//
// author: thomas diewald
// date: 06.09.2011
//
// example how to use diewald_CV_kit.blobdetection.
//
// it shows some simple marker tracking. the markers are domino images (see the data folder).
// the marker recognition is very simple in this case:
//   found blobs are checked, for how much inner contours they have.
//   so 1 inner contour means yeah, i found "domino 1",
//   2 inner contour means yeah, i found "domino 2", ... and so on.
// 
// of course you can make some smarter tracking, by checking for other marker features too!
//
// -------------------------------------------------------
// interaction:
// key 's' - start making movie
// key 'e' - stop making movie
//---------------------------------------------------------
import java.util.Locale;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;
import codeanticode.gsvideo.*;
import processing.video.*;

MovieMaker mm;  // Declare MovieMaker object
boolean make_movie = !true;



PFont font;
PImage img_marker_1, img_marker_2, img_marker_1dot;
int size_x, size_y;

BlobDetector blob_detector;
GSCapture video;
PImage maker_imgs[] = new PImage[6];
MarkerDomino marker[] = new MarkerDomino[maker_imgs.length];



public void setup() {

  String image_path = sketchPath+"/data/";
  for (int i = 0; i < marker.length; i++) {
    maker_imgs[i] = loadImage(image_path + "diewald_CV_kit_marker_domino_"+(i+1)+".jpg");
    marker[i] = new MarkerDomino(this, maker_imgs[i], "domino_"+(i+1));
  }

  video = new GSCapture(this, 640, 480, 30);
  size(video.width, video.height);

  blob_detector = new BlobDetector(video.width, video.height);
  blob_detector.setResolution(1);
  blob_detector.computeContours(true);
  blob_detector.computeBlobPixels(true);
  blob_detector.setMinMaxPixels(10*10, video.width*video.height/5);

  blob_detector.setBLOBable(new BLOBable_MARKER(this, video));

  font = createFont("Calibri", 14);
  textFont(font);
  frameRate(200);
  
  
    
  // Create MovieMaker object with size, filename,
  // compression codec and quality, framerate
  if( make_movie ){
    startMovie();
  }
}






public void draw() {

  //image(sample_img, 0, 0);

  if (video != null && video.available()) {
    background(255);
    video.read();
    image(video, 0, 0);
    //println("");
    //update the blob_detector with the new pixelvalues
    blob_detector.update();

    // get a list of all the blobs
    ArrayList<Blob> blob_list = blob_detector.getBlobs();
    //      println("blob_list.size() = "+blob_list.size());

    MarkerDomino detected_marker = null;


    // iterate through the blob_list
    for (int blob_idx = 0; blob_idx < blob_list.size(); blob_idx++ ) {

      // get the current blob from the blob-list
      Blob blob = blob_list.get(blob_idx);


      int number_of_pixels = blob.getNumberOfPixels();
      //        System.out.println("blob["+blob_idx+"] pixels = "+number_of_pixels);

      detected_marker = null;

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

          //            drawContour(contour.getPixels(), color(0, 0, 255), color(0, 255, 0, 150), false, 7); 

          // find the contours points, that overlap with the boundingbox
          //            ArrayList<Pixel> cp = contour.getPixels();
          //            Pixel left, top, bot, right;
          //            left  = top = bot = right = cp.get(0);
          //            for(int i = 0; i < cp.size(); i++){
          //              Pixel p = cp.get(i);
          //
          //              if( p.x_  < left.x_) left = p;
          //              if( p.x_ == left.x_ && p.y_ < left.y_) left = p;
          //              
          //              if( p.y_  < top.y_) top = p;
          //              if( p.y_ == top.y_ && p.x_ > top.x_) top = p;
          //              
          //              if( p.x_  > right.x_) right = p;
          //              if( p.x_ == right.x_ && p.y_ > right.y_) right = p;
          //              
          //              if( p.y_  > bot.y_) bot = p;
          //              if( p.y_ == bot.y_ && p.x_ < bot.x_) bot = p;
          //            }
          //            
          //            int diameter = 10;
          //            fill(0);
          //            stroke(255, 0, 0);
          //            ellipse(left.x_ , left.y_ , diameter, diameter);
          //            ellipse(top.x_  , top.y_  , diameter, diameter);
          //            ellipse(right.x_, right.y_, diameter, diameter);
          //            ellipse(bot.x_  , bot.y_  , diameter, diameter);




          // example how to simplify a contour
          int repeat_simplifying = 10;
          ArrayList<Pixel> contour_simple = Polyline.SIMPLIFY(contour, 2, 2);
          for (int simple_cnt = 0; simple_cnt < repeat_simplifying; simple_cnt++) {
            contour_simple = Polyline.SIMPLIFY(contour_simple, 2, 2);
          }

          // generate a convex hull object
          ConvexHullDiwi convex_hull = new ConvexHullDiwi();
          convex_hull.update(contour_simple);
          //simplify convex hull
          ArrayList<Pixel> convex_hull_simple = Polyline.SIMPLIFY(convex_hull, 2, 2);
          for (int simple_cnt = 0; simple_cnt < repeat_simplifying; simple_cnt++) {
            convex_hull_simple = Polyline.SIMPLIFY(convex_hull_simple, 2, 2);
          }

          // make sure, contour_simple is closed
          int size    = contour_simple.size();
          Pixel first = contour_simple.get(0);
          Pixel last  = contour_simple.get(size-1); 
          if ( first != last)
            contour_simple.add(first);

          // make sure, convex_hull_simple is closed
          size  = convex_hull_simple.size();
          first = convex_hull_simple.get(0);
          last  = convex_hull_simple.get(size-1); 
          if ( first != last)
            convex_hull_simple.add(first);



          // contour: length / area
          float length_contour         = Polyline.LENGTH( contour        );
          float length_contour_simple  = Polyline.LENGTH( contour_simple );
          float area_contour           = Polyline.AREA  ( contour        );
          float area_contour_simple    = Polyline.AREA  ( contour_simple );


          // convex_hull: length / area
          float length_convex_hull         = Polyline.LENGTH( convex_hull        );
          float length_convex_hull_simple  = Polyline.LENGTH( convex_hull_simple );
          float area_convex_hull           = Polyline.AREA  ( convex_hull        );
          float area_convex_hull_simple    = Polyline.AREA  ( convex_hull_simple );

          // fillfactor, defines the ratio of the size of the boundingbox-vs the area-size of the contour
          float bb_fill_factor = bb.xSize()*bb.ySize() / area_contour;

          float diff_length_simple      = abs(length_contour_simple / length_convex_hull_simple);
          float diff_length_convex_hull = abs(length_convex_hull_simple / length_convex_hull);
          float diff_area_simple        = abs(area_contour_simple   / area_convex_hull_simple);
          float diff_area_contour       = abs(area_contour_simple   / area_contour);
          float diff_area_convex_hull   = abs(area_convex_hull_simple   / area_convex_hull);
          
          // check if a marker is found, here's much room for improvements!!!!!!
          if ( (
                  bb_fill_factor            <= 1.30 &&
                  abs(diff_area_contour-1f) >= 0.15 &&
                  true
                ) || (
                  diff_length_simple        <=  1.1 &&
                  diff_area_simple          >=  0.9 &&
                  convex_hull.get().size()  >=    4 &&
                  convex_hull_simple.size() >=    4 && 
                  convex_hull_simple.size() <=    8 &&
                  contour_simple.size()     >=    4 &&
                  //                contour_simple.size()     <=    8 &&
                  bb_fill_factor            <=   2.2 &&
                  true 
                )
            ) {
      
            detected_marker = null;

            for (int marker_idx = 0; marker_idx < marker.length; marker_idx++) {
              
              if ( contour_list.size() == marker[marker_idx].numberOfDots() ) {
                detected_marker = marker[marker_idx];
                break;
              }
            }

            if ( detected_marker != null) {
              drawBoundingBox(bb, color(0), 1);  
                  

              float text_x = bb.xMin()+2;
              float text_y = bb.yMin()- textDescent()*2;
              fill(0);
              rect(text_x-2, text_y+textDescent(), textWidth(detected_marker.marker_name_)+4, -textDescent()-textAscent());
              fill(255);
              text(detected_marker.marker_name_, text_x, text_y);
              //                drawContour(contour.getPixels(), color(255, 0, 0), color(0, 255, 0, 150), !false, 5); 
              //                drawConvexHull(convex_hull, color(0, 255, 0), 1);


              drawContour(contour_simple, color(255, 0, 0), color(0, 255, 0, 150), false, 2); 
              //                drawContour(convex_hull_simple, color(0, 255, 0), color(0, 255, 0, 150), false, 1); 

//              println("----------------------------------------------------------------");
//              println("found marker              = " + detected_marker.marker_name_);
//              println("");
//              println("convex_hull_simple size   = " + convex_hull_simple.size());
//              println("convex_hull        size   = " + convex_hull.get().size());
//              println("contour_simple     size   = " + contour_simple.size());
//              println("contour            size   = " + contour.getPixels().size());
//              println("");
//              println("bb_fill_factor            = " + bb_fill_factor);
//              println("diff_area_simple          = " + diff_area_simple);
//              println("diff_area_contour         = " + diff_area_contour);
//              println("diff_area_convex_hull     = " + diff_area_convex_hull);
//              println("");
//              println("diff_length_simple        = " + diff_length_simple);
//              println("diff_length_convex_hull   = " + diff_length_convex_hull);
//
//              println("length_contour_simple     = "+length_contour_simple);
//              println("length_convex_hull_simple = "+length_convex_hull_simple);
//
//              println("number_of_pixels          = " + number_of_pixels);
//              println("area_contour              = " + area_contour);
            }
          }
        } 
        else {
          if ( detected_marker != null) {
            //               draw the inner contours, if they have more than 20 vertices
            if ( contour.getPixels().size() > 20) {
              drawContour(contour.getPixels(), color(255, 150, 0), color(0, 100), false, 2);
              int repeat_simplifying = 3;
              ArrayList<Pixel> contour_simple = Polyline.SIMPLIFY(contour, 2, 2);
              for (int simple_cnt = 0; simple_cnt < repeat_simplifying; simple_cnt++) {
                contour_simple = Polyline.SIMPLIFY(contour_simple, 2, 2);
              }
              drawContour(contour_simple, color(0, 150, 255), color(0, 100), false, 1);
            }
          }
        }
      }
    }
    fill(0, 200); 
    noStroke();
    rect(0, 0, 150, 50);
    printlnNumberOfBlobs(blob_detector);
    printlnFPS();
    
    
      
    if( mm != null){
      mm.addFrame();  // Add window's pixels to movie
    }
  }

//  translate(0, 50);
//  scale(.4f);
//  int x_pos = 0;
//  int y_pos = 10;
//  for(int i = 0; i < marker.length; i++){
//    marker[i].visualize(x_pos, y_pos);
//    y_pos+= marker[i].height_+10;
//  }
}



void keyReleased(){
  if (key == 'e') {
    if( mm != null){
      endMovie();
    }
  }
  if (key == 's') {
    if (mm == null){
      startMovie();
    }
  }
}




void startMovie(){
   String movie_name = "VIDEO/diewald_CV_kit_domino.mov";
   mm = new MovieMaker(this, width, height,  movie_name,
                       20, MovieMaker.JPEG, MovieMaker.BEST);
   println("----- STARTED MOVIE -----" + movie_name);
}

void endMovie(){
  mm.finish();  // Finish the movie if space bar is pressed!
  mm = null;
  println("----- FINISHED MOVIE -----");
}

