/**
 * 
 * diewald_CV_kit v1.1
 * 
 * this library provides simple tools needed in computer-vision.
 * 
 * 
 * 
 *   (C) 2012    Thomas Diewald
 *               http://www.thomasdiewald.com
 *   
 *   last built: 12/13/2012
 *   
 *   download:   http://thomasdiewald.com/processing/libraries/diewald_CV_kit/
 *   source:     https://github.com/diwi/diewald_CV_kit 
 *   
 *   tested OS:  osx,windows
 *   processing: 1.5.1, 2.07
 *
 *
 *
 *
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */



package diewald_CV_kit.blobdetection;

import java.util.ArrayList;

import diewald_CV_kit.libraryinfo.LibraryInfo;
import diewald_CV_kit.utility.BoundingBox;


/**
 * BlobDetector is the main entry class of the library.<br>
 * <br>
 * it computates the blobs/regions/labels, and their contours found in an image/video/etc., based on the condition-interface (BLOBable).<br>
 * 
 * 
 * @author thomas diewald (c) 2011
 *
 */
public final class BlobDetector {
  
  protected int pixel_jump_ = 1;
  protected final Pixel img_pixels_[][];

  protected final int width_, height_;
  
  private final ArrayList<Blob> blobs_ = new ArrayList<Blob>(50);
  
  // define the blob-detection area
  private BoundingBox bb_blob_detecting_area_;

  private int min_blob_pixels_ = 0;
  private int max_blob_pixels_ = Integer.MAX_VALUE;
  private boolean compute_blob_contours_ = true;
  private boolean compute_blob_pixels_   = false;

  
  private BLOBable blobable_default_ = new BLOBable_DEFAULT();
  private BLOBable blobable_active_ = blobable_default_;
  
  
  
  static {
    @SuppressWarnings("unused")
    String version = LibraryInfo.version();
  }
  
  
  /**
   * 
   * define dimensions of the blob-detection.
   * 
   * @param width   width of the frame 
   * @param height  height of the frame
   */
  public BlobDetector( int width, int height ){
    width_  = width;
    height_ = height;
    img_pixels_ = new Pixel[height_][width_];
    for( int y = 0; y < height_; y++)
      for( int x = 0; x < width_; x++)
        img_pixels_[y][x] = new Pixel(x, y);
   
    bb_blob_detecting_area_ = new BoundingBox(0, 0, width_, height_); 
  }
  



  
  /**
   * generate blobs/contours/etc. depending on the previous settings.
   * @return true on success, false on any kind of failure.
   */
  public final boolean update(){
    if( blobable_active_ == null ){
      System.err.println("diewald_cv: NO VALID CONDITION !!!");
      return false;
    }
    blobable_active_.updateOnFrame(width_, height_);
    
    int blobdetector_min_x_ = bb_blob_detecting_area_.xMin();
    int blobdetector_min_y_ = bb_blob_detecting_area_.yMin();
    int blobdetector_max_x_ = bb_blob_detecting_area_.xMax();
    int blobdetector_max_y_ = bb_blob_detecting_area_.yMax();
    
//    System.out.println("------------------------------------------------------");
//    long time = System.currentTimeMillis();
    
    blobs_.clear();
    ArrayList<PixelRow> pixelrows = new ArrayList<PixelRow>(8000);
    int row = 0;
    for(int y = blobdetector_min_y_; y < blobdetector_max_y_; y+=pixel_jump_){
      row = y*width_;
      for(int x = blobdetector_min_x_; x < blobdetector_max_x_; x+=pixel_jump_){
        Pixel px_current = img_pixels_[y][x];
        px_current.reset(); // reset the previous state of the pixel!
      
//        if ( !blobable_active_.isBLOBable(row+x],  x, y))
        if ( !blobable_active_.isBLOBable(row+x, x, y))
          continue;

        PixelRow pixelrow_pixel_left = ( x-pixel_jump_ >= blobdetector_min_x_ ) ? img_pixels_[y][x-pixel_jump_].getPixelRow(): null;
        if( pixelrow_pixel_left != null ){
          pixelrow_pixel_left.xe_ = x;
          px_current.setPixelRow(pixelrow_pixel_left);
        } else {
          PixelRow pr = new PixelRow(x, y);
          px_current.setPixelRow(pr);
          pixelrows.add(pr);
        }
        
        PixelRow pixelrow_pixel_top = ( y-pixel_jump_ >= blobdetector_min_y_ ) ? img_pixels_[y-pixel_jump_][x].getPixelRow() : null;
        if( pixelrow_pixel_top != null )
          px_current.getPixelRow().addTopRow(pixelrow_pixel_top);
        
      } 
    } 

    
    /*
    // CREATE BLOBS FROM PIXELROWS (hidden from library user!)
    // there are currently two ways, to do this... recursively iterating through 
    // the linked pixelrows, or by using a stack.
    // using the new stackversion avoids a possible stack-overflow-exception
    // when recursing too deep. 
    // TODO: allow adjusting stacksize! (current limit is 30000)
    
    boolean STACK_VERSION = true;
    
     
    if( STACK_VERSION ){ // use stack version
      int blob_id = 0;
      for(int idx = 0; idx < pixelrows.size() ; idx++){
        PixelRow pr = pixelrows.get(idx);
        if( pr.getBlob() == null ) {
          Blob new_blob = new Blob(this, blob_id, pr, true);
          if( new_blob.getNumberOfPixels() >= min_blob_pixels_ && 
              new_blob.getNumberOfPixels() <= max_blob_pixels_ ){
            blobs_.add(new_blob);
            blob_id++;
          }
        }
      }
    } else { // use recursive version, to create blob from adjacent pixelrows
      
    }
*/
    try
    {
      int blob_id = 0;
      for(int idx = 0; idx < pixelrows.size() ; idx++){
        PixelRow pr = pixelrows.get(idx);
        if( pr.getBlob() == null ) {
          Blob new_blob = new Blob(this, blob_id, pr);
          if( new_blob.getNumberOfPixels() >= min_blob_pixels_ && 
              new_blob.getNumberOfPixels() <= max_blob_pixels_ ){
            blobs_.add(new_blob);
            blob_id++;
          }
        }
      }
    }
    catch(StackOverflowError e){
      String class_name = this.getClass().getCanonicalName();
      System.err.println("diewald_CV_kit: error type:  " + e);
      System.err.println("                in class:    " + class_name);
      System.err.println("                ... while generating blobs ...");
      System.err.println("                why?: blob has too much small branches / too much pixels to link");
      blobs_.clear();
      return false;
    }
    
    
    for(int i = 0; i < blobs_.size(); i++ ){
      if( compute_blob_contours_ ) blobs_.get(i).updateContours();
      if( compute_blob_pixels_   ) blobs_.get(i).updatePixels();
    }
    
//    System.out.println("time to calculate:   "+ (System.currentTimeMillis()-time) + " ms");
//    System.out.println("number of blobs:   "+ blobs_.size());
//    System.out.println("------------------------------------------------------");
    return true;
  }

  
  
  /**
   * set a bounding-box to define the detection-area (by reference).<br>
   * note: the bounds are not checked, so make sure, they are within the image-bounds.
   * @param bb bounding-box.
   */
  public final void setDetectingArea(BoundingBox bb){   
    bb_blob_detecting_area_ = bb;
  }
  
  /**
   * returns the boundingsbox, which defines the detection-area (by reference).<br>
   * @return the boundingsbox, which defines the detection-area.
   */
  public final BoundingBox getDetectingArea(){
    return bb_blob_detecting_area_;
  }
  
  
  
  
  /**
   *  set the condition (BLOBable) that defines, which pixels are part of a blob.
   *  this condition gets called for every pixels.
   * 
   * @param blobable the condition
   */
  public final void setBLOBable(BLOBable blobable){
    if( blobable == null )
      return;
    blobable_active_ = blobable;
    blobable_active_.init();
//    System.out.println("diewald_cv: BLOBable: "+ blobable.getClass().getSimpleName());
  }
  
  /**
   * defines, how many pixels are skipped on each detection. <br>
   * this can be important on a low frame-rate, or if the resolution of the generated blob is not that important.
   * e.g.<br>
   * setResolution(1) - take every pixel for detection
   * setResolution(2) - take every second pixel for detection
   * setResolution(3) - take every third pixel for detection
   * 
   * @param pixel_jump
   */
  public final void setResolution(int pixel_jump){
    pixel_jump_ = (pixel_jump < 1) ? 1 : pixel_jump;
  }
  

  /**
   * define the minimum/maximum number of pixels a blob can have.<br>
   * only blobs with a number of pixels within this range, are detected.
   * 
   * 
   * @param min_blob_pixels minimum number of pixels
   * @param max_blob_pixels maximum number of pixels
   */
  public final void setMinMaxPixels(int min_blob_pixels, int max_blob_pixels){
    min_blob_pixels_ = min_blob_pixels;
    max_blob_pixels_ = max_blob_pixels;
  }
  
  /**
   * defines, whether the the contours (outer and inner ones) of each blob are generated.<br>
   * if you dont need contours, call computeContours(false);<br>
   * the default setting is: computeContours(true);
   * @param compute_contours
   */
  public final void computeContours( boolean compute_contours){
    compute_blob_contours_ = compute_contours;
  }
  
  /**
   * if computeBlobPixels(true) is called, the library generates a new pixelarray
   * for each detected blob, after each update.<br>
   * if you don't need this blobs pixel-array, you can save a lot of computation-time, by calling
   * computeBlobPixels(false), which is the default setting!
   * 
   * @param compute_blob_pixels 
   */
  public final void computeBlobPixels( boolean compute_blob_pixels){
    compute_blob_pixels_ = compute_blob_pixels;
  }

 
  /**
   * getBlobs() returns the detected blobs after each update().
   * 
   * @return the detected blobs 
   */
  public final ArrayList<Blob> getBlobs(){
    return blobs_;
  }
  
  /**
   * getPixels() returns an 2-d array of pixels.<br>
   * each pixel contains information about its position x/y,
   * and the blobs id, it belongs to (call: "pixel".getBlob() ).<br>
   * 
   * this array is used internally, to label the pixels accordingly to their neighborhood, defined by the interface BLOBable.<br>
   * on each blob-detection update, each pixel in this array gets a reset.
   * 
   * @return the labeled pixels
   */
  public final Pixel[][] getPixels(){
    return img_pixels_;
  }
  
}









final class BLOBable_DEFAULT implements BLOBable{
  int width_, height_;
  String name;

  @Override
  public final boolean isBLOBable(int index, int x, int y) {
    return false;
  }
  
  @Override
  public final void updateOnFrame( int width, int height) {
    width_ = width;
    height_ = height;
    System.out.println("default");
  }
  
  @Override
  public final void init() {
    name = this.getClass().getSimpleName(); 
    System.out.println("default = "+name);
  }
}








