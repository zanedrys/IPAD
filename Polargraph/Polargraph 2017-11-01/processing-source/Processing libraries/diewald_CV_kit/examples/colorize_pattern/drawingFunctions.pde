//--------------------------------------------------------------------------------------------
// this tab is just a collection of functions that draw polylines, points, etc.
// you'll see this tab in the other examples too.
//--------------------------------------------------------------------------------------------

import java.util.Locale;



//float fps_av = 0;
//float fps_count = 0;

public void printlnFPS() {
  String frame_rate = String.format(Locale.ENGLISH, "speed: %6.2f fps%n", frameRate);
//  println(frame_rate);
//  if( frameCount > 50 ){
//    fps_av+=frameRate;
//    fps_count++;
//    println(String.format(Locale.ENGLISH, "speed_av: %6.2f fps%n", fps_av/fps_count));
//  }
  fill(100, 200, 255);
  text(frame_rate, 10, 20);
}



public void printlnNumberOfBlobs(BlobDetector blob_detector) {
  fill(100, 200, 255);
  text("number of blobs: "+blob_detector.getBlobs().size(), 10, 40);
}






// draw convex-hull - as polyline
public void drawConvexHull(ConvexHullDiwi convex_hull, int stroke_color, float stroke_weight) {
  noFill();
  stroke(stroke_color); 
  strokeWeight(stroke_weight);
  DoubleLinkedList<Pixel> convex_hull_list = convex_hull.get();
  convex_hull_list.gotoFirst();
  beginShape();
  for (int cvh_idx = 0; cvh_idx < convex_hull_list.size()+1; cvh_idx++, convex_hull_list.gotoNext() ) {
    Pixel p = convex_hull_list.getCurrentNode().get();
    vertex(p.x_, p.y_);
  }
  endShape();
}





// draw convex-hull-points - only points
public void drawConvexHullPoints(ConvexHullDiwi convex_hull, int stroke_color, float stroke_weight) {
  noFill();
  stroke(stroke_color); 
  strokeWeight(stroke_weight);
  DoubleLinkedList<Pixel> convex_hull_list = convex_hull.get();
  convex_hull_list.gotoFirst();

  for (int cvh_idx = 0; cvh_idx < convex_hull_list.size(); cvh_idx++, convex_hull_list.gotoNext() ) {
    Pixel p = convex_hull_list.getCurrentNode().get();
    point(p.x_, p.y_);
  }
}




// draw boundingsbox
public void drawBoundingBox(BoundingBox bb, int stroke_color, float stroke_weight) {
  noFill();
  stroke(stroke_color); 
  strokeWeight(stroke_weight);
  rect(bb.xMin(), bb.yMin(), bb.xSize(), bb.ySize());
}





// draw contour
public void drawContour(ArrayList<Pixel> pixel_list, int stroke_color, int fill_color, boolean fill, float stroke_weight) {
  if ( !fill)
    noFill();
  else
    fill(fill_color);
  stroke(stroke_color);
  strokeWeight(stroke_weight);
  beginShape();
  for (int idx = 0; idx < pixel_list.size(); idx++) {
    Pixel p = pixel_list.get(idx);
    vertex(p.x_, p.y_);
  }
  endShape();
}




// draw points
public void drawPoints(ArrayList<Pixel> pixel_list, int stroke_color, float stroke_weight) {
  stroke(stroke_color);
  strokeWeight(stroke_weight);

  for (int idx = 0; idx < pixel_list.size(); idx++) {
    Pixel p = pixel_list.get(idx);
    point(p.x_, p.y_);
  }
}

