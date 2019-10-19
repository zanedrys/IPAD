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
 * the interface Blobable can be seen as a "function pointer".<br>
 * the method "isBLOBable()" is called for each pixel of an image, to check if it belongs to
 * a blob or not.<br>
 * so this should be a convenient way, to make your own conditions for blobs.<br>
 * <br>
 * <ul>
 * <li>
 * here's an example of the default condition, integrated in the imageprocessing class:<br>
 * 
 * <pre>
 *final class BLOBable_DEFAULT implements BLOBable{
 *  int width_, height_;
 *  String name;
 *
 *  //@Override
 *  public final boolean isBLOBable(int index, int x, int y) {
 *    return false;
 *  }
 *  
 *  //@Override
 *  public final void updateOnFrame( int width, int height) {
 *    width_ = width;
 *    height_ = height;
 *    System.out.println("default");
 *  }
 *  
 *  //@Override
 *  public final void init() {
 *    name = this.getClass().getSimpleName(); 
 *    System.out.println("default = "+name);
 *  }
 *}
 * </pre>
 * </li>
 * <li>
 * Here's another example, that can just be copied into your sketch
 * <pre>
 *public final class BLOBable_A implements BLOBable{
 *  int width_, height_;
 *  private float mousex_val_;
 *  private String name_; 
 *  
 *  private PApplet papplet_;
 *  private PImage img_;
 *  
 *  public BLOBable_A(PApplet papplet, PImage img){
 *    papplet_ = papplet;
 *    img_ = img;
 *  }
 *  //@Override
 *  public final void init() {
 *    name_ = this.getClass().getSimpleName(); 
 *  }
 *  
 *  //@Override
 *  public final void updateOnFrame(int width, int height) {
 *    width_ = width;
 *    height_ = height;
 *    mousex_val_ = PApplet.map(papplet_.mouseX, 0, papplet_.width,  0, 100);
 *  }
 *  //@Override
 *  public final boolean isBLOBable(int pixel_index, int x, int y) {    
 *    return (PixelColor.brighntess(img_.pixels[pixel_index]) < mousex_val_);
 *  }
 * 
 *}
 * </pre>
 * </li>
 * </ul>
 * 
 * 
 *
 * @author thomas diewald (c) 2011
 *
 */


public interface BLOBable {
  /**
   * checks the current pixel-index for beeing part of the blob or not.
   * isBLOBable() is called for every pixel!
   * 
   * @param pixel_index the current pixelindex which is checked.
   * @param x  current x-position of the detection (column).
   * @param y  current x-position of the detection (row).
   * @return true, if the pixel is part of the blob. false if not.
   */
  public boolean isBLOBable(int pixel_index, int x, int y);
  
  /**
   * updateOnFrame() is called every time, before the blobdetection starts to analyse. 
   * 
   * @param width  width of the whole frame (video, image, etc.)
   * @param height height of the whole frame (video, image, etc.)
   */
  public void updateOnFrame( int width, int height);
  
  /**
   * init() is called when assigning a BLOBable-instance to a blobdetection.
   */
  public void init();
}
