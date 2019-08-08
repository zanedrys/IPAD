import sys
import RPi.GPIO as GPIO

#GPIO setup and assignments:
GPIO.setmode(GPIO.BOARD) 

motorL_pin_step = 17
motorL_pin_dir = 4
motorR_pin_step = 22
motorR_pin_dir = 23
servo_pin = 18

GPIO.setup(motorL_pin_step, GPIO.OUT)
GPIO.setup(motorL_pin_dir, GPIO.OUT)
GPIO.setup(motorR_pin_step, GPIO.OUT)
GPIO.setup(motorR_pin_dir, GPIO.OUT)
GPIO.setup(servo_pin, GPIO.OUT)

#Machine Constants
    #Motor Properties:
        stepsPerRev = 400       #Steps per full rotation of stepper motor
        gearRadius = 5          #Radius of the cable gear.
        reverseMotorL = False   #swap motor direction
        reverseMotorR = False 
    #Machine Dimensions:
        motorSeparation = 1000  #todo, distance between the motors (mm)
        originMotor1 = 0        #length of cable from motors at origin (top left print area)
        originMotor2 = 0        
        printWidth = 0          #Print dimensions from origin
        printHeight = 0
        delay = 0.005           #Delay between high/low for each motor step. Affects speed and accuracy.

#todo
    #load and scale path to fit within print dimensions
    #run loop on each path:
        #Find x,y length delta between current point (x,y coordinate) (origin at beginning) and next point in path.
            #convert point to lengths 
            #difference between previous point length and next point length.
        #Length to number of steps, based off of stepsPerRev and distancePerStep
        #lift servo if movement isn't for a line.
        #for each step on each motor switch low/high on motorstep pin, set direction pin.
