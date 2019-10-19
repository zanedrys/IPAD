
public void keyPressed() {
  if ( key == CODED ) {
    if ( keyCode == UP)   detection_resolution++;
    if ( keyCode == DOWN) detection_resolution--;
    if ( detection_resolution < 1 ) detection_resolution = 1;
  }
}

public void keyReleased() {
  if ( key == 'c') printCamSettings(cam);
  
  if (key == 'e') {
    if( mm != null){
      endMovie();
    }
  }
  if (key == 's') {
    if (mm == null){
      startMovie();
    }
  }
}




void startMovie(){
   String movie_name = "VIDEO/diewald_CV_kit_kinect_3d_pointcloud.mov";
   mm = new MovieMaker(this, width, height,  movie_name,
                       20, MovieMaker.JPEG, MovieMaker.BEST);
   println("----- STARTED MOVIE -----" + movie_name);
}

void endMovie(){
  mm.finish();  // Finish the movie if space bar is pressed!
  mm = null;
  println("----- FINISHED MOVIE -----");
}


