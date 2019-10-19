
public void keyPressed() {
  if ( key == CODED ) {
    if ( keyCode == UP)   detection_resolution++;
    if ( keyCode == DOWN) detection_resolution--;
    if ( detection_resolution < 1 ) detection_resolution = 1;
  }
}

public void keyReleased() {
  if ( key == 'c') printCamSettings(cam);
}

