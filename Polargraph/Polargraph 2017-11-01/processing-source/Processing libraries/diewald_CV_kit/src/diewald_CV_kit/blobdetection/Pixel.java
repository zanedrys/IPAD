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



/**
 *
 * the class Pixel is used for saving pixel coordinates, id-labeling (connected component labeling).<br>
 * <br>
 * 
 * 
 * @author thomas diewald (c) 2011
 *
 */
public final class Pixel {
  private PixelRow pixel_row_ = null;
  
  protected boolean border_ = false;
  public final int x_, y_;

  /**
   * initialize a new pixel
   * @param x x-position
   * @param y y-position
   */
  public Pixel(int x, int y){
    x_ = x;
    y_ = y;
  }
  
  /**
   * set the pixelrow this pixel is part of.
   * @param pixel_row the pixelrow this pixel is part of.
   */
  protected final void setPixelRow(PixelRow pixel_row){
    pixel_row_ = pixel_row;
  }
  /**
   * returns the pixelrow this pixel is part of.
   * @return the pixelrow this pixel is part of.
   */
  protected final PixelRow getPixelRow(){
    return pixel_row_;
  }
  
  
  /**
   * returns the blob, this pixel is part of.
   * @return the blob, this pixel is part of.
   */
  public final Blob getBlob(){
    if( pixel_row_ == null )
      return null;
    return pixel_row_.getBlob();
  }
  
  /**
   * reset this pixels state
   */
  protected void reset(){
    if( pixel_row_ != null ){
      pixel_row_ = null;
      border_ = false;
    }
  }
  
  /**
   * returns true, if this pixel, is part of a contour.
   * @return true, if this pixel, is part of a contour.
   */
  public final boolean isOnAContour(){
    return border_;
  }
}
