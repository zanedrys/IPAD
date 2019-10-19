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
//import java.util.Stack;


/**
*
* used for calculating the blobs.<br>
* 
* 
* @author thomas diewald (c) 2011
*
*/
public final class PixelRow {

  protected final int y_;  // y position of the row
  protected final int xs_; // row start 
  protected int xe_;       // row end
  
  protected ArrayList<PixelRow> top_rows_    = new ArrayList<PixelRow>(3);
  protected ArrayList<PixelRow> bottom_rows_ = new ArrayList<PixelRow>(3);
  protected PixelRow last_added_ = null;
  protected Blob blob_;
  
  protected PixelRow(int x, int y){ 
    y_ = y;
    xs_ = xe_ = x;
  }
 
  protected final void addTopRow(PixelRow top_row){
    if( last_added_ != top_row){
      last_added_ = top_row;
      top_rows_.add(top_row);
      top_row.bottom_rows_.add(this);
    }
  }
  
  

  protected final void linkWithBlob(Blob blob){
    if( blob_ != null )
      return;
    blob_ = blob;
    blob_.addPixelRow(this);
    
    for(int i = 0; i < top_rows_   .size(); i++ ) top_rows_   .get(i).linkWithBlob(blob);
    for(int i = 0; i < bottom_rows_.size(); i++ ) bottom_rows_.get(i).linkWithBlob(blob); 
    
    
  }
  
  
  /*
  // alternative version to avoid recursion (and stackoverflow exception)
  private final static PixelRow[] stack = new PixelRow[30000]; 
  
  protected final static void linkWithBlob(final Blob blob, PixelRow pixelrow){
 
    // V1: using java.util.Stack
//    Stack<PixelRow> stack = new Stack<PixelRow>();
//    stack.add(pr);
//    
//    while( !stack.empty() ){
//      pr = stack.pop();
//      if( pr.blob_ == null ){
//        pr.blob_ = blob;
//        blob.addPixelRow(pr);
//   
//        stack.addAll(pr.top_rows_);
//        stack.addAll(pr.bottom_rows_);
//      }
//    }
    
   
    // V2: using array and stack-pointer (... stack-array: final and static!!!)
    int stack_ptr = 0;
    stack[0] = pixelrow;


    while( stack_ptr >= 0)
    {
      pixelrow = stack[stack_ptr--];
 
      if( pixelrow.blob_ == null )
      {
        pixelrow.blob_ = blob;
        blob.addPixelRow(pixelrow);
  
        for(int i = pixelrow.top_rows_   .size(); --i >= 0; ) stack[++stack_ptr] = pixelrow.top_rows_   .get(i);
        for(int i = pixelrow.bottom_rows_.size(); --i >= 0; ) stack[++stack_ptr] = pixelrow.bottom_rows_.get(i);
//        pixelrow.top_rows_   = null;
//        pixelrow.bottom_rows_= null;
      }
    }
  }
  */
  protected final Blob getBlob(){
    return blob_;
  }
  
  /**
   * returns the start x-position of the pixelrow.
   * @return the start x-position of the pixelrow.
   */
  public final int getStartX(){
    return xs_;
  }
  /**
   * returns the end x-position of the pixelrow.
   * @return the end x-position of the pixelrow.
   */
  public final int getEndX(){
    return xe_;
  }
  /**
   * returns the y-position of the pixelrow.
   * @return the y-position of the pixelrow.
   */
  public final int getY(){
    return y_;
  }
}
