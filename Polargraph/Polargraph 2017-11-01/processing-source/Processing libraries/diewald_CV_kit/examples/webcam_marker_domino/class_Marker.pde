public final class MarkerDomino{
  
  PImage img_;
  PApplet parent_;
  String marker_name_ = "";
  BlobDetector marker_blob;
  float white_percentage = 0;
  int width_, height_;

  MarkerDomino(PApplet parent, PImage img, String marker_name){
    parent_      = parent;
    img_         = img;
    width_       = img_.width;
    height_      = img_.height;
    marker_name_ = marker_name;
    
    marker_blob = new BlobDetector(width_, height_);
    marker_blob.setResolution(1);
    marker_blob.computeContours(true);
    marker_blob.computeBlobPixels(!true);
    marker_blob.setMinMaxPixels(0, width_*height_);
    marker_blob.setBLOBable(new BLOBable_MARKER(parent_, img_));
    analyze();
  }
  
  public void analyze(){
    img_.loadPixels();
    marker_blob.update();
  }
  
  public void visualize(int x, int y){
    pushMatrix();
    translate(x, y);
    image(img_, 0, 0);
 
    ArrayList<Blob> blob_list = marker_blob.getBlobs();
    for(int blob_idx = 0; blob_idx < blob_list.size(); blob_idx++ ){
      Blob blob = blob_list.get(blob_idx);

      ArrayList<Contour> contour_list = blob.getContours();
      for(int contour_idx = 0; contour_idx < contour_list.size(); contour_idx++ ){
        Contour contour = contour_list.get(contour_idx);
        BoundingBox bb = contour.getBoundingBox();
        if( contour_idx == 0){
          drawBoundingBox(bb, color(0), 1);  
          fill(0);
          text(marker_name_, bb.xMin(), bb.yMin()- textDescent()*2);
          drawContour(contour.getPixels(), color(255, 0, 0), color(0, 255, 0, 50), false, 1); 
          
          ConvexHullDiwi convex_hull = new ConvexHullDiwi();
          convex_hull.update(contour.getPixels());
          drawConvexHull(convex_hull, color(0, 255, 0), 1);
        } else {
          if( contour.getPixels().size() > 20){
            drawContour(contour.getPixels(), color(255, 150,0), color(0, 100), false, 1);
          }
        }
      }
    }
    popMatrix();
  }
  
  public final int numberOfDots(){
    return marker_blob.getBlobs().get(0).getContours().size();
  }
}

