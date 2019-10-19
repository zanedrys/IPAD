public final class BLOBable_redBlobs implements BLOBable{
  private PImage img_;
  int col = color(255, 0, 0);
  
  public BLOBable_redBlobs(PImage img){
    img_ = img;
  }
  
  //@Override
  public final void init() {
  }
  
  //@Override
  public final void updateOnFrame(int width, int height) {
  }
  //@Override
  public final boolean isBLOBable(int pixel_index, int x, int y) {
    if( img_.pixels[pixel_index] ==  col){
      return true;
    } else {
      return false;
    }
  }
}






public final class BLOBable_greenBlobs implements BLOBable{
  private PImage img_;
  int col = color(0, 255, 0);
  public BLOBable_greenBlobs(PImage img){
    img_ = img;
  }
  
  //@Override
  public final void init() {
  }
  
  //@Override
  public final void updateOnFrame(int width, int height) {
  }
  //@Override
  public final boolean isBLOBable(int pixel_index, int x, int y) {
    if( img_.pixels[pixel_index] == col){
      return true;
    } else {
      return false;
    }
  }
}






