import RPi.GPIO as GPIO
import time
from time import sleep

# Define GPIO pins
in1 = 24
in2 = 23
en = 5
in3 = 22
in4 = 27
en_a = 18

# GPIO setup
GPIO.setmode(GPIO.BCM)
GPIO.setup(in1, GPIO.OUT)
GPIO.setup(in2, GPIO.OUT)
GPIO.setup(en, GPIO.OUT)
GPIO.setup(in3, GPIO.OUT)
GPIO.setup(in4, GPIO.OUT)
GPIO.setup(en_a, GPIO.OUT)


# Ensure all pins are low at startup
GPIO.output(in1, GPIO.LOW)
GPIO.output(in2, GPIO.LOW)
GPIO.output(in3, GPIO.LOW)
GPIO.output(in4, GPIO.LOW)

# PWM setup
p = GPIO.PWM(en, 1000)
p_a = GPIO.PWM(en_a, 1000)
p.start(25)
p_a.start(25)
p.ChangeDutyCycle(25)
p_a.ChangeDutyCycle(25)

# Control functions
def backward():
    print("backward")
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)

def forward():
    print("forward")
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)

def stopCar():
    print("stop")
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.LOW)

def turnRight():
    print("right")
    GPIO.output(in1, GPIO.HIGH)
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)
    GPIO.output(in4, GPIO.HIGH)

def turnLeft():
    print("left")
    GPIO.output(in1, GPIO.LOW)
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.HIGH)
    GPIO.output(in4, GPIO.LOW)

def slowDownCar():
    print("low")
    p.ChangeDutyCycle(25)
    p_a.ChangeDutyCycle(25)

def speedUpCar():
    print("high")
    p.ChangeDutyCycle(75)
    p_a.ChangeDutyCycle(75)

def mediumUpCar():
    print("medium")
    p.ChangeDutyCycle(50)
    p_a.ChangeDutyCycle(50)

def testMove():
    print("test")
    forward()
    sleep(5)
    turnLeft()
    sleep(5)
    backward()
    sleep(5)
    stopCar()


def turnLeft180():
    """
    Cette fonction permet de tourner a gauche de 180 degree 
    """
    print("Left 180")
    GPIO.output(in1, GPIO.HIGH)  # Reculer moteur gauche
    GPIO.output(in2, GPIO.LOW)
    GPIO.output(in3, GPIO.LOW)  # Avancer moteur droit 
    GPIO.output(in4, GPIO.HIGH)
    #temp1 = 1
    #x = 'z'
    time.sleep(0.8)
    #temp1 = 1
    stopCar()
    #forward()
def turnRight180():
    """
    Cette fonction permet de tourner a droite de 180 degree 
    """
    print("right 180")
    GPIO.output(in1, GPIO.LOW)  # Avancer moteur gauche
    GPIO.output(in2, GPIO.HIGH)
    GPIO.output(in3, GPIO.HIGH)  # Reculer moteur droit
    GPIO.output(in4, GPIO.LOW)
    time.sleep(0.8)
    stopCar()
    
def move_forward_distance(distance_cm):
    start =time.time()
    speed =1.2
    total_time = distance_cm / speed
    forward()
    sleep(total_time)
    stopCar()
    #turnLeft180()
    end = time.time()
    print (end-start)
def cover(dis):
    move_forward_distance(dis)
    turnLeft180()
    stopCar()
    move_forward_distance(dis+0.25)
    #stopCar()
    turnRight180()
    #stopCar()
    #move_forward_distance(dis+0.25)
    #stopCar()
def cover_rectangle(length, width):
    pass_width = 1  # Width of each pass, adjust based on your tractor's effective width
    number_of_passes = int(width / pass_width)
    
    for pass_num in range(number_of_passes):
        # Move forward the full length of the rectangle
        move_forward_distance(length)
        # Turn around 180 degrees to come back
        turnRight()
        sleep(1)  # Short delay to stabilize after turn
        turnRight()
        sleep(1)  # Short delay to stabilize after turn
        
        # Move forward the full length of the rectangle
        move_forward_distance(length)
        
        # Prepare for the next pass, if there's more area to cover
        if pass_num < number_of_passes - 1:
            # Turn 90 degrees to the right to align for the next pass
            turnRight()
            sleep(1)  # Short delay to stabilize after turn
            # Move forward the width of one pass
            move_forward_distance(pass_width)
            # Turn 90 degrees to the right again to realign for the next length pass
            turnRight()
            sleep(1)  # Short delay to stabilize after turn

# Main loop
try:
    print("Ready for commands (r-forward, b-backward, rt-right, lt-left, l-low, m-medium, h-high, t-test, e-exit):")
    while True:
        x = input()
        if x == 'f':
            forward()
        elif x == 'b':
            backward()
        elif x == 's':
            stopCar()
        elif x == 'rt':
            turnRight180()
        elif x == 'lt':
            turnLeft()
        elif x == 'l':
            slowDownCar()
        elif x == 'm':
            mediumUpCar()
        elif x == 'h':
            speedUpCar()
        elif x == 't':
            testMove()
        elif x == 'd':
            distance = float(input("Type distance to go (m): "))
            #move_forward_distance(distance)
            #for i in 1..5:
            cover(distance)
            
        elif x == 'r':
            length = float(input("Enter the length of the rectangle (cm): "))
            width = float(input("Enter the width of the rectangle (cm): "))
            cover_rectangle(length, width)
            
        elif x == 'e':
            print("Exiting")
            break
        else:
            print("Invalid command. Try again.")
except KeyboardInterrupt:
    print("Program stopped by User")
except Exception as e:
    print("An error occurred: {}".format(e))
finally:
    GPIO.cleanup()
    print("GPIO Clean up")
