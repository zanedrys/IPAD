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

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import diewald_CV_kit.blobdetection.Contour;
import diewald_CV_kit.blobdetection.Pixel;
/**
*
* the class Polyline provides some convenient static methods to edit/analyse/etc. polygons.<br>
* <br>
* 
* 
* @author thomas diewald (c) 2011
*
*/
public abstract class Polyline {
  private Polyline(){ 
  }

 
  
  //----------------------------------------------------------------------------
  // AREA
  //----------------------------------------------------------------------------
  /**
   * returns the surface area of the polyline
   * 
   * @param polyline the polyline to get the length from
   * @return returns the surface area of the polyline
   */
  public static final float AREA( Pixel[] polyline ){
    Pixel p0, p1;
    float area = 0;
    for(int idx = 0; idx < polyline.length-1; idx++){
      p0 = polyline[idx];
      p1 = polyline[idx+1];
      area += (p0.x_ + p1.x_)*(p1.y_ - p0.y_);
    }
    area *= (area < 0) ? -.5f : +.5f;
    return area;
  }
  
  /**
   * returns the surface area of the polyline
   * 
   * @param polyline the polyline to get the length from
   * @return returns the surface area of the polyline
   */
  public static final float AREA( List<Pixel> polyline ){
    List<Pixel> list = polyline;
    Pixel p0, p1;
    float area = 0;
    for(int idx = 0; idx < list.size()-1; idx++){
      p0 = list.get(idx);
      p1 = list.get(idx+1);
      area += (p0.x_ + p1.x_)*(p1.y_ - p0.y_);
    }
    area *= (area < 0) ? -.5f : +.5f;
    return area;
  }
  
  /**
   * returns the surface area of the contour
   * 
   * @param contour the polyline to get the length from
   * @return returns the surface area of the contour
   */
  public static final float AREA(Contour contour){
    return AREA(contour.getPixels());
  }
  
  /**
   * returns the surface area of the convex_hull
   * 
   * @param convex_hull the polyline to get the length from
   * @return returns the surface area of the convex_hull
   */
  public static final float AREA( ConvexHullDiwi convex_hull ){
    DoubleLinkedList<Pixel> list = convex_hull.get();
    list.gotoFirst();
    Pixel p0, p1;
    float area = 0;
    for(int cvh_idx = 0; cvh_idx < list.size(); cvh_idx++){
      p0 = list.getCurrentNode().get();
      list.gotoNext();
      p1 =  list.getCurrentNode().get();
      area += (p0.x_ + p1.x_)*(p1.y_ - p0.y_);
    }
    area *= (area < 0) ? -.5f : +.5f;
    return area;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  //----------------------------------------------------------------------------
  // LENGTH
  //----------------------------------------------------------------------------
  /**
   * returns the length of the polyline
   * 
   * @param polyline the polyline to get the length from
   * @return returns the length of the polyline
   */
  public static final float LENGTH( Pixel[] polyline ){

    Pixel p1, p2;
    float length = 0;
    for(int i = 0; i < polyline.length-1; i++){
      p1 = polyline[i];
      p2 = polyline[i+1];
      length += distance(p1, p2);
    }
    return length;
  }
  
  
  /**
   * returns the length of the polyline
   * 
   * @param polyline the polyline to get the length from
   * @return returns the length of the polyline
   */
  public static final float LENGTH( List<Pixel> polyline ){
    List<Pixel> list = polyline;
    Pixel p1, p2;
    float length = 0;
    for(int i = 0; i < list.size()-1; i++){
      p1 = list.get(i);
      p2 = list.get(i+1);
      length += distance(p1, p2);
    }
    return length;
  }
  
  /**
   * returns the length of the contour
   * 
   * @param contour the polyline to get the length from
   * @return returns the length of the contour
   */
  public static final float LENGTH( Contour contour ){
    return LENGTH(contour.getPixels());
  }
  
  /**
   * returns the length of the convex_hull
   * 
   * @param convex_hull the polyline to get the length from
   * @return returns the length of the convex_hull
   */
  public static final float LENGTH( ConvexHullDiwi convex_hull ){
    DoubleLinkedList<Pixel> list = convex_hull.get();
    list.gotoFirst();
    Pixel p1, p2;
    float length = 0;
    for(int idx = 0; idx < list.size(); idx++){
      p1 = list.getCurrentNode().get();
      list.gotoNext();
      p2 = list.getCurrentNode().get();
      length += distance(p1, p2);
    }
    return length;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  //----------------------------------------------------------------------------
  // SIMPLIFY
  //----------------------------------------------------------------------------
  /**
   *  
   * returns a new simplified polyline, from a given contour
   *
   * @param contour to simplify
   * @param step    the number of vertices in a row, that are checked for the maximum offset
   * @param max_offset maximum offset a vertice can have 
   * @return new simplified polygon
   */
  public static final ArrayList<Pixel> SIMPLIFY( Contour contour, int step,  float max_offset ){
    return SIMPLIFY(contour.getPixels(), step, max_offset);
  }

  /**
   *  
   * returns a new simplified polyline, from a given convex_hull
   *
   * @param convex_hull to simplify
   * @param step    the number of vertices in a row, that are checked for the maximum offset
   * @param max_offset maximum offset a vertice can have 
   * @return new simplified polygon
   */
  public static final ArrayList<Pixel> SIMPLIFY( ConvexHullDiwi convex_hull, int step,  float max_offset ){
    return SIMPLIFY(convex_hull.get().toList(), step, max_offset);
  }
  /**
   *  
   * returns a new simplified polyline, from a given polyline
   *
   * @param polyline to simplify
   * @param step    the number of vertices in a row, that are checked for the maximum offset
   * @param max_offset maximum offset a vertice can have 
   * @return new simplified polygon
   */
  public static final ArrayList<Pixel> SIMPLIFY( List<Pixel> polyline, int step,  float max_offset ){
    ArrayList<Pixel> poly_simp = new ArrayList<Pixel>();
    int index_cur  = 0;
    int index_next = 0;
    poly_simp.add(polyline.get(index_cur));
    
    while( index_cur != polyline.size()-1 ){
      index_next = ((index_cur + step) >= polyline.size()) ? (polyline.size()-1) :  (index_cur + step) ;

      Pixel p_cur  = polyline.get( index_cur  );
      Pixel p_next = polyline.get( index_next );

      while( (++index_cur < index_next) &&  (max_offset > abs(distancePoint2Line(p_cur, p_next, polyline.get(index_cur))) ) );
      poly_simp.add(polyline.get(index_cur));
    }
    return poly_simp;
  }
  
  /**
   *  
   * returns a new simplified polyline, from a given polyline
   *
   * @param polyline to simplify
   * @param step    the number of vertices in a row, that are checked for the maximum offset
   * @param max_offset maximum offset a vertice can have 
   * @return new simplified polygon
   */
  public static final ArrayList<Pixel> SIMPLIFY( Pixel[] polyline, int step,  float max_offset ){
    ArrayList<Pixel> poly_simp = new ArrayList<Pixel>();
    int index_cur  = 0;
    int index_next = 0;
    poly_simp.add(polyline[index_cur]);
    
    while( index_cur != polyline.length-1 ){
      index_next = ((index_cur + step) >= polyline.length) ? (polyline.length-1) :  (index_cur + step) ;

      Pixel p_cur  = polyline[index_cur];
      Pixel p_next = polyline[index_next];

      while( (++index_cur < index_next) &&  (max_offset > abs(distancePoint2Line(p_cur, p_next, polyline[index_cur])) ) );
      poly_simp.add(polyline[index_cur]);
    }
    return poly_simp;
  }
  

  
  
  //----------------------------------------------------------------------------
  // INSIDE
  //----------------------------------------------------------------------------

  /**
   * 
   * check if a given coordinate pair is inside a polyline.
   * this method uses java.awt.Polygon;
   */
  public static final boolean INSIDE( Pixel[] polyline, int x, int y){
    int[] x_coords = new int[polyline.length];
    int[] y_coords = new int[polyline.length];
    int idx = 0;
    for( Pixel p : polyline ){
      x_coords[idx] = p.x_;
      y_coords[idx] = p.y_;
      idx++;
    }
    Polygon polygon = new Polygon(x_coords, y_coords, polyline.length);
    return polygon.contains(x, y);
  }
  
  /**
   * 
   * check if a given coordinate pair is inside a polyline.
   * this method uses java.awt.Polygon;
   */
  public static final boolean INSIDE( List<Pixel> polyline, int x, int y){
    int[] x_coords = new int[polyline.size()];
    int[] y_coords = new int[polyline.size()];
    int idx = 0;
    for( Pixel p : polyline ){
      x_coords[idx] = p.x_;
      y_coords[idx] = p.y_;
      idx++;
    }
    Polygon polygon = new Polygon(x_coords, y_coords, polyline.size());
    return polygon.contains(x, y);
  }
  
  /**
   * 
   * check if a given coordinate pair is inside a contour.
   * this method uses java.awt.Polygon;
   */
  public static final boolean INSIDE( Contour contour, int x, int y){
    return INSIDE(contour.getPixels(), x, y);
  }
  /**
   * 
   * check if a given coordinate pair is inside a convex_hull.
   * this method uses java.awt.Polygon;
   */
  public static final boolean INSIDE( ConvexHullDiwi convex_hull, int x, int y){
    DoubleLinkedList<Pixel> list = convex_hull.get();
    list.gotoFirst();
    Pixel p;
    int[] x_coords = new int[list.size()];
    int[] y_coords = new int[list.size()];
    

    for(int idx = 0; idx < list.size(); idx++, list.gotoNext()){
      p = list.getCurrentNode().get();
      x_coords[idx] = p.x_;
      y_coords[idx] = p.y_;
    }
    Polygon polygon = new Polygon(x_coords, y_coords, list.size());
    return polygon.contains(x, y);
  }
  
  
  
  
  
  
  
  
  
  
  /**
   * 
   * returns the distance between two pixels
   * 
   */
  public static final double distance(Pixel p1, Pixel p2){
    return distance(p1.x_, p1.y_, p2.x_, p2.y_ );
  }
  
  /**
   * 
   * returns the distance between two coordinate pairs
   * 
   */
  public static final double distance(float x1, float y1,float x2, float y2){
    float dx = x1-x2;
    float dy = y1-y2;
    return Math.sqrt(dx*dx+ dy*dy);
  }

    
  /**
   * 
   * returns the shortest distance of a point(p3) to a line (p1-p2)
   * 
   */
  public static final float distancePoint2Line(Pixel p1, Pixel p2, Pixel p3 ){
    float x1 = p2.x_-p1.x_, y1 = p2.y_-p1.y_;
    if( x1 == 0 && y1 == 0)
      return 0;
    float x2 = p3.x_-p1.x_, y2 = p3.y_-p1.y_;
    if( x2 == 0 && y2 == 0)
      return 0;
    float A = x1*y2 - x2*y1;
    if( A == 0)
      return 0;
    float p1p2_mag = (float)Math.sqrt(x1*x1 + y1*y1);
    return A/p1p2_mag;
  }
  
  
  private final static float abs(float val){
    return val < 0 ? -val : val;
  }
  
  
  
}
