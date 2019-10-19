

//new BLOBable class, that implements the BLOBable-interface.
public final class BLOBable_Kinect_2D implements BLOBable{
  int width_, height_;
  private String name_; 
  
  private PApplet papplet_;
  private KinectFrameDepth kinect_depth_;
  private int[] kinect_depth_values;
  
  public BLOBable_Kinect_2D(PApplet papplet){
    papplet_ = papplet;
  }
  
  public BLOBable_Kinect_2D setKinectDepth(KinectFrameDepth kinect_depth){
    kinect_depth_ = kinect_depth;
    return this;
  }
  

  
  //@Override
  public final void init() {
    name_ = this.getClass().getSimpleName(); 
  }
  
  //@Override
  public final void updateOnFrame( int width, int height) {
    width_ = width;
    height_ = height;
//    println("MY NAME IS: " +this.getClass().getSimpleName()); 
    kinect_depth_values = kinect_depth_.getRawDepth();

  }
  
  //@Override
  public final boolean isBLOBable(int pixel_index, int x, int y) {
    
    float depth = kinect_depth_values[pixel_index];
    if ( depth > 650 && depth < 800 ) {
      
//    float hue = PixelColor.hue(kinect_depth_values[pixel_index]);
//    if ( hue > 200 && hue < 300 ) {
      return true;
    } else {
      return false;
    }
  }
}
