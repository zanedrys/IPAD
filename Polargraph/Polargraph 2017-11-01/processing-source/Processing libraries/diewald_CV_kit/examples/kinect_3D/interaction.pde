public void mousePressed() {
  detection_area.set(mouseX, mouseY, mouseX, mouseY);
}
public void mouseDragged() {
  int min_x = detection_area.xMin();
  int min_y = detection_area.yMin();
  int max_x = mouseX;
  int max_y = mouseY;

  if ( max_x < min_x )  max_x = min_x;
  if ( max_x > size_x ) max_x = size_x;
  if ( max_y < min_y )  max_y = min_y;
  if ( max_y > size_y ) max_y = size_y;
  detection_area.set(min_x, min_y, max_x, max_y);
}

public void keyPressed() {
  if ( key == CODED ) {
    if ( keyCode == UP)   detection_resolution++;
    if ( keyCode == DOWN) detection_resolution--;
    if ( detection_resolution < 1 ) detection_resolution = 1;
  }
}

public void keyReleased() {
  if ( key == 'b') draw_blobs_boundingsbox = !draw_blobs_boundingsbox;
  if ( key == 'f') draw_filled_blobs       = !draw_filled_blobs;
}

