# IPAD
Interactive Programmable Arts Display


Getting Started - Testing Hardware
  Below are the instructions to setup and test the hardware (CNC shield, Arduino, Stepper Motors, Drivers and various connections.

    1. Plug in necessary hardware, uno on arduino, 12v to cnc, steppers to cnc etc (See wiring diagram)
    2. Download and install Arduino IDE https://www.arduino.cc/en/main/software
    3. Download GRBL https://github.com/gnea/grbl/releases
    4. Download XLoader http://www.hobbytronics.co.uk/arduino-xloader
    5. Follow instructions to get GRBL on the Arduino/cnc
    6. Download Universal Gcode Sender https://winder.github.io/ugs_website/
    7. UGS should detect the CNC shield and any connected motors
    8. Plug and Play Away
    
    
Setting up a Gamepad/Joystick   
  Below are the methods to using a joystick/gamepad or any analog input method.
 
    1. Plug in necessary component
    2. Download and install Joys2Key
    3. Downloiad the library file JoyToKeyToPlotter.cfg file
    4. Import file to JoyToKey
    4. From within UGS (Universal Gcode Sender), use the joystick 
