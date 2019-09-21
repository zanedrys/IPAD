import sys
import RPi.GPIO as GPIO
import time

#GPIO setup and assignments:
GPIO.setmode(GPIO.BOARD) 

step_enable = 12
motorL_pin_step = 7
motorL_pin_dir = 11
motorR_pin_step = 13
motorR_pin_dir = 15
servo_pin = 12

GPIO.setup(step_enable, GPIO.OUT)
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
        delay = 0.001           #Delay between high/low for each motor step. Affects speed and accuracy.

#move shit

print("Moving shit")

GPIO.output(step_enable, False) #yup, low enables the steppers.

GPIO.output(motorL_pin_dir, True):
for i in range(512):
    GPIO.output(motorL_pin_step,True)
    GPIO.output(motorL_pin_step,False)
    time.sleep(delay)

GPIO.output(motorL_pin_dir, False):
    for i in range(512):
        GPIO.output(motorL_pin_step,True)
        GPIO.output(motorL_pin_step,False)
        time.sleep(delay)


#todo
    #load and scale path to fit within print dimensions
    #run loop on each path:
        #Find x,y length delta between current point (x,y coordinate) (origin at beginning) and next point in path.
            #convert point to lengths 
            #difference between previous point length and next point length.
        #Length to number of steps, based off of stepsPerRev and distancePerStep
        #lift servo if movement isn't for a line.
        #for each step on each motor switch low/high on motorstep pin, set direction pin.


GPIO.cleanup()