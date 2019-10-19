//new BLOBable class, that implements the BLOBable-interface.
public final class BLOBable_Colorizer implements BLOBable{
  int width_, height_;
  private float hsb_[] = new float[3];
  private float mousex_val_, mousey_val_;
  private String name_; 
  private PApplet papplet_;
  private PImage img_;
  int pixels[];
  
  public BLOBable_Colorizer(PApplet papplet, PImage img){
    papplet_ = papplet;
    img_ = img;
  }
  
  //@Override
  public final void init() {
    name_ = this.getClass().getSimpleName(); 
  }
  
  //@Override
  public final void updateOnFrame(int width, int height) {
    width_ = width;
    height_ = height;
    pixels = img_.pixels;
//    mousex_val_ = PApplet.map(papplet_.mouseX, 0, papplet_.width,  0, 100);
//    mousey_val_ = PApplet.map(papplet_.mouseY, 0, papplet_.height, 0, 100);
//    println("MY NAME IS: " +this.getClass().getSimpleName());
  }
  //@Override
  public final boolean isBLOBable(int pixel_index,  int x, int y) {
    if ( (PixelColor.brighntess(pixels[pixel_index]) > 50) )
//    if ( (PixelColor.brighntess(pixels[y*width_+x]) >50) )
//  public final boolean isBLOBable(int pixel_color, int x, int y) {
//    if ( (PixelColor.brighntess(pixel_color) >50) )
      return true;
    else
      return false;
  }
}
