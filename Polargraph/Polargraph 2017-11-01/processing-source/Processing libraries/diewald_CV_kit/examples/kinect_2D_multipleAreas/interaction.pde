

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
