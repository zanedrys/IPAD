


//---------------------------------------------------------------------------------------------------- 
void initPeasyCam() {
  cam = new PeasyCam(this, 0, 0, 350, 400);
  cam.setMinimumDistance(1);
  cam.setMaximumDistance(100000);
  cam.setRotations(2.9, -0.2, 3.07); 

  cam.setDistance (381.16);
  cam.setRotations(  0.18,    0.52,   -0.12);
  cam.lookAt      (-44.28,   36.57, -250.13);

}


public void printCamSettings(PeasyCam camera) {
  float[] la  = camera.getLookAt();
  float[] rot = camera.getRotations();
  float[] pos = camera.getPosition();
  float dis   = (float)camera.getDistance();

  String rotation = String.format(Locale.ENGLISH, "%7.2f, %7.2f, %7.2f ", rot[0], rot[1], rot[2]);
  String look_at  = String.format(Locale.ENGLISH, "%7.2f, %7.2f, %7.2f ", la[0], la[1], la[2]);
  String position = String.format(Locale.ENGLISH, "%7.2f, %7.2f, %7.2f ", pos[0], pos[1], pos[2]);
  String distance = String.format(Locale.ENGLISH, "%7.2f ", dis);
  println("distance = "+ distance);
  println("rotation = "+ rotation);
  println("look_at  = "+ look_at);
  println("position = "+ position);
}


