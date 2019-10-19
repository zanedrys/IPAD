
//-------------------------------------------------------------------
void drawPointCloud(int resolution ) {  
  // get the kinects 3d-data (by reference)
  KinectPoint3D kinect_3d[] = k3d_.get3D();


  int cam_w_ = size_x;
  int cam_h_ = size_y;

  strokeWeight(1f); 

  int default_color = color(0);
  stroke( default_color );

  KinectPoint3D point;
  for (int y = 0; y < cam_h_-resolution ; y += resolution) {
    for (int x = 0; x< cam_w_-resolution ; x += resolution) {
      int index1 = y*cam_w_+x;
      point = kinect_3d[index1];

      if (point.z > 0 ) continue; // no valid 3d-point

      float cx = point.x * 100;
      float cy = point.y * 100;
      float cz = point.z * 100;
      point(cx, cy, cz);
    }
  }
} 





public void drawDetectionBox() {
  float x_min = detection_area.xMin();
  float y_min = detection_area.yMin();
  float z_min = blobsable_kinect3d.getMinDepth();
  float x_max = detection_area.xMax();
  float y_max = detection_area.yMax();
  float z_max = blobsable_kinect3d.getMaxDepth();

  KinectPoint3D kp3d_000 = k3d_.getTransformedPixel(x_min, y_min, z_min);
  KinectPoint3D kp3d_100 = k3d_.getTransformedPixel(x_max, y_min, z_min);
  KinectPoint3D kp3d_110 = k3d_.getTransformedPixel(x_max, y_max, z_min);
  KinectPoint3D kp3d_010 = k3d_.getTransformedPixel(x_min, y_max, z_min);

  KinectPoint3D kp3d_001 = k3d_.getTransformedPixel(x_min, y_min, z_max);
  KinectPoint3D kp3d_101 = k3d_.getTransformedPixel(x_max, y_min, z_max);
  KinectPoint3D kp3d_111 = k3d_.getTransformedPixel(x_max, y_max, z_max);
  KinectPoint3D kp3d_011 = k3d_.getTransformedPixel(x_min, y_max, z_max);

  stroke(0); 
  strokeWeight(1);
  noFill();

  //    //front
  //    fill(255, 0, 0, 100);
  //    beginShape();
  //      vertex(kp3d_000.x, kp3d_000.y, kp3d_000.z);
  //      vertex(kp3d_100.x, kp3d_100.y, kp3d_100.z);
  //      vertex(kp3d_110.x, kp3d_110.y, kp3d_110.z);
  //      vertex(kp3d_010.x, kp3d_010.y, kp3d_010.z);
  //      vertex(kp3d_000.x, kp3d_000.y, kp3d_000.z);
  //    endShape();
  //    
  //    
  //    //back
  //    fill(0, 0, 255, 100);
  //    beginShape();
  //      vertex(kp3d_001.x, kp3d_001.y, kp3d_001.z);
  //      vertex(kp3d_101.x, kp3d_101.y, kp3d_101.z);
  //      vertex(kp3d_111.x, kp3d_111.y, kp3d_111.z);
  //      vertex(kp3d_011.x, kp3d_011.y, kp3d_011.z);
  //      vertex(kp3d_001.x, kp3d_001.y, kp3d_001.z);
  //    endShape();

  noFill();
  //bottom
  //    fill(0, 0, 255, 50);
  beginShape();
  vertex(kp3d_110.x, kp3d_110.y, kp3d_110.z);
  vertex(kp3d_010.x, kp3d_010.y, kp3d_010.z);
  vertex(kp3d_011.x, kp3d_011.y, kp3d_011.z);
  vertex(kp3d_111.x, kp3d_111.y, kp3d_111.z);
  vertex(kp3d_110.x, kp3d_110.y, kp3d_110.z);
  endShape();


  //top
  //    fill(0, 0, 255, 50);
  beginShape();
  vertex(kp3d_000.x, kp3d_000.y, kp3d_000.z);
  vertex(kp3d_100.x, kp3d_100.y, kp3d_100.z);
  vertex(kp3d_101.x, kp3d_101.y, kp3d_101.z);
  vertex(kp3d_001.x, kp3d_001.y, kp3d_001.z);
  vertex(kp3d_000.x, kp3d_000.y, kp3d_000.z);
  endShape();


  //left
  //    fill(0, 0, 255, 50);
  beginShape();
  vertex(kp3d_000.x, kp3d_000.y, kp3d_000.z);
  vertex(kp3d_001.x, kp3d_001.y, kp3d_001.z);
  vertex(kp3d_011.x, kp3d_011.y, kp3d_011.z);
  vertex(kp3d_010.x, kp3d_010.y, kp3d_010.z);
  vertex(kp3d_000.x, kp3d_000.y, kp3d_000.z);
  endShape();

  //right
  //    fill(0, 0, 255, 50);
  beginShape();
  vertex(kp3d_100.x, kp3d_100.y, kp3d_100.z);
  vertex(kp3d_101.x, kp3d_101.y, kp3d_101.z);
  vertex(kp3d_111.x, kp3d_111.y, kp3d_111.z);
  vertex(kp3d_110.x, kp3d_110.y, kp3d_110.z);
  vertex(kp3d_100.x, kp3d_100.y, kp3d_100.z);
  endShape();
}


public void drawBoundingBox3d(float x_min, float y_min, float z_min, float x_max, float y_max, float z_max) {
  stroke(255, 125, 0); 
  strokeWeight(2); 
  noFill();
  beginShape();
  vertex(x_min, y_min, z_min);
  vertex(x_max, y_min, z_min);
  vertex(x_max, y_max, z_min);
  vertex(x_min, y_max, z_min);
  vertex(x_min, y_min, z_min);
  endShape();

  beginShape();
  vertex(x_min, y_min, z_max);
  vertex(x_max, y_min, z_max);
  vertex(x_max, y_max, z_max);
  vertex(x_min, y_max, z_max);
  vertex(x_min, y_min, z_max);
  endShape();

  line(x_min, y_min, z_min, 
  x_min, y_min, z_max);

  line(x_max, y_min, z_min, 
  x_max, y_min, z_max);

  line(x_max, y_max, z_min, 
  x_max, y_max, z_max);

  line(x_min, y_max, z_min, 
  x_min, y_max, z_max);
}


public void drawWKS(float size) {
  strokeWeight(3);
  stroke(255, 0, 0); 
  line(0, 0, 0, size, 0, 0);
  stroke(0, 255, 0); 
  line(0, 0, 0, 0, size, 0);
  stroke(0, 0, 255); 
  line(0, 0, 0, 0, 0, size);
}




