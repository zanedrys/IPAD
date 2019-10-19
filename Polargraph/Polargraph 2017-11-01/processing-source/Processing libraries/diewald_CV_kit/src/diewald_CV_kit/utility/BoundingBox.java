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



package diewald_CV_kit.utility;

import diewald_CV_kit.blobdetection.Pixel;


/**
 *
 *
 * @author thomas diewald (c) 2011
 *
 */
public final class BoundingBox {
  private float center_x_, center_y_;
  private int x_min_, x_max_, y_min_, y_max_;
  private int size_x_, size_y_;
  
  /**
   * generate a new bounding-box.
   */
  public BoundingBox(){
  }
  /**
   * 
   * generate a new bounding-box, and define its bounds.
   * @param x_min minimum x-pos of the bounding-box.
   * @param y_min minimum y-pos of the bounding-box.
   * @param x_max maximum x-pos of the bounding-box.
   * @param y_max maximum y-pos of the bounding-box.
   */
  public BoundingBox(int x_min, int y_min, int x_max, int y_max){
    set(x_min, y_min, x_max, y_max);
  }
  
  /**
   * 
   * set the bounds of the bounding-box.
   * @param x_min minimum x-pos of the bounding-box.
   * @param y_min minimum y-pos of the bounding-box.
   * @param x_max maximum x-pos of the bounding-box.
   * @param y_max maximum y-pos of the bounding-box.
   */
  public final void set(int x_min, int y_min, int x_max, int y_max){
    x_min_ = x_min;
    x_max_ = x_max;
    y_min_ = y_min;
    y_max_ = y_max;
    size_x_ = x_max_ - x_min_;
    size_y_ = y_max_ - y_min_;
    center_x_ = x_min_ + size_x_ * 0.5f;
    center_y_ = y_min_ + size_y_ * 0.5f;
  }
  
  /**
   * check if the given coordinate is inside the bounding-box.
   * @param x x-coordinate.
   * @param y y-coordinate.
   * @return true, if the coordinate is inside the bounding-box.
   */
  public final boolean inside(int x, int y){
    return x >= x_min_ && x < x_max_ && y >= y_min_ && y < y_max_;
  }
  
  /**
   * check if the given pixel is inside the bounding-box
   * @param p the pixel to check for.
   * @return true, if the pixel is inside the bounding-box
   */
  public final boolean inside(Pixel p){
    return p.x_ >= x_min_ && p.x_ < x_max_ && p.y_ >= y_min_ && p.y_ < y_max_;
  }
  
  public final int xMin(){ return x_min_; }
  public final int yMin(){ return y_min_; }
  public final int xMax(){ return x_max_; }
  public final int yMax(){ return y_max_; }
  public final int xSize(){ return size_x_; }
  public final int ySize(){ return size_y_; }
  public final float xCenter(){ return center_x_; }
  public final float yCenter(){ return center_y_; }
  
}
