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

import diewald_CV_kit.utility.BoundingBox;



/**
 * the class Contour calculates a closed polyline of a blob.<br>
 * a blob can have only ONE outer contour, and endless inner contours.<br>
 * for example the letter "B" as an image, would have one outer contour, and two inner contours.<br>
 * 
 * @author thomas diewald (c) 2011
 *
 */
public final class Contour {
  
  private final Blob parent_blob_;
  private final int ID_;
  
  private final ArrayList<Pixel> pixels_ = new ArrayList<Pixel>();
  private final BoundingBox boundingbox_;
  

  protected final boolean clock_wise_; // only the outer contour goes in clockwise direction --> important for the convex hull;

  /**
   * 
   * @param parent_blob  the parent blob, this contour is part of
   * @param ID           the id of the contour. also used for checking the direction (clockwise or counter-clockwise).
   * @param x_pos_start  start of the contour - x-position
   * @param y_pos_start  start of the contour - y-position
   */
  protected Contour(Blob parent_blob, int ID, int x_pos_start, int y_pos_start){
    parent_blob_ = parent_blob;
    boundingbox_   = new BoundingBox();
    generate(parent_blob_.blob_detector_.img_pixels_, x_pos_start, y_pos_start);
    updateBoundingBox();
    ID_ = ID;
    
    clock_wise_ = (ID == 0 );
    

  }
  /**
   *----------------------------------------------------------------------------
   * my algorithm uses the "4 - neighborhood only"
   *--------------------------
   *   0  
   * 3 + 1 
   *   2  
   *-------------------------- 
   * 
   * @param pixel_list the array of pixels, with the blob IDs.
   * @param x_pos  start of the contour - x-position
   * @param y_pos  start of the contour - y-position
   * @return true on success
   */
  private final boolean generate(Pixel pixel_list[][], int x_pos, int y_pos){
    
    // just search for a contour, within the detection-area
    BoundingBox detection_area = parent_blob_.blob_detector_.getDetectingArea();
    int x_min = detection_area.xMin();
    int y_min = detection_area.yMin();
    int x_max = detection_area.xMax();
    int y_max = detection_area.yMax();
    

    int search_count = 0;

    int x_pos_next = 0, y_pos_next = 0;
    int neighbor = 0;
    int pj = parent_blob_.blob_detector_.pixel_jump_;
 
    int index_step[][] = { { 0, -pj}, {+pj,  0}, { 0, +pj}, {-pj,  0} }; // vector
  
    Pixel current_pixel = null;
    Pixel pixel_start = pixel_list[y_pos][x_pos];
    if( pixel_start.getBlob() != parent_blob_ || !detection_area.inside(pixel_start) )
      return false; // in case the startpixel makes no sense, or is outside of the detection-area!!!!

    pixels_.clear();
    pixels_.add( pixel_start );
    pixel_start.border_ = true;

    int pixel_after_start_counter = 0;

  
    
    while(search_count++ < 4){ 
      x_pos_next = x_pos + index_step[neighbor][0];
      y_pos_next = y_pos + index_step[neighbor][1];
      
      
      if( x_pos_next >= x_min && x_pos_next < x_max && 
          y_pos_next >= y_min && y_pos_next < y_max)
      {
        current_pixel = pixel_list[y_pos_next][x_pos_next];
        if( current_pixel.getBlob() == parent_blob_ ){
 
          pixels_.add(current_pixel);
          current_pixel.border_ = true;
          
          // check if the contour finder already found the first and second pixel of the contour twice
          // this means, it would start all over, --> endless loop
          if( pixels_.size() >= 2 && 
              pixels_.get(pixels_.size()-2) == pixel_start && 
              pixels_.get(1) == current_pixel &&
              pixel_after_start_counter++ > 0)
          {
            pixels_.remove(pixels_.size()-1); // remove the last pixel added
            break;
          }

          x_pos = x_pos_next;
          y_pos = y_pos_next;
          search_count = 0;
          neighbor += 2;
        }
      } 
      neighbor++;
      neighbor %= 4;
    } 
     
    return true;
  }
  
  
  /**
   * update the bounding-box for the contour.
   */
  private final void updateBoundingBox(){
    int x_min, x_max, y_min, y_max;
    x_min = x_max = pixels_.get(0).x_;
    y_min = y_max = pixels_.get(0).y_;
    for(int i = 0; i < pixels_.size(); i++){
      Pixel p = pixels_.get(i);
      if( p.x_ < x_min ) x_min = p.x_;
      if( p.x_ > x_max ) x_max = p.x_;
      if( p.y_ < y_min ) y_min = p.y_;
      if( p.y_ > y_max ) y_max = p.y_;
    }
    boundingbox_.set(x_min, y_min, x_max, y_max);
  }
  
  /**
   * returns a list of contour-pixels.
   * @return a list of contour-pixels.
   */
  public final ArrayList<Pixel> getPixels(){
    return pixels_;
  }
  /**
   * returns the bounding-box of the contour.
   * @return the bounding-box of the contour.
   */
  public final BoundingBox getBoundingBox(){
    return boundingbox_;
  }
  /**
   * returns true if the contour has clockwise-rotation.
   * @return true if the contour has clockwise-rotation.
   */
  public final boolean clockWise(){
    return clock_wise_;
  }
  /**
   * returns the ID of the contour.
   * @return the ID of the contour.
   */
  public final int ID(){
    return ID_;
  }
  
}

