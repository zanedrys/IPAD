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



import java.util.List;

import diewald_CV_kit.blobdetection.Pixel;
/**
 * the class ConvexHullDiwi calculates the convex-hull of some given points (Pixel).<br>
 * <br>
 * the list of convex-hull-points is represented as a double-linked-list, which enables very fast adding/removing of points<br>
 * 
 * @author thomas diewald (c) 2011
 *
 */
public final class ConvexHullDiwi{

  private final DoubleLinkedList<Pixel> point_list_ = new DoubleLinkedList<Pixel>();
  /**
   * generate a new convex hull.
   */
  public ConvexHullDiwi(){
  }
  
  /**
   * returns the double-linked list, that represents the convex-hull-points.
   * @return the double-linked list, that represents the convex-hull-points.
   */
  public final DoubleLinkedList<Pixel> get(){
    return point_list_;
  }
  
  /**
   * clear the convex hull (empty the point list).
   */
  public final void clear(){
    point_list_.clear();
  }
  
  /**
   * if the coordinates of the given pixel are part of the hull, remove this hull-point.
   * 
   * @param pixel the pixel to remove
   * @return true on success
   */
  public final boolean remove(Pixel pixel){
    if( pixel == null ) 
      return false;
    return remove(pixel.x_, pixel.y_);
  }
  
  
  /**
   * if the given coordinates are part of the hull, remove this hull-point.
   * 
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @return true on success
   */
  public final boolean remove(float x, float y){
    Pixel p;
    for(int i = 0; i < point_list_.size(); i++, point_list_.gotoNext()){
      p = point_list_.getCurrentNode().get();
      if( p.x_ == x && p.y_ == y ){
        point_list_.removeCurrentNode();
        return true;
      }
    }
    return false;
  }
  
  
  

  /**
   * update the existing convex-hull, on an other convex-hull.
   * @param convex_hull
   * @return the current convex-hull-instance itself.
   */
  public final ConvexHullDiwi update(ConvexHullDiwi convex_hull){
    if( convex_hull == null)
      return this;
    DoubleLinkedList<Pixel> hull_points = convex_hull.get();

    hull_points.gotoMark();
    for(int i = 0; i < hull_points.size(); i++, hull_points.gotoNext() )
      update( hull_points.getCurrentNode().get() );

    hull_points.gotoMark();
    return this;
  }
  
  /**
   * update the existing convex-hull, on a list of pixels.
   * @param pixels
   * @return the current convex-hull-instance itself.
   */
  public final ConvexHullDiwi update(List<Pixel> pixels){
    if( pixels == null)
      return this;

    for(int i = 0; i < pixels.size(); i++)
      update( pixels.get(i) );

    return this;
  } 
  
  /**
   * update the existing convex-hull, on an array of pixels.
   * @param pixels
   * @return the current convex-hull-instance itself.
   */
  public final ConvexHullDiwi update(Pixel pixels[]){
    if( pixels == null)
      return this;

    for(int i = 0; i < pixels.length; i++)
      update( pixels[i] );
    return this;
  }
  
  
  /**
   * update the existing convex-hull, on a single pixel.
   * @param pixel
   * @return the current convex-hull-instance itself.
   */
  public final ConvexHullDiwi update(Pixel pixel){
    if( pixel == null)
      return this;
    if( point_list_.size() < 2) {
      Pixel first = null;
      if( point_list_.size() == 1)
        first = point_list_.gotoFirst().getCurrentNode().get();
        
      if( first == null || (first.x_ != pixel.x_ && first.y_ != pixel.y_) )
        point_list_.add(pixel);
    } else{
      computePoint(point_list_, pixel);
    }
    return this;
  }
  
  /**
   * update the existing convex-hull, on a single coordinate.
   * @param x
   * @param y
   * @return the current convex-hull-instance itself.
   */
  public final ConvexHullDiwi update(int x, int y){
    return update(new Pixel(x, y) );
  }
  

  /**
   * compute the convex hull
   * @param hull
   * @param pnext
   */
  private final void computePoint(DoubleLinkedList<Pixel> hull, Pixel pnext){
    for(int i = 0; i < hull.size(); i++){ // TODO: check if hull.size()-1 is ok
      //if( isRight(hull.get().value, hull.next().get().value, pnext) ){
      if( onSide(hull.getCurrentNode().get(), hull.gotoNext().getCurrentNode().get(), pnext) > 0){
        hull.gotoPrev().add(pnext);
        hull.setMark();
        
        //while( isRight(hull.get().value, hull.next().get().value, hull.next().get().value )){
        while( onSide(hull.getCurrentNode().get(), hull.gotoNext().getCurrentNode().get(), hull.gotoNext().getCurrentNode().get() ) > 0){
          hull.gotoMark().gotoNext().removeCurrentNode();
          hull.gotoMark();
        }
        hull.gotoMark();
        //while( isLeft(hull.get().value, hull.prev().get().value, hull.prev().get().value )){
        while( onSide(hull.getCurrentNode().get(), hull.gotoPrev().getCurrentNode().get(), hull.gotoPrev().getCurrentNode().get() ) < 0){
          hull.gotoMark().gotoPrev().removeCurrentNode();
          hull.gotoMark();
        }
        break;
      }
    }
  }
  
  
  /**
   * used for calculating the position of a point "c" according to a line "ab"
   * 
   * @param a start point of line
   * @param b end point of line
   * @param c point to calculate
   * 
   * @return   0  --> c is on same vector
   *         > 0  --> c is right from ab
   *         < 0  --> c is left  from ab 
   */
  public final float onSide(Pixel a, Pixel b, Pixel c){
    return -1*((b.x_ - a.x_)*(c.y_ - a.y_) - (b.y_ - a.y_)*(c.x_ - a.x_)); //mult *-1 reverts left and right,---> now the contour goes in clockwise direction
  }
  
//  public final boolean isRight(Point_ a, Point_ b, Point_ c){
//    return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
//  }
//  public final boolean isLeft(Point_ a, Point_ b, Point_ c){
//    return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) < 0;
//  }

  
}
