#Step 0: Preamble
#------------------------------------------------------------------------
#------------------------------------------------------------------------
#Code Written by: Salty Scott
#Current Project: www.rowboboat.com
#This code is a very basic example of using python to control a spark fun
# easy driver.  The spark fun easy driver that I am using in this example
# is connected to a 42HS4013A4 stepper motor and my raspberry pi.  Pin 23
# is the direction control and pin 24 is the step control.  I am using
# these components in the www.rowboboat.com project version 2.0 and I
# hope someone finds this a useful and simple example.
#------------------------------------------------------------------------
#------------------------------------------------------------------------

#Step 1: Import necessary libraries 
#------------------------------------------------------------------------
#------------------------------------------------------------------------
import sys
import RPi.GPIO as gpio #https://pypi.python.org/pypi/RPi.GPIO more info
import time
#------------------------------------------------------------------------
#------------------------------------------------------------------------

#Step 2: Read arguements https://www.youtube.com/watch?v=kQFKtI6gn9Y
#------------------------------------------------------------------------
#------------------------------------------------------------------------
#read the direction and number of steps; if steps are 0 exit 
try: 
    direction = sys.argv[1]
    steps = int(float(sys.argv[2]))
except:
    steps = 0

#print which direction and how many steps 
print("You told me to turn %s %s steps.") % (direction, steps)
#------------------------------------------------------------------------
#------------------------------------------------------------------------


#Step 3: Setup the raspberry pi's GPIOs
#------------------------------------------------------------------------
#------------------------------------------------------------------------
#use the broadcom layout for the gpio
gpio.setmode(gpio.BCM)
#GPIO23 = Direction
#GPIO24 = Step
gpio.setup(23, gpio.OUT)
gpio.setup(24, gpio.OUT)
#------------------------------------------------------------------------
#------------------------------------------------------------------------


#Step 4: Set direction of rotation
#------------------------------------------------------------------------
#------------------------------------------------------------------------
#set the direction equal to the appropriate gpio pin
if direction == 'left':
    gpio.output(23, True)
elif direction == 'right':
    gpio.output(23, False)
#------------------------------------------------------------------------
#------------------------------------------------------------------------


#Step 5: Setup step counter and speed control variables
#------------------------------------------------------------------------
#------------------------------------------------------------------------
#track the numebr of steps taken
StepCounter = 0

#waittime controls speed
WaitTime = 0.000001
#------------------------------------------------------------------------
#------------------------------------------------------------------------


#Step 6: Let the magic happen
#------------------------------------------------------------------------
#------------------------------------------------------------------------
# Start main loop
while StepCounter < steps:

    #turning the gpio on and off tells the easy driver to take one step
    gpio.output(24, True)
    gpio.output(24, False)
    StepCounter += 1

    #Wait before taking the next step...this controls rotation speed
    time.sleep(WaitTime)
#------------------------------------------------------------------------
#------------------------------------------------------------------------


#Step 7: Clear the GPIOs so that some other program might enjoy them
#------------------------------------------------------------------------
#------------------------------------------------------------------------
#relase the GPIO
gpio.cleanup()
#------------------------------------------------------------------------
#------------------------------------------------------------------------
