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



/**
 *
 * a class to generate/analyse colors in a fast way.<br>
 * 
 * 
 * @author thomas diewald (c) 2011
 *
 */
public final class PixelColor {
  private int r_ = 0;
  private int g_ = 0;
  private int b_ = 0;
  private int a_ = 0xFF; //alpha
  
  
  public PixelColor(int rgba){
    setCol(rgba);
  }
  
  public PixelColor(int r, int g, int b){
    setCol(r, g, b);
  }
 
  public PixelColor(int r, int g, int b, int a){
    setCol(r, g,  b, a);
  }
  
  public final void setCol(int rgba){
    a_ = (rgba >> 24) & 0xff;
    r_ = (rgba >> 16) & 0xff;
    g_ = (rgba >>  8) & 0xff;
    b_ = (rgba >>  0) & 0xff;
  }
  
  public final void setCol(int r, int g, int b){
    r_ = r;
    g_ = g;
    b_ = b;
    a_ = 0xFF;
  }
  
  public final void setCol(int r, int g, int b, int a){
    r_ = r;
    g_ = g;
    b_ = b;
    a_ = a;
  }
  
  public final void setR(int r){ r_ = r; }
  public final void setG(int g){ g_ = g; }
  public final void setB(int b){ b_ = b; }
  public final void setA(int a){ a_ = a; }
  
  public final int red  (){return r_; }
  public final int green(){return g_; }
  public final int blue (){return b_; }
  public final int alpha(){return a_; }
  
  
  public final int getCol(){
    return ( a_ << 24) | (r_ << 16) | (g_ << 8)| (b_ << 0);
  }
  
  public final String toString(){
    return "rgba: "+ r_ +", "+ g_ +", "+ b_ +", "+ a_;
  }
  
  
  
  
  
  
  
  //----------------------------------------------------------------------------
  // STATIC
  //----------------------------------------------------------------------------
  
  public static final int color(int r, int g, int b, int a){
    return ( a << 24) | (r << 16) | (g << 8)| (b << 0);
  }
  
  public static final int color(int r, int g, int b){
    return 0xFF000000 | (r << 16) | (g << 8)| (b << 0);
  }
  
  public static final int color(int gray, int a){
    return ( a << 24) | (gray << 16) | (gray << 8)| (gray << 0);
  }
  
  public static final int color(int gray){
    return 0xFF000000 | (gray << 16) | (gray << 8)| (gray << 0);
  }
  
  public static final int alpha( int rgba ){return (rgba >> 24) & 0xFF; }
  public static final int red  ( int rgba ){return (rgba >> 16) & 0xFF; }
  public static final int green( int rgba ){return (rgba >>  8) & 0xFF; }
  public static final int blue ( int rgba ){return (rgba >>  0) & 0xFF; }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /** 
   * converts a rgb-value (single integer) value, to an hsb-value.
   * resulting values: hue (0-360), saturation(0-100), brightness(0-100)
   * 
   * @param rgb  an red-color
   *             
   * @return the hsb-color as an new array, if no parameter was given(hsb)
   * 
   */
  public static final float[] rgb2hsb(int rgb, float hsb[]){
    return rgb2hsb((rgb>>16)&0xFF, (rgb>>8)&0xFF, rgb&0xFF, hsb);
  } 
  
  
  
  /** 
   * converts a rgb-value (red, green, blue) value, to an hsb-value.
   * resulting values: hue (0-360), saturation(0-100), brightness(0-100)
   * 
   * @param r    red
   * @param g    green
   * @param b    blue
   * @param hsb  array (size has to be 3), to save the values
   *             if hsb == null, a new array will be generated
   *             which will slow down execution!!!
   *             
   * @return the hsb-color as an new array, if no parameter was given(hsb)
   * 
   */
  private final static float factor_s_b_ = 100/255f; // to map brighntess between 0 ... 100
  public static final float[] rgb2hsb(float r, float g, float b, float hsb[]){
    if( hsb == null )
      hsb = new float[3]; // fastest, if its defined outside of the function!
    // find min / max
    float MAX = r, MIN = g;
    if( r < g ){MIN = r; MAX = g;}
    if( b > MAX ) MAX = b;
    if( b < MIN ) MIN = b;
    
//    float factor_s_b_ = 100/255f; // to map brighntess between 0 ... 100

    if( MAX == MIN ){ // color is gray
      hsb[0] = 0;
      hsb[1] = 0;
      hsb[2] = MAX * factor_s_b_;
      return hsb;
    } else { 
      float hue_tmp = 0;
      
      float factor_h_ = 60f/(MAX-MIN);
      if     ( MAX == r ) hue_tmp = factor_h_ * (g-b) ;
      else if( MAX == g ) hue_tmp = factor_h_ * (b-r) + 120f;
      else if( MAX == b ) hue_tmp = factor_h_ * (r-g) + 240f;

      if( hue_tmp <  0 )  hue_tmp += 360f;

      hsb[0] = hue_tmp;
      hsb[1] = 100*(MAX-MIN)/MAX;
      hsb[2] = MAX * factor_s_b_;

      return hsb;
    }
  } 
  
  

  public static final float hue(int rgb){
    return hue((rgb>>16)&0xFF, (rgb>>8)&0xFF, rgb&0xFF);
  } 
  
  public static final float hue(float r, float g, float b){
    float MAX = r, MIN = g;
    if( r < g ){MIN = r; MAX = g;}
    if( b > MAX ) MAX = b;
    if( b < MIN ) MIN = b;
   
    if( MAX == MIN ) return 0;
    float h_ = 0;
    h_ = 0;
    float factor_h_ = 60f/(MAX-MIN);
    if     ( MAX == r ) h_ = factor_h_ * (g-b) ;
    else if( MAX == g ) h_ = factor_h_ * (b-r) + 120f;
    else if( MAX == b ) h_ = factor_h_ * (r-g) + 240f;
    if( h_ <  0 )  h_ += 360f;
    return h_;
  } 
  
  
  
  public static final float saturation(int rgb){
    return saturation((rgb>>16)&0xFF, (rgb>>8)&0xFF, rgb&0xFF);
  } 
  
  public static final float saturation(float r, float g, float b){
    float MAX = r, MIN = g;
    if( r < g ){MIN = r; MAX = g;}
    if( b > MAX ) MAX = b;
    if( b < MIN ) MIN = b;
    if( MAX == MIN ) return 0;
    return 100*(MAX-MIN)/MAX;
  } 
  
  
  
  public static final float brighntess(int rgb){
    return brighntess((rgb>>16)&0xFF, (rgb>>8)&0xFF, rgb&0xFF);
  } 
  

  public static final float brighntess(float r, float g, float b){
    float MAX = r; 
    if( MAX < g ) MAX = g;
    if( MAX < b ) MAX = b;
    return MAX * factor_s_b_;
  } 
  

  
  
  
  /** 
   * converts a hsb-value (hue, saturation, brightness) value, to an rgb-value.
   * resulting values: red (0-255), green (0-255), blue (0-255)
   * 
   * @param h    hue
   * @param s    saturation
   * @param b    brightness
   * @param rgb  array (size has to be 3), to save the values
   *             if rgb == null, a new array will be generated
   *             which will slow down execution!!!
   *             
   * @return the rgb-color as an new array, if no parameter was given(rgb)
   */
  public static final float[] hsb2rgb(float h, float s, float b, float rgb[]){
    if( rgb == null )
      rgb = new float[3];
     
    float hi = h *   .0166666666666666f; // -->      h/ 60f, but MUCH faster
    float S  = s *   .01f;               // -->      s/100f, but MUCH faster
    float V  = b * 2.55f;                // -->  255*b/100f, but MUCH faster
    
    int hi_int = (int)hi;
    float f = hi - hi_int;
    
    float p = V * ( 1 - S );
    float q = V * ( 1 - S * f);
    float t = V * ( 1 - S * ( 1 - f ) );
    
    switch ( hi_int){
      case 0: rgb[0] = V; rgb[1] = t; rgb[2] = p; break;
      case 1: rgb[0] = q; rgb[1] = V; rgb[2] = p; break;
      case 2: rgb[0] = p; rgb[1] = V; rgb[2] = t; break;
      case 3: rgb[0] = p; rgb[1] = q; rgb[2] = V; break;
      case 4: rgb[0] = t; rgb[1] = p; rgb[2] = V; break;
      case 5: rgb[0] = V; rgb[1] = p; rgb[2] = q; break;
      case 6: rgb[0] = V; rgb[1] = t; rgb[2] = p; break;
    }

    return rgb;
  } 
  
  
  
  
  
  
  
  
  
  /** 
   * converts a hsb-value (hue, saturation, brightness) value, to an rgb-value.
   * resulting values: red (0-255), green (0-255), blue (0-255)
   * 
   * @param h    hue
   * @param s    saturation
   * @param b    brightness
   * 
   * @return the rgb-color as an integer
   */
  public static final int hsb2rgb(float h, float s, float b){

    float hi = h *   .0166666666666666f; // -->      h/ 60f, but MUCH faster
    float S  = s *   .01f;               // -->      s/100f, but MUCH faster
    float V  = b * 2.55f;                // -->  255*b/100f, but MUCH faster
    
    int hi_int = (int)hi;
    float f = hi - hi_int;
    
    float p = V * ( 1 - S );
    float q = V * ( 1 - S * f);
    float t = V * ( 1 - S * ( 1 - f ) );
    
    float r_ = 0, g_ = 0, b_ = 0;
    
    switch ( hi_int){
      case 0: r_ = V; g_ = t; b_ = p; break;
      case 1: r_ = q; g_ = V; b_ = p; break;
      case 2: r_ = p; g_ = V; b_ = t; break;
      case 3: r_ = p; g_ = q; b_ = V; break;
      case 4: r_ = t; g_ = p; b_ = V; break;
      case 5: r_ = V; g_ = p; b_ = q; break;
      case 6: r_ = V; g_ = t; b_ = p; break;
    }

    return 0xFF000000 | ((int)r_ << 16) | ((int)g_ << 8)| ((int)b_ << 0);
  } 
  
  
  
  
  
  
  
  
  
  
  

  
  
 
}
